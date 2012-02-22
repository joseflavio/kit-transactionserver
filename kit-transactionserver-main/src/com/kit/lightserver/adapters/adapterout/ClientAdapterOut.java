package com.kit.lightserver.adapters.adapterout;

import kit.primitives.base.Primitive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kit.lightserver.network.SocketWrapper;
import com.kit.lightserver.types.response.ClientResponseRSTY;
import com.kit.lightserver.types.response.CloseDataOutputCommandRSTY;

public final class ClientAdapterOut {

    static private final Logger LOGGER = LoggerFactory.getLogger(ClientAdapterOut.class);

    private final ClientAdapterOutSender sender;

    public ClientAdapterOut(final SocketWrapper socket) {

        this.sender = new ClientAdapterOutSender(socket);

    }// constructor

    public void sendBack(final ClientResponseRSTY clientResponse) {


        if( clientResponse instanceof CloseDataOutputCommandRSTY ) {
            LOGGER.info("Closing data output stream to mobile. clientResponse=" + clientResponse);
            sender.closeOutput();
        }
        else {

            final AdoConverterResult<Primitive> converterResult = AdoPrimiveConverter.convert(clientResponse);
            //LOGGER.debug("Processing " + clientResponse + " to " + converterResult);

            if( !converterResult.isSuccess() ) {

                /*
                 * If we can't convert, the conversation has to finish to avoid make the client inconsistent
                 */
                sender.closeOutput();

            }// if

            /*
             * Even with error converting, we need to send the error to the client
             */
            final Primitive clientPrimitive = converterResult.getPrimitiveToSend();
            sender.sendToTheClientSocket(clientPrimitive);

        }

    }

    public boolean isValidToSend() {
        return sender.isValidToSend();
    }

}// class
