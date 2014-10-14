package com.sfebiz.demo.client;

import java.io.IOException;
import java.io.InputStream;

/**
 * 用于protobuf从InputStream中读取多个实体
 * 
 * @author rendong
 */
public class LimitedInputStream extends InputStream {
    private InputStream input;
    private int         length;

    public LimitedInputStream(InputStream input, int limit) {
        this.input = input;
        this.length = limit;
    }

    @Override
    public int read() throws IOException {
        if (length == 0) return -1;
        int b = input.read();
        if (b != -1) {
            length--;
        }
        return b;
    }

    @Override
    public int read(byte[] b) throws IOException {
        if (length == 0 || b == null) return -1;
        int size = input.read(b, 0, length > b.length ? b.length : length);
        if (size != -1) {
            length -= size;
        } else {
            length = 0;
        }
        return size;
    }

    @Override
    public int read(byte[] b, int offset, int length) throws IOException {
        if (this.length == 0 || b == null) return -1;
        int size = input.read(b, offset, this.length > length ? length : this.length);
        if (size != -1) {
            this.length -= size;
        } else {
            this.length = 0;
        }
        return size;
    }
}
