package cz.csas.appmenu;

import cz.csas.cscore.client.WebApiConfiguration;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 13.05.16.
 */
public abstract class AppMenu {
    private static AppMenu sharedInstance;

    /**
     * Get instance of PlacesSDK main object.
     *
     * @return the main places object
     */
    public static AppMenu getInstance() {
        if (sharedInstance == null)
            sharedInstance = new AppMenuImpl();
        return sharedInstance;
    }

    /**
     * Init places sdk.
     *
     * @param webApiConfiguration the web api configuration to set up the PlacesSDK
     * @return the places
     */
    public abstract AppMenu init(WebApiConfiguration webApiConfiguration);

    /**
     * Get places client.
     * See also {@link AppMenuClient}
     *
     * @return the places client which is used to provide all the resources of PlacesSDK.
     */
    public abstract AppMenuClient getAppMenuClient();
}
