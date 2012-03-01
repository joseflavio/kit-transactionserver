package com.kit.lightserver.adapters.adapterout;

import java.util.LinkedList;
import java.util.List;

import kit.primitives.base.Primitive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kit.lightserver.network.SocketWrapper;
import com.kit.lightserver.types.response.ClientResponseRSTY;

public final class ClientAdapterOut {

    static private final Logger LOGGER = LoggerFactory.getLogger(ClientAdapterOut.class);

    private final ClientAdapterOutSender sender;

    public ClientAdapterOut(final SocketWrapper socket) {

        this.sender = new ClientAdapterOutSender(socket);

    }// constructor

    public void sendBack(final AdoResponseEnvelope adoResponseEnvelope) {

        if( adoResponseEnvelope instanceof CloseDataOutputCommandRSTY ) {
            LOGGER.info("Closing data output stream to mobile. adoResponseEnvelope=" + adoResponseEnvelope);
            sender.closeOutput();
        }
        else if( adoResponseEnvelope instanceof AdoPrimitiveListEnvelope ) {

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

            /*
             * ? Even with error converting, we need to send the error to the client ?
             */
            sender.sendToTheClientSocket(primitiveList);

            if( successConvertingAll == false ) { // If we can't convert, the conversation has to finish to avoid make the client inconsistent
                LOGGER.error("The ADO output will be closed because an error occurred.");
                sender.closeOutput();
            }

        } else {
            LOGGER.error("Unknow kind of AdoEnvelope. adoResponseEnvelope="+adoResponseEnvelope);
        }

    }

    public boolean isValidToSend() {
        return sender.isValidToSend();
    }

}// class
