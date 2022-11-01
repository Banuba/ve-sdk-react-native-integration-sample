# iOS Integration Guide into React Native Expo project

An integration and customization of Banuba Video Editor SDK is implemented in **ios** directory
of your React Native Expo project using native iOS development process.

## Basic
The following steps help to complete basic integration into your React Native Expo project.

:exclamation: **Important:** Please before run ```sudo arch -x86_64 gem install ffi``` in terminal for Apple M-series chip based on ARM architecture.


<ins>All changes are made in **ios** directory.</ins>
1. __Set Banuba Video Editor SDK token__  
   Set the token in [VideoEditorModule](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/ios/VideoEditorModule.swift#L114)<br></br>
   To get access to your trial, please, get in touch with us by [filling a form](https://www.banuba.com/video-editor-sdk) on our website. Our sales managers will send you the trial token.<br>
   :exclamation: The token **IS REQUIRED** to run sample and an integration in your app.<br></br>

2. __Add Banuba SDK dependencies__  
   Add iOS Video Editor SDK dependencies to your Podfile.</br>
   [See example](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/master/ios/Podfile).</br><br>

3. __Setup React Native and iOS platform Bridge__  
   Add [vesdkreactnativeintegrationsample-Bridging-Header.h](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/master/ios/vesdkreactnativeintegrationsample-Bridging-Header.h) and [VideoEditorModuleBridge.m](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/master/ios/VideoEditorModuleBridge.m) files .</br>
   These files help to start Video Editor SDK from React Native.</br><br>

4. __Add SDK Initializer class__  
   Add [VideoEditorModule.swift](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/master/ios/VideoEditorModule.swift) file to your project.
   This class helps to initialize and customize Banuba Video Editor SDK.</br><br>

5. __Add assets and resources__
    1. [bundleEffects](https://github.com/Banuba/ve-sdk-react-native-integration-sample/tree/main/ios/vesdkreactnativeintegrationsample/bundleEffects) to use built-in Banuba AR effects. Using Banuba AR requires [Face AR product](https://docs.banuba.com/face-ar-sdk-v1). Please contact Banuba Sales managers to get more AR effects.
    2. [luts](https://github.com/Banuba/ve-sdk-react-native-integration-sample/tree/main/ios/vesdkreactnativeintegrationsample/luts) to use Lut effects shown in the Effects tab.</br><br>

6. __Start Video Editor SDK__  
   Use ```startIosVideoEditor()``` method defined in ```App.js``` to start Video Editor from React Native on iOS.</br>
   ```
       async function startIosVideoEditor() {
             return await VideoEditorModule.openVideoEditor();
      };
       
       <Button
                title = "Open Video Editor"
                onPress={async () => {
                    if (Platform.OS === 'ios') {
                        startIosVideoEditor().then(response => {
                          const exportedVideoUri = response?.videoUri;
                          // Handle received exported video
                        }).catch(e => {
                          // Handle error
                        })
                    } else {
                       ...
                    }
                  }
                }
            />
   ```
   Export returns response where you can find ```videoUri``` the path were exported video stored.</br>
   [See example](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/App.js#L47)</br>


## What is next?

We have covered a basic process of Video Editor SDK integration into your React Native Expo project.</br>
More details and customization options you will find in native [Banuba Video Editor SDK iOS Integration Sample](https://github.com/Banuba/ve-sdk-ios-integration-sample).