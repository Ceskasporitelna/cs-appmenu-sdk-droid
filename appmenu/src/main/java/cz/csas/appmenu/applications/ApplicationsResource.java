package cz.csas.appmenu.applications;

import cz.csas.cscore.webapi.Resource;
import cz.csas.cscore.webapi.WebApiClient;
import cz.csas.cscore.webapi.apiquery.HasInstanceResource;

/**
 * The type Applications resource. This resource provides access to {@link ApplicationResource}
 * with given identifier.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 13.05.16.
 */
public class ApplicationsResource extends Resource implements HasInstanceResource<ApplicationResource> {
    /**
     * Instantiates a new Resource.
     *
     * @param basePath the base path
     * @param client   the client
     */
    public ApplicationsResource(String basePath, WebApiClient client) {
        super(basePath, client);
    }

    /**
     * Get new Application Resource based on given identifier
     * @param id the identifier of application
     * @return the application resource
     */
    @Override
    public ApplicationResource withId(Object id) {
        return new ApplicationResource(id, getBasePath(), getClient());
    }
}
