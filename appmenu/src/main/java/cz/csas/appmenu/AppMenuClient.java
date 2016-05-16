package cz.csas.appmenu;

import cz.csas.appmenu.applications.ApplicationsResource;
import cz.csas.cscore.client.WebApiConfiguration;
import cz.csas.cscore.webapi.WebApiClient;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 13.05.16.
 */
public class AppMenuClient extends WebApiClient {


    /**
     * Instantiates a new places client.
     *
     * @param webApiConfiguration the webApiConfiguration as a settings parameter of the client
     */
    public AppMenuClient(WebApiConfiguration webApiConfiguration) {
        super(webApiConfiguration);
    }

    @Override
    protected String getClientPath() {
        return "/webapi/api/v2";
    }

    private String getBasePath() {
        return "appmenu";
    }

    /**
     * Get the places resource.
     * See also {@link ApplicationsResource}
     *
     * @return the places resource
     */
    public ApplicationsResource getAppMenuResource() {
        return new ApplicationsResource(getBasePath(), this);
    }

}
