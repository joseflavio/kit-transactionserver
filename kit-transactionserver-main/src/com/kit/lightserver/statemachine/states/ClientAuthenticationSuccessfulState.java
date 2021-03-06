package com.kit.lightserver.statemachine.states;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fap.framework.statemachine.ProcessingResult;
import com.fap.framework.statemachine.ResultStateTransition;
import com.fap.framework.statemachine.StateSME;

import com.kit.lightserver.domain.containers.FormsParaEnviarCTX;
import com.kit.lightserver.domain.containers.SimpleServiceResponse;
import com.kit.lightserver.domain.types.ConhecimentoSTY;
import com.kit.lightserver.domain.types.NotafiscalSTY;
import com.kit.lightserver.services.be.forms.FormServices;
import com.kit.lightserver.statemachine.StateMachineMainContext;
import com.kit.lightserver.statemachine.types.CommunicationCTX;
import com.kit.lightserver.statemachine.types.ConversationFinishedStatusCTX;

final class ClientAuthenticationSuccessfulState extends BaseState implements StateSME<KitEventSME> {

    static private final Logger LOGGER = LoggerFactory.getLogger(ClientAuthenticationSuccessfulState.class);

    public ClientAuthenticationSuccessfulState(final StateMachineMainContext context) {
        super(context);
    }

    @Override
    public ProcessingResult<KitEventSME> transitionOccurred() {

        /*
         * GETTING ALL THE FORMS HERE, SHOULD IT BE HERE???
         */
        final String ktClientUserId = context.getClientInfo().getKtClientUserId();

        final FormServices formServices = FormServices.getInstance(context.getConfigAccessor());

        final SimpleServiceResponse<FormsParaEnviarCTX> serviceResponse;
        if (context.getClientInfo().isMustReset()) {
            serviceResponse = formServices.retrieveCurrentForms(ktClientUserId, false); //TODO: Eu nao queria usar flags aqui, pensar se melhor usar duas funcoes
        }
        else {
            serviceResponse = formServices.retrieveCurrentForms(ktClientUserId, true);
        }

        if (serviceResponse.isValid() == false) {
            LOGGER.error("Unexpected service response={}", serviceResponse);
            final StateSME<KitEventSME> newState = UnrecoverableErrorState.getInstance(context, ConversationFinishedStatusCTX.FINISHED_GENERAL_ERROR);
            final ResultStateTransition<KitEventSME> transition = new ResultStateTransition<KitEventSME>(newState);
            return transition;
        }

        /*
         * First we order the forms (group the Notasfiscais to be sent after its Conhecimento)
         */
        final FormsParaEnviarCTX formsContext = serviceResponse.getValidResult();
        LOGGER.warn("formsContext.getConhecimentoList()= " + formsContext.getConhecimentoList() );
        final CommunicationCTX communicationCTX = new CommunicationCTX();
        for ( final ConhecimentoSTY conhecimentoSTY : formsContext.getConhecimentoList() ) {
            communicationCTX.addToFormsToSendOrderedList(conhecimentoSTY);
            final List<NotafiscalSTY> notasFiscaisDoConhecimento = formsContext.getNotasfiscaisPorConhecimento(conhecimentoSTY);
            if( notasFiscaisDoConhecimento != null ) {
                for (final NotafiscalSTY notafiscalSTY : notasFiscaisDoConhecimento) {
                    communicationCTX.addToFormsToSendOrderedList(notafiscalSTY);
                }
            }
        }

        final SendFormsState sendFormsState = new SendFormsState(context, communicationCTX);
        return new ResultStateTransition<KitEventSME>(sendFormsState);

    }

    @Override
    public ProcessingResult<KitEventSME> processEvent(final KitEventSME event) {
        LOGGER.error("Invalid event. event=" + event);
        final StateSME<KitEventSME> newState = UnrecoverableErrorState.getInstance(context, ConversationFinishedStatusCTX.FINISHED_ERROR_UNEXPECTED_EVENT);
        return new ResultStateTransition<KitEventSME>(newState);
    }

}// class
