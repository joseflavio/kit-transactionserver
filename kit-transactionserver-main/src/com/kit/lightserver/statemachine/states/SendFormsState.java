package com.kit.lightserver.statemachine.states;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfap.framework.statemachine.ProcessingResult;
import com.jfap.framework.statemachine.ResultStateTransition;
import com.jfap.framework.statemachine.ResultWaitEvent;
import com.jfap.framework.statemachine.StateSME;
import com.kit.lightserver.domain.ConhecimentoSTY;
import com.kit.lightserver.domain.FormSTY;
import com.kit.lightserver.domain.NotafiscalSTY;
import com.kit.lightserver.services.be.forms.FormServices;
import com.kit.lightserver.statemachine.CommunicationCTX;
import com.kit.lightserver.statemachine.ConversationFinishedStatusCTX;
import com.kit.lightserver.statemachine.KitGeneralCTX;
import com.kit.lightserver.statemachine.events.FormOperationClientSuccessEventSME;
import com.kit.lightserver.statemachine.events.FormOperationUpdateFormsCompleteEventSME;
import com.kit.lightserver.types.response.ChannelProgressRSTY;
import com.kit.lightserver.types.response.FormContentFullConhecimentoRSTY;
import com.kit.lightserver.types.response.FormContentFullNotafiscalRSTY;
import com.kit.lightserver.types.response.FormContentFullRSTY;
import com.kit.lightserver.types.response.FormOperationGetStatusRSTY;
import com.kit.lightserver.types.response.FormOperationResetRSTY;

final class SendFormsState extends KitTransactionalAbstractState implements StateSME<KitEventSME> {

    static private final Logger LOGGER = LoggerFactory.getLogger(SendFormsState.class);

    static private final int MAX_FORMS_TO_SEND_UNTIL_CONFIRMATION = 50; // Calculado com time-out no ADI de 5 segundos

    private final CommunicationCTX communicationCTX;

    public SendFormsState(final KitGeneralCTX context, final CommunicationCTX communicationCTX) {
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
        final int totalUpdateSteps = communicationCTX.getFormsToSendOrderedListSize();
        LOGGER.info("Channel Progress totalUpdateSteps=" + totalUpdateSteps);
        final ChannelProgressRSTY channelProgress = new ChannelProgressRSTY(totalUpdateSteps);
        context.getClientAdapterOut().sendBack(channelProgress);

        /*
         * Getting the first *bunch* of forms and sending
         */
        sendFormsAndRequestClientStatus();

        return new ResultWaitEvent<KitEventSME>();

        // DELETE FORMS?
        // public static FormOperation createServerRequestChanges() {
        // return createFormOperation(FormOperation.UPDATED_FORMS);
        // }

    }

    @Override
    public ProcessingResult<KitEventSME> processEvent(final KitEventSME event) {

        if (event instanceof FormOperationClientSuccessEventSME) {

            if( communicationCTX.getFormsSentWaitingForConfirmationList().size() == 0 ) {
                LOGGER.warn("Investigate. getFormsSentWaitingForConfirmationList().size()=0");
                return finishedSendingFormsAndRequestUpdates();
            }

            final String ktClientId = context.getClientInfo().getKtClientId();
            final boolean serviceSuccess = FormServices.flagFormsAsReceived(ktClientId, communicationCTX.getFormsSentWaitingForConfirmationList());
            if (serviceSuccess == false) {
                final StateSME<KitEventSME> errorState = UnrecoverableErrorState.getInstance(context, ConversationFinishedStatusCTX.FINISHED_GENERAL_ERROR);
                return new ResultStateTransition<KitEventSME>(errorState);
            }
            communicationCTX.clearSentListWaitingForConfirmation();
            sendFormsAndRequestClientStatus();
            return new ResultWaitEvent<KitEventSME>();
        }
        else if (event instanceof FormOperationUpdateFormsCompleteEventSME) {
            return finishedSendingFormsAndRequestUpdates();
        }
        else {

            /*
             * ERROR
             */
            if (communicationCTX.getFormsToSendOrderedListSize() > 0) {
                LOGGER.error("Something went wrong. We still had forms to send. communicationCTX.getFormsToSendOrderedListSize()="
                        + communicationCTX.getFormsToSendOrderedListSize());
            }
            LOGGER.error("Invalid event. event=" + event);
            final StateSME<KitEventSME> newState = UnrecoverableErrorState.getInstance(context, ConversationFinishedStatusCTX.FINISHED_ERROR_UNEXPECTED_EVENT);

            return new ResultStateTransition<KitEventSME>(newState);

        }
    }

    private ProcessingResult<KitEventSME> finishedSendingFormsAndRequestUpdates() {
        LOGGER.warn("communicationCTX.getFormsToSendOrderedListSize()=" + communicationCTX.getFormsToSendOrderedListSize());
        LOGGER.info("Client Status is ok. All Forms successfully sent.");
        final RetrieveUpdatedFormsState retrieveUpdatedFormsState = new RetrieveUpdatedFormsState(context);
        return new ResultStateTransition<KitEventSME>(retrieveUpdatedFormsState);
    }

    private void sendFormsAndRequestClientStatus() {

        final List<FormSTY> formsToSend = communicationCTX.extractFormsToSendInOrder(MAX_FORMS_TO_SEND_UNTIL_CONFIRMATION);

        LOGGER.info("Client Status is ok. Sending forms. formsToSend.size()=" + formsToSend + ", formsToSendOrderedListSize()="
                + communicationCTX.getFormsToSendOrderedListSize());

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

        final FormOperationGetStatusRSTY formOperationGetStatusRSTY = new FormOperationGetStatusRSTY();
        context.getClientAdapterOut().sendBack(formOperationGetStatusRSTY);

    }

}// class
