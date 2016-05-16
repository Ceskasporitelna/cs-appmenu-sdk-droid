package cz.csas.appmenu;

import cz.csas.cscore.client.WebApiConfiguration;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 13.05.16.
 */
public interface AppMenuManager {


    /**
     * TODO hauseja:
     * checkForVersionInterval
     * ios: https://github.com/Ceskasporiteln/cs-appmenu-sdk-ios/blob/appmenu-2/CSAppMenuSDK/AppManager.swift#L17
     * <p/>
     * retryInterval
     * iso: https://github.com/Ceskasporiteln/cs-appmenu-sdk-ios/blob/appmenu-2/CSAppMenuSDK/AppManager.swift#L21
     */

    public void init(String appId, String categoryKey, WebApiConfiguration webApiConfiguration);

    public void getAppInformation();

    public void registerAppInformationObtainedCallback();

    public void unregisterAppInformationObtainedCallback();

    public void unregisterAllAppInfomationObtainedCallbacks();

    public void startCheckingAppVersion();

    public void fakeMinimalVersionFromServer();


    /**
     Afther app version check you can set how many seconds app should wait until next check for new version
     *//*
    public var checkForVersionInterval: NSTimeInterval = 12.0*60*60
    *//**
     If data request fail, how many seconds app should wait until next try
     *//*
    public var retryInterval: NSTimeInterval = 4.0

    public let appId: String!
    public let categoryKey: String?

    private let webApiConfiguration : WebApiConfiguration
    private var client:AppMenuClient!

    private var isCheckingAppVersion:Bool = false
    private var versionCheckedAtTimestamp: NSTimeInterval?

    private var isDownloadingData:Bool = false

    private var appIsOutdatedCallback:((thisApp: AppItem)->Void)?
    private var loaderQueue:[((appInformation: AppInformation)->Void)] = []
    private var observingCallbacks:[String:((appInformation: AppInformation)->Void)] = [:]

    private let syncQueue: dispatch_queue_t!
    private var _completionQueue: dispatch_queue_t?
    public var completionQueue: dispatch_queue_t {
        get {
            return self._completionQueue ?? dispatch_get_main_queue()
        }
        set {
            self._completionQueue = newValue
        }
    }

    private var appInformation:AppInformation?{
        didSet{
            if self.appInformation != nil {
                let data = NSKeyedArchiver.archivedDataWithRootObject(self.appInformation!)
                NSUserDefaults.standardUserDefaults().setObject(data, forKey: self.appId)
            }
        }
    }


    init(appId:String!, categoryKey:String?, webApiConfiguration : WebApiConfiguration)
    {
        self.appId = appId
        self.categoryKey = categoryKey
        self.webApiConfiguration = webApiConfiguration

        self.client = AppMenuClient(config: self.webApiConfiguration)

        self.syncQueue = dispatch_queue_create( "CSAppMenuSDK.SerialQueue", DISPATCH_QUEUE_SERIAL )
        dispatch_set_target_queue( self.syncQueue, GlobalUtilityQueue )
        self.attemptToLoadAppInfoFromDefaults()
    }

    deinit{
        NSNotificationCenter.defaultCenter().removeObserver(self)
    }

    private func attemptToLoadAppInfoFromDefaults()
    {
        if self.appInformation == nil{
        if UIApplication.sharedApplication().protectedDataAvailable{
            if let cachedAppInfoData:NSData = NSUserDefaults.standardUserDefaults().objectForKey(appId) as? NSData{
                self.appInformation = NSKeyedUnarchiver.unarchiveObjectWithData(cachedAppInfoData) as? AppInformation
            }
        }
    }
    }

    //MARK: -
    *//**
     Retrieves AppInformation either from cache or the server and returns it thorugh the callback.
     The callback may be called TWICE. First when the AppInformation is returned from the cache (if present there) and then when the fresh data is returned from the server. The server download is trigerred when no data in cache are present or when the stored data is older than the allowOlderThanInSeconds parameter specifies.
     AppManager will retry on callers behalf until it succeeds to obtain the requested AppInformation.
     Retry interval can be specified as a AppManager property RetryInterval
     *//*
    public func getAppInformation(allowOlderThanInSeconds allowOlderThanInSeconds:NSTimeInterval, callback: ((appInformation: AppInformation)->Void))
    {
        dispatch_async( self.syncQueue, {

                self.attemptToLoadAppInfoFromDefaults()
        if self.appInformation != nil {
        if allowOlderThanInSeconds > self.appInformation?.timeIntervalSinceDownload() {
            self.callCallBack(callback)
        }else{
            self.callCallBack(callback)
            self.loadAppInfo(callback)
        }
    }else{
        self.loadAppInfo(callback)
    }
        })
    }

    private func loadAppInfo(callback: ((appInformation: AppInformation)->Void))
    {
        self.loaderQueue.append(callback)
        if !self.isDownloadingData{
        self.loadApps()
    }
    }

    private func callCallBack(callback: ((appInformation: AppInformation)->Void))
    {
        if self.appInformation != nil{
        dispatch_async( self.completionQueue, {

                callback(appInformation:self.appInformation!)
        })
    }
    }

    //MARK: - load apps
    private func loadApps()
    {
        self.isDownloadingData = true
        self.client.application.parameters(self.appId).list { (result) in
        switch result{
            case .Success(let apps):
            var otherApps:[AppItem] = []
            var thisApp:AppItem?
            for app in apps.items{
                if !app.itunesLink.isEmpty && !app.appName.isEmpty{
                    if app.categoryKey == self.categoryKey {
                        thisApp = AppItem(app: app)
                    }else{
                        otherApps.append(AppItem(app: app))
                    }
                }
            }
            let appInfo = AppInformation(thisApp: thisApp, otherApps: otherApps)
            self.isDownloadingData = false
            self.notifyForCallBack(appInfo)
            self.notifyLoaderQueue(appInfo)
            self.appInformation = AppInformation(thisApp: appInfo.thisApp, otherApps: appInfo.otherApps, source: .Cache, downloadedAtTimestamp: appInfo.downloadedAtTimestamp)

            case .Failure(_):
            dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (Int64)(UInt64(self.retryInterval)*NSEC_PER_SEC)), self.syncQueue) {
                self.loadApps()
            }
        }
    }
    }

    private func notifyForCallBack(appInfo:AppInformation)
    {
        dispatch_async( self.completionQueue, {

        for tag in self.observingCallbacks.keys {
        if let callback:((appInformation: AppInformation)->Void) = self.observingCallbacks[tag]{
            callback(appInformation: appInfo)
        }
    }
        })
    }

    private func notifyLoaderQueue(appInfo:AppInformation)
    {
        dispatch_async( self.completionQueue, {

        for callback in self.loaderQueue {
        callback(appInformation: appInfo)
    }
        self.loaderQueue.removeAll()
        })
    }

    //MARK: -
    *//**
     Registers callback that will be invoked when new AppInformation data is downloaded, if there is any data in cache you will get it immediately.
     You can register multiple callbacks. They will be called in order of registration when the AppInformation is obtaind

     - parameter tag: tag of registering object
     *//*
    public func registerAppInformationObtainedCallback(tag tag:String, callback: ((appInformation: AppInformation)->Void))
    {
        self.observingCallbacks[tag] = (callback)

        if self.appInformation != nil {
        dispatch_async( self.completionQueue, {
                callback(appInformation: self.appInformation!)
        })
    }
    }

    *//**
     Will remove reference registered callback

     - parameter tag: tag of unregistering object
     *//*
    public func unregisterAppInformationObtainedCallback(tag tag:String)
    {
        self.observingCallbacks.removeValueForKey(tag)
    }

    *//**
     Will remove all reference to registered callbacks
     *//*
    public func unregisterAllAppInfomationObtainedCallbacks()
    {
        self.observingCallbacks.removeAll()
    }

    //MARK: -
    *//**
     Checks the app version. This should be called only once in the application(application:, didFinishLaunchingWithOptions) method.
     The version is checked immidately after this method is called and then every 12 hours when UIApplicationDidBecomeActive event is fired, interval can be changed with property checkForVersionInterval.
     If the app version is outdated, a callback is fired.

     The SDK has to be configured with a categoryKey and your version in CFBundleShortVersionString must be in the format of MAJOR.MINOR in order for this check to work.

     If the check fails, AppManager will retry the check on callers behalf until it succeeds. Retry interval can be specified as a AppManager property retryInterval
     *//*
    public func startCheckingAppVersion(appIsOutdatedCallback:((thisApp: AppItem)->Void))
    {
        if !self.isCheckingAppVersion{
        self.isCheckingAppVersion = true

        let notificationCenter = NSNotificationCenter.defaultCenter()
        notificationCenter.addObserver(self, selector: #selector(applicationDidBecomeActiveNotification), name: UIApplicationDidBecomeActiveNotification, object: nil)

        self.appIsOutdatedCallback = appIsOutdatedCallback
        self.chechAppVersion()
    } else {
        assert(true, "You called checkingAppVersion for the second time! You can call it just once!")
        return
    }
    }
*/
}
