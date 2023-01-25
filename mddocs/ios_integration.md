# iOS integration guide into React Native Expo project

The following guide covers basic integration process into your React Native Expo project
where required part of an integration and customization of Banuba Video Editor SDK is implemented in **ios** directory
of your React Native Expo project using native iOS development process.  

### Prerequisite
:exclamation: The license token **IS REQUIRED** to run sample and an integration into your app.  
Please follow [Installation](../README.md#Installation) guide if the license token is not set.

### Add SDK dependencies
Add iOS Video Editor SDK dependencies to your [Podfile](../ios/Podfile)

### Add Bridge between React Native and iOS  
Add [vesdkreactnativeintegrationsample-Bridging-Header.h](../ios/vesdkreactnativeintegrationsample-Bridging-Header.h) 
and [VideoEditorModuleBridge.m](../ios/VideoEditorModuleBridge.m) files for communication between React Native and iOS.

### Add SDK integration module  
Add [VideoEditorModule.swift](../ios/VideoEditorModule.swift) file
to initialize SDK dependencies. This class also allows you to customize many Video Editor SDK features i.e. min/max video durations, export flow, order of effects and others.

### Add resources
Video Editor SDK uses a lot of resources required for running.  
Please make sure all these resources are provided in your project.
1. [bundleEffects](../ios/vesdkreactnativeintegrationsample/bundleEffects) to use built-in Banuba AR effects. Using Banuba AR requires [Face AR product](https://docs.banuba.com/face-ar-sdk-v1). Please contact Banuba Sales managers to get more AR effects.
2. [luts](../ios/vesdkreactnativeintegrationsample/luts) to use Lut effects shown in the Effects tab.

### Start SDK
First, initialize Video Editor SDK using license token in ```VideoEditorModule``` on iOS.
```swift
let videoEditor = BanubaVideoEditor(
        token: token,
        ...
      )
```
Please note, instance ```videoEditor``` can be **nil** if the license token is incorrect.  
[See example](../ios/VideoEditorModule.swift#L36)

Next, to start Video Editor SDK from React Native use ```startIosVideoEditor()``` method defined in [App.js](../App.js#L110).
It will open Video Editor SDK from camera screen.

Use ```startIosVideoEditor()``` method defined in ```App.js``` to start Video Editor from React Native on iOS.</br>
```javascript
   function initVideoEditor() {
      VideoEditorModule.initVideoEditor(LICENSE_TOKEN);
   }
   
   async function startIosVideoEditor() {
      initVideoEditor();
      return await VideoEditorModule.openVideoEditor();
   };
       
   <Button title = "Open Video Editor" onPress={async () => {
                    if (Platform.OS === 'ios') {
                        startIosVideoEditor().then(response => {
                          const exportedVideoUri = response?.videoUri;
                          // Handle received exported video
                        }).catch(e => {
                          // Handle error
                        })
                    } 
                  }
                }
   />
 ```
Export returns [videoUri](../App.js#L112) path as a ```String``` value where exported video stored to ReactNative.

### Enable custom Audio Browser experience
Video Editor SDK allows to implement your experience of providing audio tracks for your users - custom Audio Browser.  
To check out the simplest experience you can set ```true``` to [useCustomAudioBrowser](../ios/AppDelegate.swift#L17)  
:exclamation:<ins>Video Editor SDK can play only files stored on device.</ins>

## What is next?
We have covered a basic process of Video Editor SDK integration into your React Native Expo project.</br>
More details and customization options you will find in native [Banuba Video Editor SDK iOS Integration Sample](https://github.com/Banuba/ve-sdk-ios-integration-sample).