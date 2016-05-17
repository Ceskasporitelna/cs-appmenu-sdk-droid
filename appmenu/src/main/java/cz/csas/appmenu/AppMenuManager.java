package cz.csas.appmenu;

import cz.csas.cscore.client.rest.CallbackWebApi;

/**
 * The interface App menu manager.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 13.05.16.
 */
public interface AppMenuManager {

    /**
     * Retrieves AppInformation either from cache or the server and returns it through the callback.
     * The callback may be called TWICE. First when the AppInformation is returned from the cache
     * (if present there) and then when the fresh data is returned from the server. The server
     * download is triggered when no data in cache are present or when the stored data is older than
     * the allowMaxAgeInSeconds parameter specifies.
     * AppManager will retry on callers behalf until it succeeds to obtain the requested
     * AppInformation.
     * Retry interval can be specified as a AppManager property RetryInterval
     *
     * @param allowMaxAgeInSeconds specifies the maximal time interval in seconds for which the
     *                             AppInformation data are considered as relevant.
     * @param callback             the callback
     */
    public void getAppInformation(Long allowMaxAgeInSeconds,
                                  CallbackWebApi<AppInformation> callback);

    /**
     * Registers callback that will be invoked whenever AppInformation data is available.
     * You can register multiple callbacks. They will be called in order of registration when the
     * AppInformation is obtained
     *
     * @param tag      describes the tag of registering object
     * @param callback the callback
     */
    public void registerAppInformationObtainedCallback(String tag,
                                                       CallbackWebApi<AppInformation> callback);

    /**
     * Will remove reference registered callback
     *
     * @param tag describes the tag of unregistering object
     */
    public void unregisterAppInformationObtainedCallback(String tag);

    /**
     * Will remove all reference to registered callbacks
     */
    public void unregisterAllAppInformationObtainedCallbacks();

    /**
     * Checks the app version. This should be called only once in the application
     * (application#didFinishLaunchingWithOptions) method.
     * The version is checked immediately after this method is called and then every 12 hours when
     * ApplicationDidBecomeActive event is fired.
     * If the app version is outdated, a callback is fired.
     * <p/>
     * The SDK has to be configured with a categoryKey and your version in
     * gradle build file must be in the format of MAJOR.MINOR.BUILD in order for this check to work.
     * <p/>
     * If the check fails, AppManager will retry the check on callers behalf until it succeeds.
     * Retry interval can be specified as a AppManager property RetryInterval
     *
     * @param callback the callback
     */
    public void startCheckingAppVersion(AppIsOutdatedCallback callback);

    /**
     * Method for testing purposes, it will rewrite app version of the this app, so you will be able
     * to test of func {@link #startCheckingAppVersion(AppIsOutdatedCallback)}
     * behaviors
     *
     * @param minimalVersionMajor the minimal version major
     * @param minimalVersionMinor the minimal version minor
     */
    public void fakeMinimalVersionFromServer(Integer minimalVersionMajor, Integer minimalVersionMinor);

    /**
     * Set retry interval in seconds
     * Default value is 4 s
     *
     * @param interval the interval
     */
    public void setRetryInterval(long interval);

    /**
     * Set check for version interval in seconds
     * Default value is 12 * 60 * 60 s
     *
     * @param interval the interval
     */
    public void setCheckForVersionInterval(long interval);

    /**
     * Get application foreground listener.
     * This listener should be taken and triggered every time the application comes to foreground to
     * check the app version.
     *
     * @return app foreground listener
     */
    public AppForegroundListener getAppForegroundListener();

}
