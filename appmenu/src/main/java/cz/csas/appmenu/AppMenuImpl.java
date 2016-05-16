package cz.csas.appmenu;

import cz.csas.cscore.CoreSDK;
import cz.csas.cscore.client.WebApiConfiguration;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 13.05.16.
 */
public class AppMenuImpl extends AppMenu {

    private AppMenuClient appMenuClient;

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


    private AppMenu init() {
        WebApiConfiguration webApiConfiguration = CoreSDK.getInstance().getWebApiConfiguration();
        if (webApiConfiguration != null && webApiConfiguration.getWebApiKey() != null &&
                webApiConfiguration.getEnvironment() != null &&
                webApiConfiguration.getEnvironment().getApiContextBaseUrl() != null) {
            appMenuClient = new AppMenuClient(webApiConfiguration);
        } else
            throw new CsAppMenuInitializationError();
        return this;
    }
}
