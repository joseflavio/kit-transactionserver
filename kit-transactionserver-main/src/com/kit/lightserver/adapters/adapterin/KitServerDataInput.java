package com.kit.lightserver.adapters.adapterin;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;

final class KitServerDataInput implements DataInput {

    private final DataInput dataInputStream;

    private int totalBytes;

    public KitServerDataInput(final DataInputStream dataInputStream) {
        this.dataInputStream = dataInputStream;
        this.totalBytes = 0;
    }

    public int getTotalBytes() {
        return totalBytes;
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

    @Override
    public void readFully(final byte[] b) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void readFully(final byte[] b, final int off, final int len) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int skipBytes(final int n) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean readBoolean() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int readUnsignedByte() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int readUnsignedShort() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public char readChar() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public float readFloat() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public double readDouble() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String readLine() throws IOException {
        throw new UnsupportedOperationException();
    }

}// class
