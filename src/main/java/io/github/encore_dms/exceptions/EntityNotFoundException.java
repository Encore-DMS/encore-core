package io.github.encore_dms.exceptions;

public class EntityNotFoundException extends EncoreException {

    public EntityNotFoundException(String s, Throwable t) {
        super(s, t);
    }

    public EntityNotFoundException(Throwable t) {
        super(t);
    }

}
