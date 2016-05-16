package cz.csas.appmenu.applications;

import java.util.List;

import cz.csas.appmenu.Application;
import cz.csas.cscore.utils.csjson.annotations.CsExpose;
import cz.csas.cscore.utils.csjson.annotations.CsSerializedName;
import cz.csas.cscore.webapi.PaginatedListResponse;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 14.05.16.
 */
public class ApplicationListResponse extends PaginatedListResponse<Application, ApplicationListResponse> {

    @CsExpose
    @CsSerializedName(value = "application", alternate = "items")
    List<Application> applications;

    @Override
    protected List<Application> getItems() {
        return applications;
    }

    public List<Application> getApplications() {
        return applications;
    }
}
