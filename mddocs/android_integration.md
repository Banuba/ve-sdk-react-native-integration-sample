# Android Integration Guide into React Native Expo project

An integration and customization of Banuba VE UI SDK is implemented in **android** directory
of your React Native Expo project using native Android development process.

## Basic
The following steps help to complete basic integration into your React Native Expo project.

<ins>All changes are made in **android** directory.</ins>
1. __Add Banuba SDK dependencies__ </br>
   Add Banuba repositories in main gradle file to get VE SDK and AR Cloud dependencies.
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

   Add VE UI SDK dependencies in app gradle file.
```groovy
    // Banuba Video Editor SDK dependencies
    def banubaSdkVersion = '1.24.2'
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
[See example](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/android/app/build.gradle#L227)</br><br>

2. __Add SDK Initializer class__ </br>
   Add [BanubaVideoEditorUISDK.kt](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/android/app/src/main/java/com/vesdkreactnativeintegrationsample/BanubaVideoEditorUISDK.kt) file.</br>
   This class helps to initialize and customize VE UI SDK.</br><br>

3. __Initialize the SDK in your application__ </br>
   Use ```new BanubaVideoEditorUISDK().initialize()``` in your ```Application.onCreate()``` method to initialize the SDK.</br>
   [See example](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/android/app/src/main/java/com/vesdkreactnativeintegrationsample/MainApplication.java#L99)</br><br>

4. __Add Video Editor React Package__ </br>
   Create new ReactPackage for VideoEditor and add it in ```List<ReactPackage> getPackages()``` method in your [Application](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/android/app/src/main/java/com/vesdkreactnativeintegrationsample/MainApplication.java#L51).<br>
   [See example](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/android/app/src/main/java/com/vesdkreactnativeintegrationsample/VideoEditorReactPackage.kt)</br><br>

5. __Update AndroidManifest.xml__ </br>
   Add ```VideoCreationActivity``` in your AndroidManifest.xml file. The Activity is used to bring together a number of screens in a certain flow.</br>
   [See example](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/android/app/src/main/AndroidManifest.xml#L62)</br><br>

6. __Add assets and resources__</br>
    1. [bnb-resources](https://github.com/Banuba/ve-sdk-react-native-integration-sample/tree/main/android/app/src/main/assets/bnb-resources) to use hardcoded Banuba AR and Lut effects.
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

7. __Start the SDK__ </br>
   Use ```VideoCreationActivity.startFromCamera(...)``` method to start VE UI SDK from Camera screen.
   Once the SDK starts you can observe export video results.</br>
   [See example](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/android/app/src/main/java/com/vesdkreactnativeintegrationsample/VideoEditorReactPackage.kt#L94)</br><br>

8. __Configure export__</br>
   You can set custom export video file name using ```ExportParams.Builder.fileName()``` method.<br>
   [See example](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/android/app/src/main/java/com/vesdkreactnativeintegrationsample/BanubaVideoEditorUISDK.kt#L232).<br></br>

   Since VE SDK is launched within ```VideoCreationActivity``` exported video is returned from the Activity into ```onActivityResult``` callback
   in [VideoEditorModule](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/android/app/src/main/java/com/vesdkreactnativeintegrationsample/VideoEditorReactPackage.kt#L28).</br><br>

   [Promises](https://reactnative.dev/docs/native-modules-android#promises) is used to make a bridge between Android and JS.<br>
   [See example](https://github.com/Banuba/ve-sdk-react-native-integration-sample/blob/main/App.js#L39)
   how to get exported video uri as a String value on JS side..<br></br>
   You can configure other data passed from ```VideoEditorReactPackage``` to JS depends on your requirements.<br></br>

## What is next?

We have covered a basic process of integration VE UI SDK into your React Native expo project.</br>
More integration details and customizations you will find in [VE UI SDK Android Integration Sample](https://github.com/Banuba/ve-sdk-android-integration-sample).