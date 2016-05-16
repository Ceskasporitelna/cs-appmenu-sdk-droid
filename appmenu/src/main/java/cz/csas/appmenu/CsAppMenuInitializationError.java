package cz.csas.appmenu;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 13.05.16.
 */
public class CsAppMenuInitializationError extends CsAppMenuError {
    /**
     * Instantiates a new Cs AppMenu initialization error.
     */
    public CsAppMenuInitializationError() {
        super("Bad appmenu initialization. WebApiKey or Environment is missing.");
    }
}
