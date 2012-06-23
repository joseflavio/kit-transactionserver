package com.kit.lightserver.statemachine.states;

import kit.primitives.channel.ChannelProgress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dajo.chronometer.Chronometer;

import com.fap.framework.statemachine.ProcessingResult;
import com.fap.framework.statemachine.ResultStateTransition;
import com.fap.framework.statemachine.ResultWaitEvent;
import com.fap.framework.statemachine.StateSME;

import com.kit.lightserver.domain.types.AuthenticationRequestTypeEnumSTY;
import com.kit.lightserver.domain.types.ConnectionInfoVO;
import com.kit.lightserver.domain.types.InstallationIdAbVO;
import com.kit.lightserver.services.be.authentication.AuthenticationService;
import com.kit.lightserver.services.be.authentication.AuthenticationServiceResponse;
import com.kit.lightserver.statemachine.StateMachineMainContext;
import com.kit.lightserver.statemachine.events.AuthenticationRequestSME;
import com.kit.lightserver.statemachine.types.ClientInfoCTX;
import com.kit.lightserver.statemachine.types.ConversationFinishedStatusCTX;
import com.kit.lightserver.types.response.AuthenticationResponseRSTY;
import com.kit.lightserver.types.response.ChannelNotificationEndConversationRSTY;

public final class InitialState extends BaseState implements StateSME<KitEventSME> {

    static private final Logger LOGGER = LoggerFactory.getLogger(InitialState.class);

    public InitialState(final StateMachineMainContext context) {
        super(context);
    }// constructor

    @Override
    public ProcessingResult<KitEventSME> transitionOccurred() {
        return new ResultWaitEvent<KitEventSME>();
    }

    @Override
    public ProcessingResult<KitEventSME> processEvent(final KitEventSME event) {
        final StateSME<KitEventSME> newState = processEventAndGetNewStateToTransition(event);
        final ResultStateTransition<KitEventSME> transition = new ResultStateTransition<KitEventSME>(newState);
        return transition;
    }

    private StateSME<KitEventSME> processEventAndGetNewStateToTransition(final KitEventSME event) {

        /*
         * Sanity
         */
        if ( (event instanceof AuthenticationRequestSME) == false ) {
            LOGGER.error("Unexpected Event. event={}", event);
            return UnrecoverableErrorState.getInstance(context, ConversationFinishedStatusCTX.FINISHED_ERROR_UNEXPECTED_EVENT);
        }

        /*
         * Real processing
         */
        AuthenticationRequestSME authenticationRequestSME = (AuthenticationRequestSME) event;

        String clientUserId = authenticationRequestSME.getClientUserId();
        String password = authenticationRequestSME.getPassword();
        InstallationIdAbVO installId = authenticationRequestSME.getInstallationIdSTY();
        ConnectionInfoVO connInfo = context.getConnectionInfo();
        AuthenticationRequestTypeEnumSTY authRequestType =  authenticationRequestSME.getAuthenticationRequestType();

        /*
         * Invoking the service
         */
        Chronometer c = new Chronometer("authenticationService.authenticate");
        c.start();
        AuthenticationServiceResponse authServResponse;
        try {
            AuthenticationService authenticationService = AuthenticationService.getInstance(context.getConfigAccessor());
            authServResponse = authenticationService.authenticate(connInfo, clientUserId, password, installId, authRequestType);
        } catch (Throwable t) {
            LOGGER.error("Unexpected error.", t);
            authServResponse = AuthenticationServiceResponse.FAILED_UNEXPECTED_ERROR;
        }
        c.stop();
        LOGGER.info(c.toString());

        /*
         * Handling the service response
         */
        final StateSME<KitEventSME> newState;
        final ClientInfoCTX clientInfo;
        if ( authServResponse == AuthenticationServiceResponse.SUCCESS_MUST_RESET ||
             authServResponse == AuthenticationServiceResponse.SUCCESS_NO_NEED_TO_RESET ) {

            LOGGER.info("Authentication successful. authServResponse=" + authServResponse);

            final boolean mustReset;
            if( AuthenticationServiceResponse.SUCCESS_MUST_RESET == authServResponse ) {
                LOGGER.info("MUST RESET. clientUserId="+clientUserId);
                mustReset = true;
            }
            else {
                LOGGER.info("NO NEED TO RESET. clientUserId="+clientUserId);
                mustReset = false;
            }

            clientInfo = new ClientInfoCTX(clientUserId, authServResponse, true, mustReset);

            newState = new ClientAuthenticationSuccessfulState(context);

            final AuthenticationResponseRSTY success = new AuthenticationResponseRSTY(authServResponse);
            context.getClientAdapterOut().sendBack(success);

        }
        else {

            LOGGER.warn("Authentication failed. authServResponse=" + authServResponse);

            /*
             * In case of Authenticate error, we just need to send the auth response with error and the client should send
             * back the Channel Notification End Channel (We don't need to request it)? depend on the case in case of protocol error yes, in database error no
             */
            final AuthenticationResponseRSTY clientAuthResponse =  new AuthenticationResponseRSTY(authServResponse);
            final ChannelNotificationEndConversationRSTY clientEndConversation = new ChannelNotificationEndConversationRSTY();
            context.getClientAdapterOut().sendBack(clientAuthResponse, clientEndConversation);

            clientInfo = new ClientInfoCTX(clientUserId, authServResponse, false, false);
            newState = WaitForEventEndConversationState.getInstance(context);

        }// if-else

        context.setClientInfo(clientInfo);

        return newState;

    }

    public static ChannelProgress createChannelProgress(final short numberOfSteps, final short previewSteps) {
        final short totalSteps = (short) (numberOfSteps + previewSteps);
        final ChannelProgress channelProgress = new ChannelProgress();
        channelProgress.numberOfSteps = totalSteps;
        return channelProgress;
    }

}// class
