package com.kit.lightserver.statemachine.states;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfap.framework.statemachine.ProcessingResult;
import com.jfap.framework.statemachine.ResultStateTransition;
import com.jfap.framework.statemachine.ResultWaitEvent;
import com.jfap.framework.statemachine.StateSME;
import com.kit.lightserver.domain.types.ConhecimentoSTY;
import com.kit.lightserver.domain.types.FormSTY;
import com.kit.lightserver.domain.types.NotafiscalSTY;
import com.kit.lightserver.services.be.forms.FormServices;
import com.kit.lightserver.statemachine.StateMachineMainContext;
import com.kit.lightserver.statemachine.events.FormOperationClientSuccessEventSME;
import com.kit.lightserver.statemachine.types.CommunicationCTX;
import com.kit.lightserver.statemachine.types.ConversationFinishedStatusCTX;
import com.kit.lightserver.types.response.ChannelProgressRSTY;
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

        /*
         * Erasing the mobile memory if necessary
         */
        if( context.getClientInfo().isMustReset() ) {
            final FormOperationResetRSTY formOperationReset = new FormOperationResetRSTY();
            context.getClientAdapterOut().sendBack(formOperationReset);
        }

        /*
         * The number of steps for the mobile progress bar
         */
        final int totalNumberOfFormsToSend = communicationCTX.getAllFormsListSize();
        LOGGER.info("totalNumberOfFormsToSend=" + totalNumberOfFormsToSend);


        ProcessingResult<KitEventSME> result;
        if( totalNumberOfFormsToSend > 0 ) {

            final ChannelProgressRSTY channelProgress = new ChannelProgressRSTY(totalNumberOfFormsToSend);
            context.getClientAdapterOut().sendBack(channelProgress);

            /*
             * Getting the first *bunch* of forms and sending
             */
            final List<FormSTY> formsToSend = communicationCTX.extractFormsToSendInOrder(MAX_FORMS_TO_SEND_UNTIL_CONFIRMATION);
            sendFormsAndRequestClientStatus(formsToSend);

            result = new ResultWaitEvent<KitEventSME>();
        }
        else {
            result = getRetrieveUpdatedFormsState();
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
            final String ktClientId = context.getClientInfo().getKtClientId();
            final boolean serviceSuccess = FormServices.flagFormsAsReceived(ktClientId, communicationCTX.getFormsSentWaitingForConfirmationList());
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
                sendFormsAndRequestClientStatus(formsToSend);
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



    private void sendFormsAndRequestClientStatus(final List<FormSTY> formsToSend) {

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

                context.getClientAdapterOut().sendBack(clientResponse);
                communicationCTX.addToFormSentList(formSTY);

            }// for

        }

        FormOperationGetStatusRSTY formOperationGetStatusRSTY = new FormOperationGetStatusRSTY();
        context.getClientAdapterOut().sendBack(formOperationGetStatusRSTY);

    }

}// class
