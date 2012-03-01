package com.kit.lightserver.adapters.adapterin;

import java.io.DataInputStream;
import java.io.IOException;

import kit.primitives.base.KitDataInputReader;

final class ServerDataReader implements KitDataInputReader {

    private final DataInputStream dataInputStream;

    private int totalBytes;

    public ServerDataReader(final DataInputStream dataInputStream) {
        this.dataInputStream = dataInputStream;
        this.totalBytes = 0;
    }

    @Override
    public String readUTF() throws IOException {
        String str = dataInputStream.readUTF();


        totalBytes += DataCounter.countWriteUTFForDataOutputStream(str);
        return str;
    }

    @Override
    public byte readByte() throws IOException {
        byte v = dataInputStream.readByte();
        totalBytes += 1;
        return v;
    }

    @Override
    public long readLong() throws IOException {
        long v = dataInputStream.readLong();
        totalBytes += 8;
        return v;
    }

    @Override
    public short readShort() throws IOException {
        short v = dataInputStream.readShort();
        totalBytes += 2;
        return v;
    }

    @Override
    public int readInt() throws IOException {
        int v = dataInputStream.readInt();
        totalBytes += 4;
        return v;
    }

    public int available() throws IOException {
        int v = dataInputStream.available();
        return v;
    }

    public int getTotalBytes() {
        return totalBytes;
    }

}// class
