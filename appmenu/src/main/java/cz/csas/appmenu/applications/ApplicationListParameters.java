package cz.csas.appmenu.applications;

import cz.csas.cscore.webapi.PaginatedParameters;
import cz.csas.cscore.webapi.Pagination;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 14.05.16.
 */
public class ApplicationListParameters extends PaginatedParameters {
    /**
     * Instantiates a new Paginated parameters.
     *
     * @param pagination the pagination
     */
    public ApplicationListParameters(Pagination pagination) {
        super(pagination);
    }
}
