package com.sfebiz.demo.client;

public class LocalException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public static final int NOT_INIT        = Integer.MIN_VALUE;
    public static final int UNKNOWN         = -1;
    public static final int TOKEN_MISSING   = 1000;
    public static final int CERT_BROKEN     = 1010;
    public static final int SERIALIZE_ERROR = 1020;
    public static final int SOCKET_TIMEOUT  = 1030;

    private int    code      = 0;
    private String errorData = null;

    public LocalException(int code) {
        this.code = code;
    }

    public LocalException(String msg, int code) {
        super(msg);
        this.code = code;
    }

    public LocalException(Exception e, String msg, int code) {
        super(msg, e);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getErrorData() {
        return errorData;
    }

    public void setErrorData(String errorData) {
        this.errorData = errorData;
    }
}
