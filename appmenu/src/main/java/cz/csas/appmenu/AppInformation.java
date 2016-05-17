package cz.csas.appmenu;

import java.io.Serializable;
import java.util.List;

/**
 * The type App information.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 23 /05/16.
 */
public class AppInformation implements Serializable {

    private AppItem thisApp;
    private List<AppItem> otherApps;
    private AppInformationSource source;
    private long downloadedAtTimestamp;

    /**
     * Instantiates a new App information.
     *
     * @param thisApp               Object describing this application. It will be only present if
     *                              categoryKey is set during the SDK initialization and only if the
     *                              server returned it
     * @param otherApps             Array of objects describing other applications
     * @param source                indicates was this app information obtained from
     * @param downloadedAtTimestamp timestamp
     */
    public AppInformation(AppItem thisApp, List<AppItem> otherApps, AppInformationSource source, long downloadedAtTimestamp) {
        this.thisApp = thisApp;
        this.otherApps = otherApps;
        this.source = source;
        this.downloadedAtTimestamp = downloadedAtTimestamp;
    }

    /**
     * Instantiates a new App information.
     *
     * @param thisApp   Object describing this application. It will be only present if categoryKey
     *                  is set during the SDK initialization and only if the server returned it
     * @param otherApps Array of objects describing other applications
     */
    public AppInformation(AppItem thisApp, List<AppItem> otherApps) {
        this.thisApp = thisApp;
        this.otherApps = otherApps;
        this.source = AppInformationSource.SERVER;
        this.downloadedAtTimestamp = System.currentTimeMillis();
    }

    /**
     * Object describing this application. It will be only present if categoryKey is set during the
     * SDK initialization and only if the server returned it
     *
     * @return this app
     */
    public AppItem getThisApp() {
        return thisApp;
    }

    /**
     * Array of objects describing other applications
     *
     * @return the other apps
     */
    public List<AppItem> getOtherApps() {
        return otherApps;
    }

    /**
     * Indicates was this app information obtained from
     *
     * @return the source
     */
    public AppInformationSource getSource() {
        return source;
    }

    /**
     * Get downloaded at timestamp.
     *
     * @return the timestamp
     */
    public long getDownloadedAtTimestamp() {
        return downloadedAtTimestamp;
    }

    /**
     * Seconds since this app information has been obtained
     *
     * @return the long
     */
    public long timeIntervalSinceDownload() {
        return System.currentTimeMillis() - downloadedAtTimestamp;
    }
}
