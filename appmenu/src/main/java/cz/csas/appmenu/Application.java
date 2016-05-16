package cz.csas.appmenu;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;
import cz.csas.cscore.utils.csjson.annotations.CsSerializedName;
import cz.csas.cscore.webapi.WebApiEntity;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 14.05.16.
 */
public class Application extends WebApiEntity {

    @CsSerializedName(value = "app_name")
    private String appName;

    @CsSerializedName(value = "url_scheme")
    private String urlScheme;

    @CsSerializedName(value = "app_icon")
    private String appIconUrl;

    @CsSerializedName(value = "google_play_link")
    private String googlePlayLink;

    @CsSerializedName(value = "category_key")
    private String categoryKey;

    @CsExpose
    private String incompatibleTextCS;

    @CsExpose
    private String incompatibleTextEN;

    @CsExpose
    private String minimalVersionMajor;

    @CsExpose
    private String minimalVersionMinor;

    @CsSerializedName(value = "package_name")
    private String packageName;



    public String getAppName() {
        return appName;
    }

    public String getUrlScheme() {
        return urlScheme;
    }

    public String getAppIconUrl() {
        return appIconUrl;
    }

    public String getGooglePlayLink() {
        return googlePlayLink;
    }

    public String getCategoryKey() {
        return categoryKey;
    }

    public String getIncompatibleTextCS() {
        return incompatibleTextCS;
    }

    public String getIncompatibleTextEN() {
        return incompatibleTextEN;
    }

    public String getMinimalVersionMajor() {
        return minimalVersionMajor;
    }

    public String getMinimalVersionMinor() {
        return minimalVersionMinor;
    }

    public String getPackageName() {
        return packageName;
    }
}
