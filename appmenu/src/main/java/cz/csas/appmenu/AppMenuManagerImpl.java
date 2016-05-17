package cz.csas.appmenu;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.csas.appmenu.applications.ApplicationListResponse;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.client.rest.android.MainThreadExecutor;
import cz.csas.cscore.error.CsSDKError;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 23/05/16.
 */
class AppMenuManagerImpl implements AppMenuManager {

    private final String APP_MENU_MANAGER_CACHE = "app_menu_manager_cache";
    private String appId;
    private String categoryKey;
    private Context context;

    private AppInformation appInformation;
    private SharedPreferences appMenuManagerCache;
    private SharedPreferences.Editor appMenuManagerEditor;
    private AppMenuClient appMenuClient;
    private AppIsOutdatedCallback appIsOutdatedCallback;
    private long versionCheckedAtTimestamp;
    private Handler handler;
    /**
     * retry interval in seconds
     */
    private long retryInterval = 4;
    /**
     * check for version interval in seconds
     */
    private long checkForVersionInterval = 12 * 60 * 60;
    private Integer[] fakeMinimalVersion = new Integer[2];
    private boolean isDownloadingData = false;
    private boolean isCheckingAppVersion = false;
    private List<CallbackWebApi<AppInformation>> loaderQueue = new ArrayList<>();
    private BackgroundQueue backgroundQueue = new BackgroundQueue("app-menu-background-thread");
    private MainThreadExecutor mainThreadExecutor = new MainThreadExecutor();
    private Map<String, CallbackWebApi<AppInformation>> observingCallbacks = new HashMap<>();
    private AppForegroundListener appForegroundListener = new AppForegroundListener() {
        @Override
        public void onApplicationOnForeground() {
            long checkDiffTime = System.currentTimeMillis() - versionCheckedAtTimestamp;
            if (checkDiffTime > checkForVersionInterval * 1000)
                checkAppVersion();
        }
    };

    private AppVersion testCurrentAppVersion;

    public AppMenuManagerImpl(String appId, String categoryKey, Context context) {
        this.appId = appId;
        this.categoryKey = categoryKey;
        this.context = context;

        // init necessary objects
        appMenuManagerCache = context.getSharedPreferences(APP_MENU_MANAGER_CACHE, Context.MODE_PRIVATE);
        appMenuManagerEditor = appMenuManagerCache.edit();
        this.appMenuClient = AppMenu.getInstance().getAppMenuClient();
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void getAppInformation(final Long allowMaxAgeInSeconds, final CallbackWebApi<AppInformation> callback) {
        backgroundQueue.addToQueue(new Runnable() {
            @Override
            public void run() {
                retrieveFromCache(categoryKey);
                if (appInformation != null) {
                    executeSuccessOnMainThread(callback, appInformation);
                    if (allowMaxAgeInSeconds * 1000 <= (System.currentTimeMillis() - appInformation.getDownloadedAtTimestamp()))
                        loadAppInfo(callback);
                } else
                    loadAppInfo(callback);
            }
        });
    }

    private void loadAppInfo(final CallbackWebApi<AppInformation> callback) {
        backgroundQueue.addToQueue(new Runnable() {
            @Override
            public void run() {
                if (isDownloadingData)
                    loaderQueue.add(callback);
                else
                    loadApplications(callback);
            }
        });
    }

    private void loadApplications(final CallbackWebApi<AppInformation> callback) {
        backgroundQueue.addToQueue(new Runnable() {
            @Override
            public void run() {
                isDownloadingData = true;
                appMenuClient.getApplicationsResource().withId(appId).list(new CallbackWebApi<ApplicationListResponse>() {
                    @Override
                    public void success(ApplicationListResponse applicationListResponse) {
                        List<AppItem> otherApps = new ArrayList<>();
                        AppItem thisApp = null;
                        for (AppItem appItem : applicationListResponse.getAppItems()) {
                            if (appItem.getPackageName() != null && appItem.getAppName() != null) {
                                if (appItem.getCategoryKey().equals(categoryKey)) {
                                    thisApp = appItem;
                                    if (fakeMinimalVersion[0] != null) {
                                        thisApp.setMinimalVersionMajor(String.valueOf(fakeMinimalVersion[0]));
                                        thisApp.setMinimalVersionMinor(String.valueOf(fakeMinimalVersion[1]));
                                    }
                                } else
                                    otherApps.add(appItem);
                            }
                        }

                        AppInformation appInfo = new AppInformation(thisApp, otherApps);
                        executeSuccessOnMainThread(callback, appInfo);
                        notifyLoaderQueue(appInfo);
                        notifyObservingCallbacks(appInfo);
                        isDownloadingData = false;
                        appInformation = new AppInformation(appInfo.getThisApp(), appInfo.getOtherApps(), AppInformationSource.CACHE, appInfo.getDownloadedAtTimestamp());
                        saveToCache(categoryKey, appInformation);
                    }

                    @Override
                    public void failure(CsSDKError error) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadApplications(callback);
                            }
                        }, retryInterval * 1000);
                    }
                });
            }
        });

    }

    private void notifyLoaderQueue(AppInformation appInformation) {
        for (CallbackWebApi<AppInformation> callback : loaderQueue) {
            executeSuccessOnMainThread(callback, appInformation);
        }
    }

    private void notifyObservingCallbacks(AppInformation appInformation) {
        for (String key : observingCallbacks.keySet()) {
            executeSuccessOnMainThread(observingCallbacks.get(key), appInformation);
        }
    }

    @Override
    public void registerAppInformationObtainedCallback(String tag, CallbackWebApi<AppInformation> callback) {
        observingCallbacks.put(tag, callback);
        if (appInformation != null)
            executeSuccessOnMainThread(callback, appInformation);
    }

    @Override
    public void unregisterAppInformationObtainedCallback(String tag) {
        observingCallbacks.remove(tag);
    }

    @Override
    public void unregisterAllAppInformationObtainedCallbacks() {
        observingCallbacks.clear();
    }

    @Override
    public void startCheckingAppVersion(AppIsOutdatedCallback callback) throws CsAppMenuError {
        if (!isCheckingAppVersion) {
            isCheckingAppVersion = true;
            this.appIsOutdatedCallback = callback;
            checkAppVersion();
        } else
            throw new CsAppMenuError(CsAppMenuError.Kind.OTHER, "You called checkingAppVersion for the second time! You can call it just once!");
    }

    private void checkAppVersion() {
        backgroundQueue.addToQueue(new Runnable() {
            @Override
            public void run() {
                getAppInformation(10L, new CallbackWebApi<AppInformation>() {
                    @Override
                    public void success(AppInformation appInformation) {
                        versionCheckedAtTimestamp = System.currentTimeMillis();
                        try {
                            AppItem thisApp = appInformation.getThisApp();
                            AppVersion currentVersion = getCurrentAppVersion(context);
                            AppVersion serverVersion = thisApp.appVersion();
                            if (serverVersion.compareTo(currentVersion) > 0)
                                executeSuccessOnMainThread(appIsOutdatedCallback, thisApp);
                        } catch (PackageManager.NameNotFoundException e) {
                            executeFailureOnMainThread(appIsOutdatedCallback, new CsAppMenuError(CsAppMenuError.Kind.OTHER, "Provided package name is not found."));
                        } catch (CsAppMenuError error) {
                            executeFailureOnMainThread(appIsOutdatedCallback, error);
                        }
                    }

                    @Override
                    public void failure(CsSDKError error) {
                    }
                });
            }
        });
    }

    @Override
    public void fakeMinimalVersionFromServer(Integer minimalVersionMajor, Integer minimalVersionMinor) {
        this.fakeMinimalVersion[0] = minimalVersionMajor;
        this.fakeMinimalVersion[1] = minimalVersionMinor;
    }

    @Override
    public void setRetryInterval(long interval) {
        this.retryInterval = interval;
    }

    @Override
    public void setCheckForVersionInterval(long interval) {
        this.checkForVersionInterval = interval;
    }

    @Override
    public AppForegroundListener getAppForegroundListener() {
        return appForegroundListener;
    }

    protected void retrieveFromCache(final String categoryKey) {
        if (appInformation == null && appMenuManagerCache.contains(categoryKey)) {
            try {
                appInformation = (AppInformation) new ObjectInputStream(new Base64InputStream(
                        new ByteArrayInputStream(appMenuManagerCache.getString(categoryKey, "").getBytes()),
                        Base64.NO_PADDING | Base64.NO_WRAP)).readObject();
            } catch (Exception e) {
                // consume error
            }
        }
    }

    protected void saveToCache(final String categoryKey, final AppInformation appInformation) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(
                    new Base64OutputStream(baos, Base64.NO_PADDING | Base64.NO_WRAP));
            oos.writeObject(appInformation);
            oos.close();
            appMenuManagerEditor.putString(categoryKey, baos.toString("UTF-8"));
            appMenuManagerEditor.commit();
        } catch (IOException e) {
            // consume error
        }
    }

    protected void clearCache() {
        appMenuManagerEditor.clear();
        appMenuManagerEditor.commit();
    }

    protected void setAppMenuClient(AppMenuClient appMenuClient) {
        this.appMenuClient = appMenuClient;
    }

    private void executeSuccessOnMainThread(final CallbackWebApi callback, final Object object) {
        mainThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                callback.success(object);
            }
        });
    }

    private void executeFailureOnMainThread(final CallbackWebApi callback, final CsSDKError error) {
        mainThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                callback.failure(error);
            }
        });
    }

    /**
     * The type Background queue provides the background thread executing queue.
     */
    private class BackgroundQueue extends HandlerThread {

        private Handler handler;

        /**
         * Instantiates a new Background queue.
         *
         * @param name the name
         */
        public BackgroundQueue(String name) {
            super(name);
            this.start();
            handler = new Handler(this.getLooper());
        }

        /**
         * Add runnable to queue.
         *
         * @param runnable the runnable
         */
        public void addToQueue(Runnable runnable) {
            this.handler.post(runnable);
        }

    }

    void setTestCurrentAppVersion(AppVersion testCurrentAppVersion) {
        this.testCurrentAppVersion = testCurrentAppVersion;
    }

    private AppVersion getCurrentAppVersion(Context context) throws PackageManager.NameNotFoundException {
        if (testCurrentAppVersion != null)
            return testCurrentAppVersion;
        return new AppVersion(context);
    }

}
