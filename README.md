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
You can install CSPlacesSDK using the following git and gradle settings.

1. Navigate to your git configured project repository and process this command to add CSAppMenu as a submodule:
```
git submodule add https://github.com/Ceskasporitelna/cs-appmenu-sdk-droid.git your_lib_folder/cs-appmenu-sdk-droid
```

2. Insert these two lines into your project settings.gradle file to include your submodules:
```gradle
include ':core'
project (':core').projectDir = new File(settingsDir, 'your_lib_folder/cs-appmenu-sdk-droid/lib/cs-core-sdk-droid/core')
include ':appmenu'
project (':appmenu').projectDir = new File(settingsDir, 'your_lib_folder/cs-appmenu-sdk-droid/appmenu')
```

3. Insert this line into your module build.gradle file to compile your submodules:
```gradle
dependencies {
...
compile project(':core')
compile project(':appmenu')
...
}
```

# Usage

After you've installed the SDK using git submodules you will be able to use the module in your project.
Also CSAppMenuSDK has dependency to CSCoreSDK, you will be able to use it as well.

**See [CoreSDK](https://github.com/Ceskasporitelna/cs-core-sdk-droid)**

## Configuration
Before using CoreSDK in your application, you need to initialize it by providing it your WebApiKey:

```java

// Set your WebApi key
CoreSDK.getInstance().useWebApiKey( "YourApiKey" )
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
