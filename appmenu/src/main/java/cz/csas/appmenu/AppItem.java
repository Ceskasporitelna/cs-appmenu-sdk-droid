package cz.csas.appmenu;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.io.Serializable;
import java.util.Map;

import cz.csas.appmenu.applications.ApplicationResource;
import cz.csas.appmenu.applications.NodesResource;
import cz.csas.cscore.utils.csjson.annotations.CsExpose;
import cz.csas.cscore.utils.csjson.annotations.CsSerializedName;
import cz.csas.cscore.webapi.WebApiEntity;

/**
 * The type Application provides information about Application.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 14.05.16.
 */
public class AppItem extends WebApiEntity implements Serializable {

    @CsExpose
    @CsSerializedName(value = "app_name")
    private String appName;

    @CsExpose
    @CsSerializedName(value = "url_scheme")
    private String urlScheme;

    @CsExpose
    @CsSerializedName(value = "app_icon")
    private String appIconUrl;

    @CsExpose
    @CsSerializedName(value = "google_play_link")
    private String googlePlayLink;

    @CsExpose
    @CsSerializedName(value = "category_key")
    private String categoryKey;

    @CsExpose
    private String incompatibleTextCS;

    @CsExpose
    private String incompatibleTextEN;

    @CsExpose
    private String descriptionTextCS;

    @CsExpose
    private String descriptionTextEN;

    @CsExpose
    private String minimalVersionMajor;

    @CsExpose
    private String minimalVersionMinor;

    @CsExpose
    @CsSerializedName(value = "package_name")
    private String packageName;

    private Map<String, String> rawData;


    /**
     * Get Application name.
     *
     * @return Name of the Application
     */
    public String getAppName() {
        return appName;
    }


    /**
     * Get Application URL scheme.
     *
     * @return the url scheme
     */
    public String getUrlScheme() {
        return urlScheme;
    }


    /**
     * Get Application icon url.
     *
     * @return the icon url
     */
    public String getAppIconUrl() {
        return appIconUrl;
    }


    /**
     * Get Application's google play link .
     *
     * @return the google play link
     */
    public String getGooglePlayLink() {
        return googlePlayLink;
    }

    /**
     * Get category key of Application.
     *
     * @return the category key
     */
    public String getCategoryKey() {
        return categoryKey;
    }

    /**
     * Get czech version of text for incompatible Application.
     *
     * @return the incompatible text cs
     */
    public String getIncompatibleTextCS() {
        return incompatibleTextCS;
    }

    /**
     * Get english version of text for incompatible Application.
     *
     * @return the incompatible text en
     */
    public String getIncompatibleTextEN() {
        return incompatibleTextEN;
    }

    /**
     * Get czech version of text for Application description.
     *
     * @return the incompatible text cs or null
     */
    public String getDescriptionTextCS() {
        return descriptionTextCS;
    }

    /**
     * Get english version of text for Application description.
     *
     * @return the incompatible text en or null
     */
    public String getDescriptionTextEN() {
        return descriptionTextEN;
    }

    /**
     * Get minimal major version of Application.
     *
     * @return minimal major version
     */
    public String getMinimalVersionMajor() {
        return minimalVersionMajor;
    }

    /**
     * Sets minimal version major.
     *
     * @param minimalVersionMajor the minimal version major
     */
    public void setMinimalVersionMajor(String minimalVersionMajor) {
        this.minimalVersionMajor = minimalVersionMajor;
    }

    /**
     * Get minimal minor version of Application.
     *
     * @return minimal minor version
     */
    public String getMinimalVersionMinor() {
        return minimalVersionMinor;
    }

    /**
     * Sets minimal version minor.
     *
     * @param minimalVersionMinor the minimal version minor
     */
    public void setMinimalVersionMinor(String minimalVersionMinor) {
        this.minimalVersionMinor = minimalVersionMinor;
    }

    /**
     * Get package name of Application.
     *
     * @return the package name
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * Gets raw data.
     *
     * @return the raw data
     */
    public Map<String, String> getRawData() {
        return rawData;
    }

    /**
     * Sets raw data.
     *
     * @param rawData the raw data
     */
    public void setRawData(Map<String, String> rawData) {
        this.rawData = rawData;
    }

    @Override
    public String toString() {
        return "AppItem{" +
                "appName='" + appName + '\'' +
                ", urlScheme='" + urlScheme + '\'' +
                ", appIconUrl='" + appIconUrl + '\'' +
                ", googlePlayLink='" + googlePlayLink + '\'' +
                ", categoryKey='" + categoryKey + '\'' +
                ", incompatibleTextCS='" + incompatibleTextCS + '\'' +
                ", incompatibleTextEN='" + incompatibleTextEN + '\'' +
                ", minimalVersionMajor='" + minimalVersionMajor + '\'' +
                ", minimalVersionMinor='" + minimalVersionMinor + '\'' +
                ", packageName='" + packageName + '\'' +
                ", rawData=" + rawData +
                '}';
    }

    /**
     * return true if application is installed on device
     *
     * @param context the context
     * @return the boolean
     */
    public boolean isInstalled(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * Open app.
     *
     * @param context the context
     */
    public void openApp(Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(intent);
        }
    }

    /**
     * Open google play page.
     *
     * @param context the context
     */
    public void openGooglePlayPage(Context context) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + packageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
        }
    }

    /**
     * Open.
     *
     * @param context the context
     */
    public void open(Context context) {
        if (isInstalled(context))
            openApp(context);
        else
            openGooglePlayPage(context);
    }

    /**
     * App version of the application object.
     *
     * @return the app version
     */
    public AppVersion appVersion() {
        if (minimalVersionMajor != null && minimalVersionMinor != null)
            return new AppVersion(Integer.parseInt(minimalVersionMajor),
                    Integer.parseInt(minimalVersionMinor));
        return null;
    }

    /**
     * Convenience getter for nodes resource
     *
     * @return the nodes resource
     */
    public NodesResource getNodesResource() {
        if (resource instanceof ApplicationResource)
            return ((ApplicationResource) resource).getNodesResource();
        return null;
    }
}
