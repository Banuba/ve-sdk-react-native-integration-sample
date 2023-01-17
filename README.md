# Banuba Video Editor SDK - React Native Expo-CLI integration sample.
[Banuba Video Editor SDK](https://www.banuba.com/video-editor-sdk) allows you to quickly add short video functionality and possibly AR filters and effects into your mobile app.
<br></br>
This sample demonstrates how to run Video Editor SDK with [React Native](https://reactnative.dev/) and [Expo-CLI](https://docs.expo.dev/workflow/expo-cli/).  

<ins>The main part of integration and customization should be implemented in **android**, **ios** directories using Native Android and iOS development.<ins>

## Dependencies
|              | Version | 
|--------------|:-------:|
| node         | 8.18.0  |
| react native | ~0.63.4 | 
| expo         | ~40.0.0 |
| Android      |  6.0+   |
| iOS          |  12.0+  |

## Usage

### Token
Before you commit to a license, you are free to test all the features of the SDK for free.  
The trial period lasts 14 days. To start it, [send us a message](https://www.banuba.com/video-editor-sdk#form).  
We will get back to you with the trial token.
You can store the token within the app.

Feel free to [contact us](https://www.banuba.com/faq/kb-tickets/new) if you have any questions.

### Prepare project
1. Complete React Native [Environment setup](https://reactnative.dev/docs/environment-setup)
2. Install [Expo CLI](https://docs.expo.dev/get-started/installation/)
3. Complete [Running On Device](https://reactnative.dev/docs/running-on-device)
4. Run ```yarn install``` to install required project dependencies.

### Android
1. Make sure variable ```ANDROID_SDK_ROOT``` is set in your environment or setup [sdk.dir](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/master/android/local.properties#L1).
2. Set Banuba license token [within the app](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/App.js#L14).
3. Run ```npm run android``` in terminal to launch the sample app on a device or launch the app in IDE i.e. Intellij, VC, etc.
4. Follow [Android Integration Guide](mddocs/android_integration.md) to integrate Video Editor SDK into your React Native Expo project.

### iOS  
1. Install Cocoa Pods dependencies. Run ```cd ios``` and ```pod install``` in terminal.
2. Set Banuba license token [within the app](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/App.js#L14).
3. Click on ```ios/vesdkreactnativeintegrationsample.xcworkspace```. Open **Signing & Capabilities** tab in Target settings and select your Development Team.
4. Run command ```npm run ios``` in terminal to launch the sample on simulator ```iPhone 11``` or choose device or specific simulator in XCode to launch the sample.
5. Follow [iOS Integration Guide](mddocs/ios_integration.md) to integrate Video Editor SDK into your React Native Expo project.


