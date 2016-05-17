# Using AppMenuSDK

This usage guide walks you through a process of initializing AppMenuSDK and check for other apps and deciding whether or not your app is outdated. It is possible to simply list all info about all th CSAS applications and keep the version up to date by version checking provided by this sdk.

## Before You Begin

Before using any SDK in your application, you need to initialize CoreSDK by providing it your WebApiKey.

```

    // Set your WebApi key
    CoreSDK.getInstance().useWebApiKey( "YourApiKey" )

    // Initialize appmenu
    AppMenu.getInstance()
        .useAppMenu(String appId, String categoryKey, Context context);

    // Obtain your AppMenuManager
    AppMenuManager manager = AppMenu.getInstance().getAppMenuManager();

```

## Usage

This usage guide provides the tutorial how to list the CSAS applications info and check the applications versions.

### Initialization

Before you will start using AppMenu you need to init your app endpoint by calling `useAppMenu(String appId, String categoryKey, Context context)` method.

```java

    // AppMenu.getInstance().useAppMenu(String appId, String categoryKey, Context context);
    AppMenu.getInstance().useAppMenu("friends24", // name of app in Česká spořitelna a.s. systém
                                     "FRIENDS24", // category key
                                     context)

    // As a first thing you have to call this endpoint to set up app manager
    AppMenuSDK.getInstance().getAppMenuManager()
    // Now you can will be able to call methods of AppManager

```

Technically `/android` is not part of `appId`. It just a URL component for calling android version of the appmenu endpoint.

```java

    // The good way of appId initialization
    AppMenu.getInstance().useAppMenu("friends24", // name of app in Česká spořitelna a.s. systém
                                     "FRIENDS24", // category key
                                     context)

    // The bad way of appId initialization
    AppMenu.getInstance().useAppMenu("friends24/android", // name of app in Česká spořitelna a.s. systém
                                         "FRIENDS24", // category key
                                         context)

```

#### CheckingAppVersion

It is possible to enable app version checking for declared app defined by the category key by calling `startCheckingAppVersion(AppIsOutdatedCallback callback)` method. This method should be called just once, otherwise you will receive an error.

```java

    AppMenuSDK.getInstance().getAppMenuManager()
        .startCheckingAppVersion(AppIsOutdatedCallback callback);

```

There is also a function  `fakeMinimalVersionFromServer(Integer minimalVersionMajor, Integer minimalVersionMinor)` which was created for testing purpose. This function provides faking version of the category key defined application.
Next time when a new applications data will be downloaded, this app `AppItem` object will have values of `minimalVersionMajor` and `minimalVersionMinor` set by this method.

App version should be also rechecked each time the app comes to the foreground if a condition for `checkForVersionInterval` is passed. For this purpose use [`AppForegroundListener`](../appmenu/src/main/java/cz/csas/appmenu/AppForegroundListener.java) and call it each time, app comes to foreground.

```java

    AppMenuSDK.getInstance().getAppMenuManager()
        .getAppForegroundListener()
        .onApplicationOnForeground();

```

### Get AppInformation

You will get [`AppInformation`](../appmenu/src/main/java/cz/csas/appmenu/AppInformation.java) from the endpoint. It contains information about both your app and the other apps of [`AppItem`](../appmenu/src/main/java/cz/csas/appmenu/AppItem.java) type. It also contains download timestamp and source marker of type [`AppInformationSource`](../appmenu/src/main/java/cz/csas/appmenu/AppInformationSource.java):

- Server - Information is fresh
- Cache - Information is older but still valid

You can specify how old the data you accept (in seconds).

```java

        // passing allowMaxAgeInSeconds = 5 means it's ok to return cached data that has been updated less than 5 seconds ago but refresh the data anyway and return it.
        // Note that according to this behaviour it is possible to get the callback response twice!
        AppMenuSDK.getInstance().getAppMenuManager()
            .getAppInformation(Long allowMaxAgeInSeconds,CallbackWebApi<AppInformation> callback);

```

### Register callback to be notified when new AppInformation is obtained

You can call `registerAppInformationObtainedCallback(String tag,CallbackWebApi<AppInformation> callback)` method to be notified when new [`AppInformation`](../appmenu/src/main/java/cz/csas/appmenu/AppInformation.java) is obtained, if there is any data in cache you will get it immediately. Choose your unique tag to identify your callback for later unregistration.

```

        AppMenuSDK.getInstance().getAppMenuManager()
            .registerAppInformationObtainedCallback(String tag,CallbackWebApi<AppInformation> callback);

```

You have to call `unregisterAppInformationObtainedCallback(String tag)` method when you want to unregister your registered callback defined by tag.

```


    AppMenuSDK.getInstance().getAppMenuManager()
        .unregisterAppInformationObtainedCallback(String tag);

```

You can also remove all the registered callbacks.

```

    AppMenuSDK.getInstance().getAppMenuManager()
        .unregisterAllAppInformationObtainedCallbacks();

```

## Demo

Check out the [demo application](https://github.com/Ceskasporitelna/csas-sdk-demo-droid) for usage demonstration.

## Further documentation

You can look into the source code of this repository to see documented classes and methods of this SDK.

This SDK communicates with AppMenu. You can have a look at its [documentation](http://docs.ext0csasapplications.apiary.io/#reference/appmenu).
