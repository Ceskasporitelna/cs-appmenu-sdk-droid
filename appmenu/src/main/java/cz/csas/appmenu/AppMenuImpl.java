package cz.csas.appmenu;

import android.content.Context;

import cz.csas.cscore.CoreSDK;
import cz.csas.cscore.client.WebApiConfiguration;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 13.05.16.
 */
class AppMenuImpl extends AppMenu {

    private AppMenuClient appMenuClient;
    private AppMenuManagerImpl appMenuManager;

    @Override
    public AppMenu init(WebApiConfiguration webApiConfiguration) {
        appMenuClient = new AppMenuClient(webApiConfiguration);
        return this;
    }

    @Override
    public AppMenuClient getAppMenuClient() {
        if (appMenuClient == null)
            init();
        return appMenuClient;
    }

    @Override
    public AppMenuManager getAppMenuManager() {
        if (appMenuManager == null)
            throw new CsAppMenuError(CsAppMenuError.Kind.BAD_MANAGER_INITIALIZATION);
        return appMenuManager;
    }

    @Override
    public AppMenu useAppMenu(String appId, String categoryKey, Context context) {
        appMenuManager = new AppMenuManagerImpl(appId, categoryKey, context);
        return this;
    }

    private AppMenu init() {
        WebApiConfiguration webApiConfiguration = CoreSDK.getInstance().getWebApiConfiguration();
        if (webApiConfiguration != null && webApiConfiguration.getWebApiKey() != null && webApiConfiguration.getEnvironment() != null && webApiConfiguration.getEnvironment().getApiContextBaseUrl() != null)
            appMenuClient = new AppMenuClient(webApiConfiguration);
        else
            throw new CsAppMenuError(CsAppMenuError.Kind.BAD_INITIALIZATION);
        return this;
    }


}
