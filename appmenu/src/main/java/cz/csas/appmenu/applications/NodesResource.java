package cz.csas.appmenu.applications;

import cz.csas.cscore.webapi.Resource;
import cz.csas.cscore.webapi.WebApiClient;
import cz.csas.cscore.webapi.apiquery.HasInstanceResource;

/**
 * The type Nodes resource. This resource provides access to {@link NodeResource} with given
 * identifier.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 29/03/2017.
 */
public class NodesResource extends Resource implements HasInstanceResource<NodeResource> {

    /**
     * Instantiates a new Resource.
     *
     * @param basePath the base path
     * @param client   the client
     */
    public NodesResource(String basePath, WebApiClient client) {
        super(basePath, client);
    }

    /**
     * Get new Node Resource based on given identifier
     *
     * @param id the identifier of node
     * @return the node resource
     */
    @Override
    public NodeResource withId(Object id) {
        return new NodeResource(id, getBasePath(), getClient());
    }
}
