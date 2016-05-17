package cz.csas.appmenu.applications;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.webapi.InstanceResource;
import cz.csas.cscore.webapi.ResourceUtils;
import cz.csas.cscore.webapi.WebApiClient;
import cz.csas.cscore.webapi.apiquery.ListEnabled;

/**
 * The type Application resource. This resource provides list of applications in flat structure.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 14.05.16.
 */
public class ApplicationResource extends InstanceResource implements
        ListEnabled<ApplicationListResponse> {
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

    /**
     * Get list of applications.
     *
     * @param callback the callback of type CallbackWebApi<T>
     */
    @Override
    public void list(CallbackWebApi<ApplicationListResponse> callback) {
        ResourceUtils.callList(this, "android", new ApplicationsRawDataTransform(), ApplicationListResponse.class, callback);
    }

    /**
     * Get nodes resource
     *
     * @return the nodes resource
     */
    public NodesResource getNodesResource() {
        return new NodesResource(appendPathWith("android"), getClient());
    }
}

