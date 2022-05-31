# Banuba AI Video Editor SDK. Integration sample for React Native.
Banuba [Video Editor SDK](https://www.banuba.com/video-editor-sdk) allows you to add a fully-functional video editor with Tiktok-like features, AR filters and effects in your app.   
The following sample brifly demonstrates how you can integrate our SDK into your [React Native](https://reactnative.dev/) project.  

# Android  
1. Make sure variable **ANDROID_SDK_ROOT** is in your environment.
2. Put [Banuba Face AR token](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/android/app/src/main/res/values/strings.xml#5) in resources.
3. Run command **npm run android** to launch the sample on device or **cd android && ./gradlew clean && cd .. && npm run android** to clean and re-run the sample.

## How to get exported video file

Video Editor SDK is launched within VideoCreationActivity. Therefore exported video is returned from this activity into onActivityResult callback in [VideoEditorModule](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/android/app/src/main/java/com/vesdkreactnativeintegrationsample/VideoEditorModule.kt). 

To connect android platform code with JS we use [Promise mechanim](https://reactnative.dev/docs/native-modules-android#promises). In this sample we send an exported video uri as a string from our VideoEditorModule into JS code (checkout [App.js](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/App.js#L39) file for details).

You can configure all data passed from VideoEditorModule depends on your requirements. 

**To set exported video name** just place the desired one into `fileName()` builder method inside [IntegrationAppExportParamsProvider](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/android/app/src/main/java/com/vesdkreactnativeintegrationsample/videoeditor/export/IntegrationAppExportParamsProvider.kt#L39) class.


# iOS  
1. Install node and cocoapods dependencies using `npm ci && cd ios && pod install`.
1. Put [Banuba Face AR token](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/ios/VideoEditorModule.swift#L34).
1. Run command **npm run ios** to launch the sample on device.

# Want to know more about the SDK?  
Please visit our main repositories with native [Android](https://github.com/Banuba/ve-sdk-android-integration-sample) and [iOS](https://github.com/Banuba/ve-sdk-ios-integration-sample) integrations to get full information about the SDK.

