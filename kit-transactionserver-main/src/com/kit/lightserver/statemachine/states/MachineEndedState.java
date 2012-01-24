package com.kit.lightserver.statemachine.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfap.framework.statemachine.ProcessingResult;
import com.jfap.framework.statemachine.ResultMachineStopped;
import com.jfap.framework.statemachine.StateSME;
import com.kit.lightserver.services.be.authentication.AuthenticationService;
import com.kit.lightserver.statemachine.StateMachineMainContext;
import com.kit.lightserver.statemachine.types.ClientInfoCTX;
import com.kit.lightserver.statemachine.types.ConversationFinishedStatusCTX;

public final class MachineEndedState implements StateSME<KitEventSME> {

    static private final Logger LOGGER = LoggerFactory.getLogger(MachineEndedState.class);

    private final StateMachineMainContext context;

    private final ConversationFinishedStatusCTX conversationFinishedStatusCTX;

    public MachineEndedState(final StateMachineMainContext context, final ConversationFinishedStatusCTX conversationFinishedStatusCTX) {
        this.context = context;
        this.conversationFinishedStatusCTX = conversationFinishedStatusCTX;
    }// constructor

    @Override
    public ProcessingResult<KitEventSME> transitionOccurred() {

        final ClientInfoCTX clientInfo = context.getClientInfo();

        if (clientInfo != null) {

            if (clientInfo.isAuthenticated()) {

                final boolean clientMustResetInNextConnection;
                if( ConversationFinishedStatusCTX.FINISHED_WITH_SUCCESS.equals(conversationFinishedStatusCTX) ) {
                    clientMustResetInNextConnection = false;
                }
                else {
                    clientMustResetInNextConnection = true; // If any problem occurred the client will be reset in the next time
                }

                final boolean logOffSuccessfull = AuthenticationService.logOff(clientInfo.getKtClientId(), clientMustResetInNextConnection);
                if (logOffSuccessfull == true) {
                    LOGGER.info("Success logging off. clientInfo=" + clientInfo);
                } else {
                    LOGGER.error("Failed logging off. clientInfo=" + clientInfo);
                }

            } else {
                LOGGER.info("No need to log off. clientInfo=" + clientInfo);
            }

        } else {
            LOGGER.error("Can not log off. clientInfo=" + clientInfo);
        }

        return new ResultMachineStopped<KitEventSME>();

    }

    @Override
    public ProcessingResult<KitEventSME> processEvent(final KitEventSME event) {
        LOGGER.error("Machine already ended. Discarding event. event=" + event + ", context=" + context);
        return new ResultMachineStopped<KitEventSME>();
    }

}// class
