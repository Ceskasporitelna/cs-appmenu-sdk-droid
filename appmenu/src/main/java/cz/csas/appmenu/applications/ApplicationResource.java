package cz.csas.appmenu.applications;

import cz.csas.appmenu.Application;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.webapi.InstanceResource;
import cz.csas.cscore.webapi.ResourceUtils;
import cz.csas.cscore.webapi.WebApiClient;
import cz.csas.cscore.webapi.apiquery.GetEnabled;
import cz.csas.cscore.webapi.apiquery.PaginatedListEnabled;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 14.05.16.
 */
public class ApplicationResource extends InstanceResource implements
        GetEnabled<cz.csas.appmenu.Application>,
        PaginatedListEnabled<ApplicationListParameters, ApplicationListResponse> {
    /**
     * Instantiates a new Instance resource.
     *
     * @param id       the id
     * @param basePath the base path
     * @param client   the client
     */
    public ApplicationResource(Object id, String basePath, WebApiClient client) {
        super(id, basePath, client);
    }

    @Override
    public void get(CallbackWebApi<cz.csas.appmenu.Application> callback) {
        ResourceUtils.callGet(this, null, null, null, Application.class, callback);
    }

    @Override
    public void list(ApplicationListParameters parameters,
                     CallbackWebApi<ApplicationListResponse> callback) {
        ResourceUtils.callPaginatedList(this, parameters,
                null, ApplicationListResponse.class, callback);
    }
}

