# Banuba Video Editor SDK - React Native Expo-CLI integration sample.

## Overview
[Banuba Video Editor SDK](https://www.banuba.com/video-editor-sdk) allows you to quickly add short video functionality and possibly AR filters and effects into your mobile app.
<br>  
The sample demonstrates how to run Video Editor SDK with [React Native](https://reactnative.dev/) and [Expo-CLI](https://docs.expo.dev/workflow/expo-cli/).  

## Usage
### License
Before you commit to a license, you are free to test all the features of the SDK for free.  
The trial period lasts 14 days. To start it, [send us a message](https://www.banuba.com/video-editor-sdk#form).  
We will get back to you with the trial token.

Feel free to [contact us](https://www.banuba.com/faq/kb-tickets/new) if you have any questions.

### Installation
1. Complete React Native [Environment setup](https://reactnative.dev/docs/environment-setup)
2. Install [Expo CLI](https://docs.expo.dev/get-started/installation/)
3. Complete [Running On Device](https://reactnative.dev/docs/running-on-device)
4. Run ```yarn install``` to install required project dependencies.
5. Set Banuba license token [within the app](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/App.js#L14).

### Run on Android
1. Make sure variable ```ANDROID_SDK_ROOT``` is set in your environment or setup [sdk.dir](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/master/android/local.properties#L1).
2. Run ```npm run android``` in terminal to launch the sample app on a device or launch the app in IDE.
3. Follow [Android Integration Guide](mddocs/android_integration.md) to integrate Video Editor SDK into your React Native Expo project.

### Run on iOS  
1. Install Cocoa Pods dependencies. Run ```cd ios``` and ```pod install``` in terminal.
2. Open **Signing & Capabilities** tab in Target settings and select your Development Team.
3. Run ```npm run ios``` in terminal to launch the sample on simulator (```iPhone 11``` default) or choose device in XCode to launch the sample.
4. Follow [iOS Integration Guide](mddocs/ios_integration.md) to integrate Video Editor SDK into your React Native Expo project.

## Dependencies
|              | Version | 
|--------------|:-------:|
| node         | 8.18.0  |
| react native | ~0.63.4 | 
| expo         | ~40.0.0 |
| Android      |  6.0+   |
| iOS          |  12.0+  |
