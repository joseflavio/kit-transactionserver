package com.kit.lightserver.statemachine.states;

import kit.primitives.channel.ChannelProgress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfap.framework.statemachine.ProcessingResult;
import com.jfap.framework.statemachine.ResultStateTransition;
import com.jfap.framework.statemachine.ResultWaitEvent;
import com.jfap.framework.statemachine.StateSME;
import com.kit.lightserver.domain.AuthenticationRequestTypeEnumSTY;
import com.kit.lightserver.domain.types.InstallationIdSTY;
import com.kit.lightserver.services.be.authentication.AuthenticationService;
import com.kit.lightserver.services.be.authentication.AuthenticationServiceResponse;
import com.kit.lightserver.statemachine.StateMachineMainContext;
import com.kit.lightserver.statemachine.events.AuthenticationRequestSME;
import com.kit.lightserver.statemachine.types.ClientInfoCTX;
import com.kit.lightserver.statemachine.types.ConversationFinishedStatusCTX;
import com.kit.lightserver.types.response.AuthenticationResponseFailedDatabaseErrorRSTY;
import com.kit.lightserver.types.response.AuthenticationResponseFailedUserAlreadyLoggedRSTY;
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
        final AuthenticationRequestSME authenticationRequestSME = (AuthenticationRequestSME) event;

        final String userClientId = authenticationRequestSME.getUserClientId();
        final String password = authenticationRequestSME.getPassword();
        final InstallationIdSTY installationId = authenticationRequestSME.getInstallationIdSTY();


        final AuthenticationServiceResponse authenticationResponse = AuthenticationService.authenticate(context.getConnectionId(), userClientId, password,
                installationId);

        LOGGER.info("Authentication complete. authenticationResponse=" + authenticationResponse);

        final StateSME<KitEventSME> newState;
        final ClientInfoCTX clientInfo;
        if (authenticationResponse.equals(AuthenticationServiceResponse.SUCCESS_MUST_RESET)
                || authenticationResponse.equals(AuthenticationServiceResponse.SUCCESS_NO_NEED_TO_RESET)) {


            final AuthenticationRequestTypeEnumSTY authenticationRequestType =  authenticationRequestSME.getAuthenticationRequestType();
            final boolean mustReset;
            if( AuthenticationRequestTypeEnumSTY.NEWLOGIN.equals(authenticationRequestType) || AuthenticationServiceResponse.SUCCESS_MUST_RESET.equals(authenticationResponse) ) {
                LOGGER.warn("MUST RESET. userClientId="+userClientId);
                mustReset = true;
            }
            else {
                LOGGER.warn("NO NEED TO RESET. userClientId="+userClientId);
                mustReset = false;
            }

            clientInfo = new ClientInfoCTX(userClientId, authenticationResponse, true, mustReset);

            newState = new ClientAuthenticationSuccessfulState(context);

        }
        else {

            clientInfo = new ClientInfoCTX(userClientId, authenticationResponse, false, false);
            newState = authenticationError(authenticationResponse);

        }// if-else

        context.setClientInfo(clientInfo);

        return newState;

        // final boolean requestClientToBeReseted;
        // if (databaseRequestReset || clientNewLogin) {
        // requestClientToBeReseted = true;
        // } else {
        // requestClientToBeReseted = false;
        // }

        // FormOperation resetClient = MobilePrimitiveFactory.createFormOperation_Reset();

        // ChannelProgress progress = MobilePrimitiveFactory.createChannelProgress(currentForms, preSteps);

        // FormContentFull currentForm = getDataResponse(controlBean);

        // AuthenticationResponseFailedRSTY authenticationResponseErrorSTY = new AuthenticationResponseFailedRSTY();
        // kitStateStateThread.enqueueToSend(authenticationResponseErrorSTY);
        // ChannelNotificationEndConversationRSTY end = new ChannelNotificationEndConversationRSTY();
        // kitStateStateThread.enqueueToSend(end);

    }

    public static ChannelProgress createChannelProgress(final short numberOfSteps, final short previewSteps) {
        final short totalSteps = (short) (numberOfSteps + previewSteps);
        final ChannelProgress channelProgress = new ChannelProgress();
        channelProgress.numberOfSteps = totalSteps;
        return channelProgress;
    }

    private StateSME<KitEventSME> authenticationError(final AuthenticationServiceResponse authenticationResponse) {

        final StateSME<KitEventSME> newState;
        if (authenticationResponse.equals(AuthenticationServiceResponse.FAILED_CLIENTID_DO_NOT_EXIST)
                || authenticationResponse.equals(AuthenticationServiceResponse.FAILED_INVALID_PASSWORD)) {

            final AuthenticationResponseFailedWrongPassowordRSTY failed = new AuthenticationResponseFailedWrongPassowordRSTY();
            context.getClientAdapterOut().sendBack(failed);

            final ChannelNotificationEndConversationRSTY endConversation = new ChannelNotificationEndConversationRSTY();
            context.getClientAdapterOut().sendBack(endConversation);

            newState = WaitForEventEndConversationState.getInstance(context);

        }
        else if (authenticationResponse.equals(AuthenticationServiceResponse.FAILED_USER_ALREADY_LOGGEDIN)) {

            final AuthenticationResponseFailedUserAlreadyLoggedRSTY failed = new AuthenticationResponseFailedUserAlreadyLoggedRSTY();
            context.getClientAdapterOut().sendBack(failed);

            final ChannelNotificationEndConversationRSTY endConversation = new ChannelNotificationEndConversationRSTY();
            context.getClientAdapterOut().sendBack(endConversation);

            newState = WaitForEventEndConversationState.getInstance(context);

        }
        else if (authenticationResponse.equals(AuthenticationServiceResponse.ERROR)) {

            final AuthenticationResponseFailedDatabaseErrorRSTY failed = new AuthenticationResponseFailedDatabaseErrorRSTY();
            context.getClientAdapterOut().sendBack(failed);

            final ChannelNotificationEndConversationRSTY endConversation = new ChannelNotificationEndConversationRSTY();
            context.getClientAdapterOut().sendBack(endConversation);

            newState = WaitForEventEndConversationState.getInstance(context);

        }
        else {
            // This should *NEVER* happen
            LOGGER.error("Unexpected service response=" + authenticationResponse);
            newState = UnrecoverableErrorState.getInstance(context, ConversationFinishedStatusCTX.FINISHED_GENERAL_ERROR);
        }

        return newState;

    }

}// class
