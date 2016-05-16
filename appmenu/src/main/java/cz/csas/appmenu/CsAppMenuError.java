package cz.csas.appmenu;

import cz.csas.cscore.error.CsSDKError;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 13.05.16.
 */
public class CsAppMenuError extends CsSDKError {
    /**
     * Instantiates a new Cs places error.
     */
    public CsAppMenuError() {
    }

    /**
     * Instantiates a new Cs places error.
     *
     * @param detailMessage the detail message
     */
    public CsAppMenuError(String detailMessage) {
        super(detailMessage);
    }


    /**
     * Instantiates a new Cs places error.
     *
     * @param detailMessage the detail message
     * @param throwable     the throwable
     */
    public CsAppMenuError(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    /**
     * Instantiates a new Cs places error.
     *
     * @param throwable the throwable
     */
    public CsAppMenuError(Throwable throwable) {
        super(throwable);
    }
}
