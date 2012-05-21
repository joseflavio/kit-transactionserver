package com.kit.lightserver.statemachine.states;

import kit.primitives.channel.ChannelProgress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fap.framework.uniqueids.LastConnectionTokenGenerator;
import com.jfap.framework.statemachine.ProcessingResult;
import com.jfap.framework.statemachine.ResultStateTransition;
import com.jfap.framework.statemachine.ResultWaitEvent;
import com.jfap.framework.statemachine.StateSME;
import com.kit.lightserver.adapters.adapterout.AdoPrimitiveListEnvelope;
import com.kit.lightserver.domain.AuthenticationRequestTypeEnumSTY;
import com.kit.lightserver.domain.types.InstallationIdSTY;
import com.kit.lightserver.services.be.authentication.AuthenticationService;
import com.kit.lightserver.services.be.authentication.AuthenticationServiceResponse;
import com.kit.lightserver.statemachine.StateMachineMainContext;
import com.kit.lightserver.statemachine.events.AuthenticationRequestSME;
import com.kit.lightserver.statemachine.types.ClientInfoCTX;
import com.kit.lightserver.statemachine.types.ConversationFinishedStatusCTX;
import com.kit.lightserver.types.response.AuthenticationResponseFailedWrongPassowordRSTY;
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
            LOGGER.error("Unexpected Event. event="+event);
            return UnrecoverableErrorState.getInstance(context, ConversationFinishedStatusCTX.FINISHED_ERROR_UNEXPECTED_EVENT);
        }

        /*
         * Real processing
         */
        AuthenticationRequestSME authenticationRequestSME = (AuthenticationRequestSME) event;

        String userClientId = authenticationRequestSME.getUserClientId();
        String password = authenticationRequestSME.getPassword();
        InstallationIdSTY installationId = authenticationRequestSME.getInstallationIdSTY();

        AuthenticationService authenticationService = AuthenticationService.getInstance(context.getConfigAccessor());

        long lastConnectionToken = LastConnectionTokenGenerator.generateRandomConnectionId();
        final AuthenticationServiceResponse authenticationResponse = authenticationService.authenticate(context.getConnectionInfo(), userClientId, password,
                installationId, lastConnectionToken);

        LOGGER.info("Authentication complete. authenticationResponse=" + authenticationResponse);

        final StateSME<KitEventSME> newState;
        final ClientInfoCTX clientInfo;
        if ( authenticationResponse.equals(AuthenticationServiceResponse.SUCCESS_MUST_RESET) ||
             authenticationResponse.equals(AuthenticationServiceResponse.SUCCESS_NO_NEED_TO_RESET) ) {


            final AuthenticationRequestTypeEnumSTY authRequestType =  authenticationRequestSME.getAuthenticationRequestType();
            final boolean mustReset;
            if( AuthenticationRequestTypeEnumSTY.RES_MANUAL == authRequestType || AuthenticationServiceResponse.SUCCESS_MUST_RESET == authenticationResponse ) {
                LOGGER.info("MUST RESET. userClientId="+userClientId);
                mustReset = true;
            }
            else {
                LOGGER.info("NO NEED TO RESET. userClientId="+userClientId);
                mustReset = false;
            }

            clientInfo = new ClientInfoCTX(userClientId, authenticationResponse, true, mustReset);

            newState = new ClientAuthenticationSuccessfulState(context);

        }
        else {

            clientInfo = new ClientInfoCTX(userClientId, authenticationResponse, false, false);
            newState = processAuthenticationError(authenticationResponse);

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

    private StateSME<KitEventSME> processAuthenticationError(final AuthenticationServiceResponse authenticationResponse) {

        /*
         * In case of Authenticate error, we just need to send the auth response with error and the client should send
         * back the Channel Notification End Channel (We don't need to request it)? depend on the case in case of protocol error yes, in database error no
         */
        final AuthenticationResponseFailedWrongPassowordRSTY response;
        if ( authenticationResponse == AuthenticationServiceResponse.FAILED_INVALID_PASSWORD ) {
            response = new AuthenticationResponseFailedWrongPassowordRSTY(AuthenticationResponseFailedWrongPassowordRSTY.Type.FAILED_INCORRECT_PASSWORD);
        }
        else if ( authenticationResponse == AuthenticationServiceResponse.FAILED_CLIENTID_DO_NOT_EXIST ) {
            response = new AuthenticationResponseFailedWrongPassowordRSTY(AuthenticationResponseFailedWrongPassowordRSTY.Type.FAILED_INEXISTENT_CLIENTID);
        }
        else if ( authenticationResponse == AuthenticationServiceResponse.FAILED_DATABASE_ERROR ) {
            response = new AuthenticationResponseFailedWrongPassowordRSTY(AuthenticationResponseFailedWrongPassowordRSTY.Type.FAILED_DATABASE_ERROR);
        }
        else if ( authenticationResponse == AuthenticationServiceResponse.FAILED_UNEXPECTED_ERROR ) {
            response = new AuthenticationResponseFailedWrongPassowordRSTY(AuthenticationResponseFailedWrongPassowordRSTY.Type.FAILED_UNEXPECTED_ERROR);
        }
        else {
            // This should *NEVER* happen
            LOGGER.error("UNEXPECTED.  authenticationResponse=" + authenticationResponse);
            response = new AuthenticationResponseFailedWrongPassowordRSTY(AuthenticationResponseFailedWrongPassowordRSTY.Type.FAILED_UNEXPECTED_ERROR);
        }

        ChannelNotificationEndConversationRSTY endConversationRSTY = new ChannelNotificationEndConversationRSTY();
        AdoPrimitiveListEnvelope primitivesEnvelope = new AdoPrimitiveListEnvelope(response, endConversationRSTY);

        context.getClientAdapterOut().sendBack(primitivesEnvelope);

        final StateSME<KitEventSME> newState = WaitForEventEndConversationState.getInstance(context);
        return newState;

    }

}// class
