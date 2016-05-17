package cz.csas.appmenu.applications;

import java.util.List;

import cz.csas.appmenu.AppItem;
import cz.csas.cscore.utils.csjson.annotations.CsExpose;
import cz.csas.cscore.utils.csjson.annotations.CsSerializedName;
import cz.csas.cscore.webapi.PaginatedListResponse;

/**
 * The type Application list response provides list of {@link AppItem}
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 14.05.16.
 */
public class ApplicationListResponse extends PaginatedListResponse<AppItem, ApplicationListResponse> {

    @CsExpose
    @CsSerializedName(value = "appItems", alternate = "items")
    List<AppItem> appItems;

    @Override
    protected List<AppItem> getItems() {
        return appItems;
    }

    /**
     * Get list of applications.
     * Convenience method for {@link #getItems()}
     * See also {@link AppItem}
     *
     * @return the applications
     */
    public List<AppItem> getAppItems() {
        return appItems;
    }

    @Override
    public String toString() {
        return "ApplicationListResponse{" +
                "appItems=" + appItems +
                '}';
    }
}
