package org.example.i18n.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class JumpOutException extends RuntimeException {
    private int code;

    public JumpOutException(int code) {
        this.code = code;
    }

    public JumpOutException(String message, int code) {
        super(message);
        this.code = code;
    }

    public JumpOutException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public JumpOutException(Throwable cause, int code) {
        super(cause);
        this.code = code;
    }

    public JumpOutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }
}
