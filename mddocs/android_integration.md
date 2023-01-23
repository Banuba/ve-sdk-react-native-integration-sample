# Android Integration Guide into React Native Expo project

An integration and customization of Banuba Video Editor SDK is implemented in **android** directory
of your React Native Expo project using native Android development process.

## Basic
The following steps help to complete basic integration into your React Native Expo project.

<ins>All changes are made in **android** directory.</ins>

:exclamation: The license token **IS REQUIRED** to run sample and an integration in your app.  
Please follow [Installation](../README.md#Installation) guide if the license token is not set<br></br>

1. __Add Banuba Video Editor SDK dependencies__ </br>
   Add Banuba repositories in main gradle file to get Video Editor SDK and AR Cloud dependencies.
    ```groovy
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Banuba/banuba-ve-sdk")
            credentials {
                username = "Banuba"
                password = ""
            }
        }
        maven {
            name = "ARCloudPackages"
            url = uri("https://github.com/Banuba/banuba-ar")
            credentials {
                username = "Banuba"
                password = ""
            }
        }
    ```
   [See example](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/android/build.gradle#L41)</br><br>

   Add Video Editor SDK dependencies in app gradle file.
```groovy
    // Banuba Video Editor SDK dependencies
    def banubaSdkVersion = '1.26.3'
    implementation "com.banuba.sdk:ffmpeg:4.4"
    implementation "com.banuba.sdk:camera-sdk:${banubaSdkVersion}"
    implementation "com.banuba.sdk:camera-ui-sdk:${banubaSdkVersion}"
    implementation "com.banuba.sdk:core-sdk:${banubaSdkVersion}"
    implementation "com.banuba.sdk:core-ui-sdk:${banubaSdkVersion}"
    implementation "com.banuba.sdk:ve-flow-sdk:${banubaSdkVersion}"
    implementation "com.banuba.sdk:ve-timeline-sdk:${banubaSdkVersion}"
    implementation "com.banuba.sdk:ve-sdk:${banubaSdkVersion}"
    implementation "com.banuba.sdk:ve-ui-sdk:${banubaSdkVersion}"
    implementation "com.banuba.sdk:ve-gallery-sdk:${banubaSdkVersion}"
    implementation "com.banuba.sdk:ve-effects-sdk:${banubaSdkVersion}"
    implementation "com.banuba.sdk:effect-player-adapter:${banubaSdkVersion}"
    implementation "com.banuba.sdk:ar-cloud:${banubaSdkVersion}"
    implementation "com.banuba.sdk:ve-audio-browser-sdk:${banubaSdkVersion}"
    implementation "com.banuba.sdk:banuba-token-storage-sdk:${banubaSdkVersion}"
    implementation "com.banuba.sdk:ve-export-sdk:${banubaSdkVersion}"
    implementation "com.banuba.sdk:ve-playback-sdk:${banubaSdkVersion}"
   ```
[See example](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/android/app/build.gradle#L228)</br><br>

2. __Add SDK dependencies initializer class__ </br>
   Add [BanubaVideoEditorSDK.kt](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/android/app/src/main/java/com/vesdkreactnativeintegrationsample/BanubaVideoEditorSDK.kt) file.</br>
   This class helps to initialize dependencies and customize Video Editor SDK.</br><br>

3. __Add Video Editor React Package__ </br>
   Add [VideoEditorModule](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/android/app/src/main/java/com/vesdkreactnativeintegrationsample/VideoEditorModule.kt) and
   create new ReactPackage for Video Editor and add it in ```List<ReactPackage> getPackages()``` method in your [Application](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/android/app/src/main/java/com/vesdkreactnativeintegrationsample/MainApplication.java#L54).<br>
   [See example](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/android/app/src/main/java/com/vesdkreactnativeintegrationsample/VideoEditorReactPackage.kt)</br><br>

4. __Initialize Video Editor SDK__ </br>
   Use ```val videoEditor = BanubaVideoEditor.initialize(LICENSE_TOKEN)``` in your ```VideoEditorModule```.</br>
   [See example](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/master/android/app/src/main/java/com/vesdkreactnativecliintegrationsample/VideoEditorModule.kt#L95)</br><br>

5. __Update AndroidManifest.xml__ </br>
   Add ```VideoCreationActivity``` in your AndroidManifest.xml file. The Activity is used to bring together a number of screens in a certain flow.</br>
   [See example](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/android/app/src/main/AndroidManifest.xml#L62)</br><br>

6. __Add Network settings__ </br>
   Add permissions into [AndroidManifest.xml](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/android/app/src/main/AndroidManifest.xml)
     ```xml
       <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
       <uses-permission android:name="android.permission.INTERNET" />
     ```
   and use ```android:usesCleartextTraffic="true"``` to allow network traffic for downloading effects from AR Cloud and stickers from [Giphy](https://giphy.com/).</br>
   Please set up correctly [network security config](https://developer.android.com/training/articles/security-config) and use of ```android:usesCleartextTraffic``` based on this [doc](https://developer.android.com/guide/topics/manifest/application-element).<br></br>

7. __Add assets and resources__</br>
    1. [bnb-resources](https://github.com/Banuba/ve-sdk-react-native-integration-sample/tree/main/android/app/src/main/assets/bnb-resources) to use built-in Banuba AR and Lut effects.
       Using Banuba AR ```assets/bnb-resources/effects``` requires [Face AR product](https://docs.banuba.com/face-ar-sdk-v1). Please contact Banuba Sales managers to get more AR effects.<br></br>

    2. [color](https://github.com/Banuba/ve-sdk-react-native-integration-sample/tree/main/android/app/src/main/res/color),
       [drawable](https://github.com/Banuba/ve-sdk-react-native-integration-sample/tree/main/android/app/src/main/res/drawable),
       [drawable-hdpi](https://github.com/Banuba/ve-sdk-react-native-integration-sample/tree/main/android/app/src/main/res/drawable-hdpi),
       [drawable-ldpi](https://github.com/Banuba/ve-sdk-react-native-integration-sample/tree/main/android/app/src/main/res/drawable-ldpi),
       [drawable-mdpi](https://github.com/Banuba/ve-sdk-react-native-integration-sample/tree/main/android/app/src/main/res/drawable-mdpi),
       [drawable-xhdpi](https://github.com/Banuba/ve-sdk-react-native-integration-sample/tree/main/android/app/src/main/res/drawable-xhdpi),
       [drawable-xxhdpi](https://github.com/Banuba/ve-sdk-react-native-integration-sample/tree/main/android/app/src/main/res/drawable-xxhdpi),
       [drawable-xxxhdpi](https://github.com/Banuba/ve-sdk-react-native-integration-sample/tree/main/android/app/src/main/res/drawable-xxxhdpi) are visual assets used in views and added in the sample for simplicity. You can use your specific assets.<br></br>

    3. [values](https://github.com/Banuba/ve-sdk-react-native-integration-sample/tree/main/android/app/src/main/res/values) to use colors and themes. Theme ```VideoCreationTheme``` and its styles use resources in **drawable** and **color** directories.<br></br>

8. __Start Video Editor SDK__ </br>
   Use ```startAndroidVideoEditor()``` method defined in ```App.js``` to start Video Editor from React Native on Android.</br>
   ```
   function initVideoEditor() {
         VideoEditorModule.initVideoEditor(LICENSE_TOKEN);
   }
   
   async function startAndroidVideoEditor() {
         initVideoEditor();
         return await VideoEditorModule.openVideoEditor();
   }
       
   <Button
            title = "Open Video Editor"
            onPress={async () => {
                if (Platform.OS === 'android') {
                    startAndroidVideoEditor().then(videoUri => {
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
   [See example](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/App.js#L99)</br><br>
   Technically it invokes ```VideoCreationActivity.startFromCamera(...)``` method to start Video Editor SDK from Camera screen.</br>
   [See example](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/master/android/app/src/main/java/com/vesdkreactnativeintegrationsample/VideoEditorModule.kt#L113)</br><br>

   Since Video Editor SDK on Android is launched within ```VideoCreationActivity``` exported video is returned from the Activity into ```onActivityResult``` callback
   in [VideoEditorModule](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/master/android/app/src/main/java/com/vesdkreactnativeintegrationsample/VideoEditorModule.kt#44).</br><br>

   [Promises](https://reactnative.dev/docs/native-modules-android#promises) is used to make a bridge between Android and JS.<br>
   Export returns ```videoUri``` path as a String value were exported video stored on JS side.  
   [See example](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/App.js#L100)<br></br>

9. __Configure export__</br>
   You can set custom export video file name using ```ExportParams.Builder.fileName()``` method.<br>
   [See example](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/android/app/src/main/java/com/vesdkreactnativeintegrationsample/BanubaVideoEditorSDK.kt#L232).<br></br>

10. __Custom Audio Browser experience__ </br>
    Video Editor SDK allows to implement your experience of providing audio tracks for your users - custom Audio Browser.  
    To check out the simplest experience on Flutter you can set ```true``` to [USE_CUSTOM_AUDIO_BROWSER](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/android/app/src/main/java/com/vesdkreactnativeintegrationsample/MainApplication.java#L30)  
    :exclamation:<ins>Video Editor SDK can play only files stored on device.</ins>

## What is next?

We have covered a basic process of integration Video Editor SDK into your React Native Expo project.</br>
More integration details and customization options you will find in [Banuba Video Editor SDK Android Integration Sample](https://github.com/Banuba/ve-sdk-android-integration-sample).