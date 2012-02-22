package com.kit.lightserver.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public interface SocketWrapper {

    DataOutputStream getDataOutputStream();

    DataInputStream getDataInputStream();

    void closeDataInputStream();

    void closeDataOutputStream();

    boolean dataOutputCanBeClosed();

    boolean isClosed();

}// interface
