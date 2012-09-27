package com.kit.lightserver.adapters.adapterout;

import java.util.LinkedList;
import java.util.List;

import kit.primitives.base.Primitive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kit.lightserver.network.SocketWrapper;
import com.kit.lightserver.statemachine.KITStateMachineRunnable.EventQueue;
import com.kit.lightserver.statemachine.events.AdapterOutUnexpectedErrorSME;
import com.kit.lightserver.types.response.ClientResponseRSTY;

public final class ClientAdapterOut {

    static private final Logger LOGGER = LoggerFactory.getLogger(ClientAdapterOut.class);

    private final ClientAdapterOutSender sender;

    private final EventQueue eventQueue;

    public ClientAdapterOut(final SocketWrapper socket, final EventQueue eventQueue) {

        this.sender = new ClientAdapterOutSender(socket);
        this.eventQueue = eventQueue;

    }// constructor

    public void sendBack(final List<ClientResponseRSTY> clientResponses) {
        final AdoPrimitiveListEnvelope primitivesEnvelope = new AdoPrimitiveListEnvelope(clientResponses);
        this.sendBack(primitivesEnvelope);
    }

    public void sendBack(final ClientResponseRSTY... responses) {
        final AdoPrimitiveListEnvelope primitivesEnvelope = new AdoPrimitiveListEnvelope(responses);
        this.sendBack(primitivesEnvelope);
    }

    public final void closeDataOutput() {
        LOGGER.info("Closing data output stream to mobile.");
        sender.closeOutput();
    }

    private void sendBack(final AdoResponseEnvelope adoResponseEnvelope) {

        if( sender.isValidToSend() == false ) {
            LOGGER.error("The clientAdapterOut can not send. adoResponseEnvelope="+adoResponseEnvelope);
            return;
        }

        if( adoResponseEnvelope instanceof AdoPrimitiveListEnvelope ) {

            AdoPrimitiveListEnvelope primitiveListEnvelope = (AdoPrimitiveListEnvelope)adoResponseEnvelope;

            List<ClientResponseRSTY> responseList = primitiveListEnvelope.getAdoClientResponseList();

            List<Primitive> primitiveList = new LinkedList<Primitive>();
            boolean successConvertingAll = true;
            for(int i=0; i<responseList.size(); ++i) {

                ClientResponseRSTY currentResponse = responseList.get(i);
                AdoConverterResult<Primitive> converterResult = AdoPrimiveConverter.convert(currentResponse);

                primitiveList.add(converterResult.getPrimitiveToSend()); // TODO: Improve this, the error should not be inside the AdoPrimiveConverter

                if( converterResult.isSuccess() == false ) {
                    successConvertingAll = false;
                    break;
                }

            }

            boolean sucessSending = sender.sendToTheClientSocket(primitiveList); // TODO: Even with error converting, we need to send the error to the client? maybe

            if( successConvertingAll == false || sucessSending == false ) {
                LOGGER.error("The ADO output will be closed because an error occurred.");
                unexpectedErrorOccurred();

            }

        } else {
            LOGGER.error("Unknow kind of AdoEnvelope. adoResponseEnvelope="+adoResponseEnvelope);
            unexpectedErrorOccurred();
        }

    }

    private void unexpectedErrorOccurred() {
        sender.closeOutput();
        eventQueue.enqueueReceived( new AdapterOutUnexpectedErrorSME() );
    }

}// class
