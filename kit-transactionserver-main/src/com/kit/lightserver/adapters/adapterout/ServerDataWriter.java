package com.kit.lightserver.adapters.adapterout;

import java.io.DataOutputStream;
import java.io.IOException;

import kit.primitives.base.KitDataOutputWriter;

import com.kit.lightserver.adapters.adapterin.DataCounter;

final class ServerDataWriter implements KitDataOutputWriter {

    private final DataOutputStream dataOutputStream;

    private int totalBytesSent = 0;

    private int flushCount = 0;

    public ServerDataWriter(final DataOutputStream dataOutputStream) {
        this.dataOutputStream = dataOutputStream;
    }

    @Override
    public void writeUTF(final String str) throws IOException {
        totalBytesSent += DataCounter.countWriteUTFForDataOutputStream(str);
        dataOutputStream.writeUTF(str);
    }

    @Override
    public void writeByte(final byte v) throws IOException {
        totalBytesSent+=1;
        dataOutputStream.writeByte(v);
    }

    @Override
    public void writeLong(final long v) throws IOException {
        totalBytesSent+=8;
        dataOutputStream.writeLong(v);
    }

    @Override
    public void writeShort(final short v) throws IOException {
        totalBytesSent+=2;
        dataOutputStream.writeShort(v);
    }

    @Override
    public void writeInt(final int v) throws IOException {
        totalBytesSent+=4;
        dataOutputStream.writeInt(v);
    }

    @Override
    public void flush() throws IOException {
        flushCount++;
        totalBytesSent+=56;
        dataOutputStream.flush();
    }

    public String getTotalBytesSent() {
        return "totalBytesSent=" + totalBytesSent + ", flushCount="+flushCount;
    }

}// class
