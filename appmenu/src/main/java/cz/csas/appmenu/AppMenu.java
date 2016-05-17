package cz.csas.appmenu;

import android.content.Context;

import cz.csas.cscore.client.WebApiConfiguration;

/**
 * The type AppMenu. This object is the core object of AppMenuSDK.
 * It provides AppMenu initialization and AppMenuClient.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 13.05.16.
 */
public abstract class AppMenu {
    private static AppMenu sharedInstance;

    /**
     * Get instance of AppMenuSDK main object.
     *
     * @return the main appMenu object
     */
    public static AppMenu getInstance() {
        if (sharedInstance == null)
            sharedInstance = new AppMenuImpl();
        return sharedInstance;
    }

    /**
     * AppMenu client initialization
     *
     * @param webApiConfiguration the configuration of Webapi
     * @return the AppMenu
     */
    public abstract AppMenu init(WebApiConfiguration webApiConfiguration);

    /**
     * Get AppMenu client
     *
     * @return the AppMenu client
     */
    public abstract AppMenuClient getAppMenuClient();

    /**
     * Configured AppManager that is able to obtain AppInformation and perform version checks
     *
     * @return the app menu manager
     */
    public abstract AppMenuManager getAppMenuManager();

    /**
     * Configures the AppMenuSDK. This has to be called before using this SDK
     *
     * @param appId       Application id string that is used in the URL for fetching the relevant
     *                    app information.
     * @param categoryKey If categoryKey is set, the SDK will try to find an AppItem with
     *                    CATEGORY_KEY corresponding to this value and treat it as information
     *                    about this particular application. This value is mandatory, if you want to
     *                    run a version check.
     * @return the configured AppMenu
     */
    public abstract AppMenu useAppMenu(String appId, String categoryKey, Context context);
}
