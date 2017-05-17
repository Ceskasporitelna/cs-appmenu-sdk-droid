# CSAppMenuSDK

This SDK allows you to list applications of Česká spořitelna a.s. [AppMenu API](http://docs.ext0csasapplications.apiary.io/#reference/appmenu).

# [CHANGELOG](CHANGELOG.md)

# Requirements
- Android 4.1+
- CSCoreSDK 0.16+
- Gradle 2.8+
- Android Studio 1.5+

# AppMenu SDK Installation
**IMPORTANT!** You need to have your SSH keys registered with the GitHub since this repository is private.

## Install
You can install CSAppMenuSDK using the following gradle settings.

1. Check your project build.gradle file that it contains `JCenter` repository:
```gradle
    allprojects {
        repositories {
            ...
            jcenter()
            ...
        }
    }
```

2. Insert these lines into your module build.gradle file to compile CSAppMenuSDK and CoreSDK (change x.y.z to the version you want to use):
```gradle
    dependencies {
        ...
        compile 'cz.csas:cs-core-sdk:x.y.z@aar'
        compile 'cz.csas:cs-appmenu-sdk:x.y.z@aar'
        ...
    }
```

# Usage

After you've installed the SDK you will be able to use the module in your project.
Also CSAppMenuSDK has dependency to CSCoreSDK, you will be able to use it as well.

**See [CoreSDK](https://github.com/Ceskasporitelna/cs-core-sdk-droid)**

## Configuration
Before using CoreSDK in your application, you need to initialize it by providing it your WebApiKey:

```java

// Set your WebApi key
CoreSDK.getInstance().useWebApiKey( "YourApiKey" );
// Initialize appmenu
AppMenu.getInstance().useAppMenu(String appId, String categoryKey, Context context);
// Obtain your AppMenuManager
AppMenuManager manager = AppMenu.getInstance().getAppMenuManager();

```
**See [configuration guide](docs/configuration.md)** for all the available configuration options.

## Usage
**See [Usage Guide](./docs/appMenu.md)** for usage instructions.

# Contributing
Contributions are more than welcome!

Please read our [contribution guide](CONTRIBUTING.md) to learn how to contribute to this project.

# Terms and License
Please read our [terms & conditions in license](LICENSE.md)
