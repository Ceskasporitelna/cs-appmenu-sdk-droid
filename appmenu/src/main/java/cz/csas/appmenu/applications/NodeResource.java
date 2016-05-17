package cz.csas.appmenu.applications;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.webapi.InstanceResource;
import cz.csas.cscore.webapi.ResourceUtils;
import cz.csas.cscore.webapi.WebApiClient;
import cz.csas.cscore.webapi.apiquery.ListEnabled;

/**
 * The type Node resource. This resource provides list of applications in tree structure.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 29/03/2017.
 */
public class NodeResource extends InstanceResource implements ListEnabled<ApplicationListResponse> {

    /**
     * Instantiates a new Instance resource.
     *
     * @param id       the id
     * @param basePath the base path
     * @param client   the client
     */
    public NodeResource(Object id, String basePath, WebApiClient client) {
        super(id, basePath, client);
    }

    @Override
    public void list(CallbackWebApi<ApplicationListResponse> callback) {
        ResourceUtils.callList(this, null, new ApplicationsRawDataTransform(), ApplicationListResponse.class, callback);
    }
}
