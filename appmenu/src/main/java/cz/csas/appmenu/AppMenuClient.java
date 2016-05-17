package cz.csas.appmenu;

import cz.csas.appmenu.applications.ApplicationsResource;
import cz.csas.cscore.client.WebApiConfiguration;
import cz.csas.cscore.webapi.WebApiClient;

/**
 * The type AppMenu client. This Client provides {@link ApplicationsResource}.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 13.05.16.
 */
public class AppMenuClient extends WebApiClient {


    /**
     * Instantiates a new AppMenu client.
     *
     * @param webApiConfiguration the webApiConfiguration as a settings parameter of the client
     */
    public AppMenuClient(WebApiConfiguration webApiConfiguration) {
        super(webApiConfiguration);
    }

    @Override
    protected String getClientPath() {
        return "/api/v2";
    }

    private String getBasePath() {
        return "appmenu";
    }

    /**
     * Get the Applications resource.
     * See also {@link ApplicationsResource}
     *
     * @return the applications resource
     */
    public ApplicationsResource getApplicationsResource() {
        return new ApplicationsResource(getBasePath(), this);
    }

}
