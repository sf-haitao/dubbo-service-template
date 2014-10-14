package com.sfebiz.demo.client;

public interface ErrorCallback {
    void callback(int apiCode, int localCode, Object data);
}
