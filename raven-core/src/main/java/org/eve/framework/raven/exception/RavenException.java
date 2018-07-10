package org.eve.framework.raven.exception;

/**
 * @author yc_xia
 * @date 2018/6/19
 */
public class RavenException extends Exception {
    public RavenException() {
    }

    public RavenException(String message) {
        super(message);
    }

    public RavenException(String message, Throwable cause) {
        super(message, cause);
    }

    public RavenException(Throwable cause) {
        super(cause);
    }

    public RavenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
