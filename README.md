# Banuba Video Editor SDK - React Native Expo-CLI integration sample.
[Banuba Video Editor SDK](https://www.banuba.com/video-editor-sdk) allows you to quickly add short video functionality and possibly AR filters and effects into your mobile app.
<br></br>
:exclamation: <ins>Support for React Native plugin is under development at the moment and scheduled for __end Q4__. Please, reach out to [support team](https://www.banuba.com/faq/kb-tickets/new) to help you with your React Native Expo project.<ins>

<ins>Keep in mind that main part of integration and customization should be implemented in **android**, **ios** directories using Native Android and iOS development.<ins>

This sample demonstrates how to run Video Editor SDK with [React Native](https://reactnative.dev/) and [Expo-CLI](https://docs.expo.dev/workflow/expo-cli/).

## Dependencies
|              | Version | 
|--------------|:-------:|
| node         | 8.18.0  |
| react native | ~0.63.4 | 
| expo         | ~40.0.0 |
| Android      |  6.0+   |
| iOS          |  12.0+  |

## Integration

### Token
We offer а free 14-days trial for you could thoroughly test and assess Video Editor SDK functionality in your app.

To get access to your trial, please, get in touch with us by [filling a form](https://www.banuba.com/video-editor-sdk) on our website. Our sales managers will send you the trial token.

:exclamation: The token **IS REQUIRED** to run sample and an integration in your app.</br>

### Prepare project
1. Complete React Native [Environment setup](https://reactnative.dev/docs/environment-setup)
2. Install [Expo CLI](https://docs.expo.dev/get-started/installation/)
3. Complete [Running On Device](https://reactnative.dev/docs/running-on-device)
4. Run ```npx install-expo-modules``` in terminal to get the latest expo modules.

### Android
1. Make sure variable ```ANDROID_SDK_ROOT``` is set in your environment or setup [sdk.dir](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/master/android/local.properties#L1).
2. Set Banuba token in the sample app [resources](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/android/app/src/main/res/values/strings.xml#L6).
3. Run ```npm run android``` in terminal to launch the sample app on a device or launch the app in IDE i.e. Intellij, VC, etc.
4. Follow [Android Integration Guide](mddocs/android_integration.md) to integrate Video Editor SDK into your React Native Expo project.

### iOS  
1. Install Cocoa Pods dependencies using ```pod install``` in terminal from **ios** directory.
2. Set Banuba token in [VideoEditorModule initializer](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/ios/VideoEditorModule.swift#L34).
3. Run command ```npm run ios``` in terminal to launch the sample on device or launch the app in IDE i.e. XCode, Intellij, VC, etc..
4. Follow [iOS Integration Guide](mddocs/ios_integration.md) to integrate Video Editor SDK into your React Native Expo project.


