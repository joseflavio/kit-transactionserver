package kit.primitives.base;

import java.io.IOException;

public interface KitDataOutputWriter {

    void writeUTF(final String str) throws IOException;

    void writeByte(byte v) throws IOException;

    void writeLong(long v) throws IOException;

    void writeShort(short v) throws IOException;

    void writeInt(int v) throws IOException;

    void flush() throws IOException;

}
