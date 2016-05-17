package cz.csas.appmenu;

/**
 * The enum App information source.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 23 /05/16.
 */
public enum AppInformationSource {

    /**
     * Value indicating that the AppInformation has been obtained from the server recently
     */
    SERVER,
    /**
     * Value indicating that the AppInformation has been obtained from the cache
     */
    CACHE
}
