package com.kit.lightserver.statemachine.states;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fap.framework.statemachine.ProcessingResult;
import com.fap.framework.statemachine.ResultStateTransition;
import com.fap.framework.statemachine.ResultWaitEvent;
import com.fap.framework.statemachine.StateSME;

import com.kit.lightserver.adapters.adapterout.AdoPrimitiveListEnvelope;
import com.kit.lightserver.domain.types.ConhecimentoSTY;
import com.kit.lightserver.domain.types.FormSTY;
import com.kit.lightserver.domain.types.NotafiscalSTY;
import com.kit.lightserver.services.db.forms.flags.FormFlagsServices;
import com.kit.lightserver.statemachine.StateMachineMainContext;
import com.kit.lightserver.statemachine.events.FormOperationClientSuccessEventSME;
import com.kit.lightserver.statemachine.types.CommunicationCTX;
import com.kit.lightserver.statemachine.types.ConversationFinishedStatusCTX;
import com.kit.lightserver.types.response.ChannelProgressRSTY;
import com.kit.lightserver.types.response.ClientResponseRSTY;
import com.kit.lightserver.types.response.FormContentFullConhecimentoRSTY;
import com.kit.lightserver.types.response.FormContentFullNotafiscalRSTY;
import com.kit.lightserver.types.response.FormContentFullRSTY;
import com.kit.lightserver.types.response.FormOperationGetStatusRSTY;
import com.kit.lightserver.types.response.FormOperationResetRSTY;

final class SendFormsState extends BaseState implements StateSME<KitEventSME> {

    static private final Logger LOGGER = LoggerFactory.getLogger(SendFormsState.class);

    static private final int MAX_FORMS_TO_SEND_UNTIL_CONFIRMATION = 40; // Calculado empiricamente pelo time-out

    private final CommunicationCTX communicationCTX;

    public SendFormsState(final StateMachineMainContext context, final CommunicationCTX communicationCTX) {
        super(context);
        this.communicationCTX = communicationCTX;
    }

    @Override
    public ProcessingResult<KitEventSME> transitionOccurred() {

        List<ClientResponseRSTY> clientResponses = new LinkedList<ClientResponseRSTY>();

        if( context.getClientInfo().isMustReset() ) { // Erasing the mobile memory if necessary
            FormOperationResetRSTY formOperationReset = new FormOperationResetRSTY();
            clientResponses.add(formOperationReset);
        }

        /*
         * The number of steps for the mobile progress bar
         */
        final int totalNumberOfFormsToSend = communicationCTX.getAllFormsListSize();
        LOGGER.info("totalNumberOfFormsToSend=" + totalNumberOfFormsToSend);


        ProcessingResult<KitEventSME> result;
        if( totalNumberOfFormsToSend > 0 ) {

            final ChannelProgressRSTY channelProgress = new ChannelProgressRSTY(totalNumberOfFormsToSend);
            clientResponses.add(channelProgress);

            // Getting the first *bunch* of forms and sending
            final List<FormSTY> formsToSend = communicationCTX.extractFormsToSendInOrder(MAX_FORMS_TO_SEND_UNTIL_CONFIRMATION);
            sendFormsAndRequestClientStatus(clientResponses, formsToSend);

            result = new ResultWaitEvent<KitEventSME>();

        }
        else {
            result = getRetrieveUpdatedFormsState();
        }

        if(clientResponses.size() > 0 ) {
            AdoPrimitiveListEnvelope primitivesEnvelope = new AdoPrimitiveListEnvelope(clientResponses);
            context.getClientAdapterOut().sendBack(primitivesEnvelope);
        }

        return result;

    }

    private ProcessingResult<KitEventSME> getRetrieveUpdatedFormsState() {
        LOGGER.info("communicationCTX.getFormsToSendOrderedListSize()=" + communicationCTX.getAllFormsListSize());
        final RetrieveUpdatedFormsState retrieveUpdatedFormsState = new RetrieveUpdatedFormsState(context);
        return new ResultStateTransition<KitEventSME>(retrieveUpdatedFormsState);
    }

    @Override
    public ProcessingResult<KitEventSME> processEvent(final KitEventSME event) {

        if (event instanceof FormOperationClientSuccessEventSME) {

            if( communicationCTX.getFormsSentWaitingForConfirmationList().size() == 0 ) { // Sanity
                LOGGER.error("Investigate. getFormsSentWaitingForConfirmationList().size()=0");
                return getRetrieveUpdatedFormsState();
            }

            /*
             * Flags the the forms were successful received
             */
            final FormFlagsServices formFlagsServices = FormFlagsServices.getInstance(context.getConfigAccessor());

            final String ktClientId = context.getClientInfo().getKtClientUserId();

            final boolean serviceSuccess =
                    formFlagsServices.flagFormsAsReceived(ktClientId, communicationCTX.getFormsSentWaitingForConfirmationList());

            if (serviceSuccess == false) {
                final StateSME<KitEventSME> errorState = UnrecoverableErrorState.getInstance(context, ConversationFinishedStatusCTX.FINISHED_GENERAL_ERROR);
                return new ResultStateTransition<KitEventSME>(errorState);
            }
            communicationCTX.clearSentListWaitingForConfirmation();

            /*
             * Getting the next forms to send
             */
            final List<FormSTY> formsToSend = communicationCTX.extractFormsToSendInOrder(MAX_FORMS_TO_SEND_UNTIL_CONFIRMATION);
            if( formsToSend.size() > 0) {

                List<ClientResponseRSTY> clientResponses = new LinkedList<ClientResponseRSTY>();
                sendFormsAndRequestClientStatus(clientResponses, formsToSend);

                if(clientResponses.size() > 0 ) {
                    AdoPrimitiveListEnvelope primitivesEnvelope = new AdoPrimitiveListEnvelope(clientResponses);
                    context.getClientAdapterOut().sendBack(primitivesEnvelope);
                }

                return new ResultWaitEvent<KitEventSME>();

            }
            else {
                LOGGER.info("Client Status is ok. All Forms successfully sent.");
                return getRetrieveUpdatedFormsState();
            }


        }

        else {

            /*
             * ERROR
             */
            if (communicationCTX.getAllFormsListSize() > 0) {
                LOGGER.error("There are still forms to be sent. communicationCTX.getAllFormsListSize()="+communicationCTX.getAllFormsListSize());
            }
            LOGGER.error("Invalid event. event=" + event);
            final StateSME<KitEventSME> newState = UnrecoverableErrorState.getInstance(context, ConversationFinishedStatusCTX.FINISHED_ERROR_UNEXPECTED_EVENT);

            return new ResultStateTransition<KitEventSME>(newState);

        }

    }// processEvent

    private void sendFormsAndRequestClientStatus(final List<ClientResponseRSTY> responseList, final List<FormSTY> formsToSend) {

        LOGGER.info("Sending forms. formsToSend.size()=" + formsToSend.size());

        if (formsToSend.size() == 0) { // Sanity
            LOGGER.error("Investigate. formsToSend.size() == 0");
        }

        if (formsToSend.size() > 0) {

            for (FormSTY formSTY : formsToSend) {

                final FormContentFullRSTY clientResponse;
                if (formSTY instanceof ConhecimentoSTY) {
                    final ConhecimentoSTY conhecimentoSTY = (ConhecimentoSTY) formSTY;
                    clientResponse = new FormContentFullConhecimentoRSTY(conhecimentoSTY);
                }
                else if (formSTY instanceof NotafiscalSTY) {
                    final NotafiscalSTY notafiscalSTY = (NotafiscalSTY) formSTY;
                    clientResponse = new FormContentFullNotafiscalRSTY(notafiscalSTY);
                }
                else {
                    final String errorMessage = "Unexpected type. formSTY=" + formSTY;
                    LOGGER.error(errorMessage);
                    throw new RuntimeException(errorMessage);
                }

                responseList.add(clientResponse);
                communicationCTX.addToFormSentList(formSTY);

            }// for

        }

        FormOperationGetStatusRSTY formOperationGetStatusRSTY = new FormOperationGetStatusRSTY();
        responseList.add(formOperationGetStatusRSTY);

    }

}// class
