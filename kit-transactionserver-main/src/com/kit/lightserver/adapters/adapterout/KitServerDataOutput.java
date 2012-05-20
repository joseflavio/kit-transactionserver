package com.kit.lightserver.adapters.adapterout;

import java.io.DataOutput;
import java.io.Flushable;
import java.io.IOException;

import com.kit.lightserver.adapters.adapterin.DataCounter;

final class  KitServerDataOutput <T extends Flushable & DataOutput>  implements Flushable, DataOutput {

    private final T dataOutputStream;

    private int totalBytesSent = 0;

    private int flushCount = 0;

    public KitServerDataOutput(final T source) {
        this.dataOutputStream = source;
    }

    public int getTotalBytesSent() {
        return totalBytesSent;
    }

    public int getFlushCount() {
        return totalBytesSent;
    }

    @Override
    public void flush() throws IOException {
        flushCount++;
        totalBytesSent+=56;
        dataOutputStream.flush();
    }

    @Override
    public void writeUTF(final String str) throws IOException {
        totalBytesSent += DataCounter.countWriteUTFForDataOutputStream(str);
        dataOutputStream.writeUTF(str);
    }

    @Override
    public void writeByte(final int v) throws IOException {
        totalBytesSent+=1;
        dataOutputStream.writeByte(v);
    }

    @Override
    public void writeLong(final long v) throws IOException {
        totalBytesSent+=8;
        dataOutputStream.writeLong(v);
    }

    @Override
    public void writeShort(final int v) throws IOException {
        totalBytesSent+=2;
        dataOutputStream.writeShort(v);
    }

    @Override
    public void writeInt(final int v) throws IOException {
        totalBytesSent+=4;
        dataOutputStream.writeInt(v);
    }

    @Override
    public void write(final int b) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(final byte[] b) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeBoolean(final boolean v) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeChar(final int v) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeFloat(final float v) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeDouble(final double v) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeBytes(final String s) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeChars(final String s) throws IOException {
        throw new UnsupportedOperationException();
    }

}// class
