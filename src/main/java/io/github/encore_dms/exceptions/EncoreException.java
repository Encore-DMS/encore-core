package io.github.encore_dms.exceptions;

public class EncoreException extends RuntimeException {

    public EncoreException(String s, Throwable t) {
        super(s, t);
    }

    public EncoreException(String s) {
        super(s);
    }

}
