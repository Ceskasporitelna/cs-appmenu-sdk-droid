package cz.csas.appmenu;

import cz.csas.cscore.error.CsSDKError;

/**
 * The type Cs appmenu error.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 01 /01/16.
 */
public class CsAppMenuError extends CsSDKError {


    /**
     * The enum Kind.
     */
    public enum Kind {

        /**
         * The bad initialization.
         */
        BAD_INITIALIZATION("Bad appmenu initialization. WebApiKey or Environment is missing."),

        /**
         * The bad manager initialization.
         */
        BAD_MANAGER_INITIALIZATION("You have to call useAppMenu method first."),

        /**
         * Other kind.
         */
        OTHER(null);

        private String detailedMessage;

        Kind(String detailedMessage) {
            this.detailedMessage = detailedMessage;
        }

    }

    private final Kind kind;

    /**
     * Instantiates a new Cs appmenu error.
     *
     * @param kind the kind
     */
    public CsAppMenuError(Kind kind) {
        super(kind.detailedMessage);
        this.kind = kind;
    }

    /**
     * Instantiates a new Cs appmenu error.
     *
     * @param kind      the kind
     * @param throwable the throwable
     */
    public CsAppMenuError(Kind kind, Throwable throwable) {
        super(throwable);
        this.kind = kind;
    }

    /**
     * Instantiates a new Cs appmenu error.
     *
     * @param kind          the kind
     * @param detailMessage the detail message
     */
    public CsAppMenuError(Kind kind, String detailMessage) {
        super(detailMessage);
        this.kind = kind;
    }

    /**
     * Gets kind.
     *
     * @return the kind
     */
    public Kind getKind() {
        return kind;
    }
}
