package kit.primitives.base;

import java.io.IOException;

public interface KitDataInputReader {

    String readUTF() throws IOException;

    byte readByte() throws IOException;

    long readLong() throws IOException;

    short readShort() throws IOException;

    int readInt() throws IOException;

}
