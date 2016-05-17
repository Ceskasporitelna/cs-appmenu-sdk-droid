# Configuration
In order to use the AppMenuSDK, you have to configure the CoreSDK.
## 1. Configure CoreSDK

Before using any SDK based on CoreSDK in your application, you need to initialize it by providing it your WebApiKey:

```java
    CoreSDK.getInstance().useWebApiKey( "YourApiKey" )
```

For more configuration options see **[CoreSDK configuration guide](https://github.com/Ceskasporitelna/cs-core-sdk-droid/blob/master/docs/configuration.md)**

## 2. Configure AppMenuSDK
You can find example of AppMenu configuration options below:
```java

    AppMenu.getInstance().useAppMenu(String appId, String categoryKey, Context context);

```
It is necessary to have configured CoreSDK as noted above in the first section.

Now you are all set to use the AppMenu! See the [AppMenu usage guide](appmenu.md) to learn how to use AppMenuSDK.


