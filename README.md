# Banuba AI Video Editor SDK - React Native Expo-CLI integration sample.
Banuba [AI Video Editor SDK](https://www.banuba.com/video-editor-sdk) allows you to quickly add short video functionality and possibly AR filters and effects into your mobile app.
<br></br>
:exclamation: <ins>Support for React Native plugin is under development at the moment and scheduled for __Q4__. Please, reach out to [support team](https://www.banuba.com/faq/kb-tickets/new) to help you with your own Flutter integration.<ins>

<ins>Keep in mind that main part of integration and customization should be implemented in **android**, **ios** directories using Native Android and iOS development.<ins>

This sample demonstrates how to run VE SDK with [React Native](https://reactnative.dev/) and [Expo-CLI](https://docs.expo.dev/workflow/expo-cli/).

## Dependencies
|              | Version | 
|--------------|:-------:|
| npm          | 8.18.0  |
| react native | ~0.63.4 | 
| expo         | ~40.0.0 |

## Integration

### Token
We offer а free 14-days trial for you could thoroughly test and assess Video Editor SDK functionality in your app.

To get access to your trial, please, get in touch with us by [filling a form](https://www.banuba.com/video-editor-sdk) on our website. Our sales managers will send you the trial token.

:exclamation: The token **IS REQUIRED** to run sample and an integration in your app.</br>

### Step 1 - Prepare project
1. Complete React Native [Environment setup](https://reactnative.dev/docs/environment-setup)
2. Install [Expo CLI](https://docs.expo.dev/get-started/installation/)
3. Run ```npx install-expo-modules``` to get the latest expo modules.

### Step 2 - Run sample Android app
1. Put Banuba token in [resources](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/android/app/src/main/res/values/strings.xml#L6).
2. Make sure variable **ANDROID_SDK_ROOT** is set in your environment or setup [sdk.dir](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/master/android/local.properties#L1).
3. Run command ```npm run android``` in terminal to launch the sample app on a device or launch the app in IDE i.e. Intellij, VC, etc.
4. [Follow further instructions](https://github.com/Banuba/ve-sdk-android-integration-sample) to integrate VE SDK in your app using native Android development.

__Configure export__

Set custom export video file name ```ExportParams.Builder.fileName()``` method.<br>
Please see [full example](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/android/app/src/main/java/com/vesdkreactnativeintegrationsample/videoeditor/export/IntegrationAppExportParamsProvider.kt#L39).

VE SDK is launched within ```VideoCreationActivity```. Exported video is returned from the Activity into ```onActivityResult``` callback
in [VideoEditorModule](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/android/app/src/main/java/com/vesdkreactnativeintegrationsample/VideoEditorModule.kt#L25). 

[Promises](https://reactnative.dev/docs/native-modules-android#promises) is used to make a bridge between Android and JS.<br>
Please see [an example](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/App.js#L39)
how to get exported video uri as a String value on JS side.

You can configure all data passed from ```VideoEditorModule``` to JS depends on your requirements.

# Step 3 - Run sample iOS app  
1. Install Cocoa Pods dependencies using ```pod install``` in terminal from **ios** directory.
2. Put Banuba token in [VideoEditorModule initializer](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/ios/VideoEditorModule.swift#L34).
3. Run command ```npm run ios``` in terminal to launch the sample on device or launch the app in IDE i.e. XCode, Intellij, VC, etc..
4. [Follow further instructions](https://github.com/Banuba/ve-sdk-ios-integration-sample) to integrate VE SDK in your app using native iOS development.


