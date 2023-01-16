import { StatusBar } from "expo-status-bar";
import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  Button,
  View,
  Platform,
  NativeModules,
} from "react-native";
const { VideoEditorModule } = NativeModules;

async function startIosVideoEditor() {
  return await VideoEditorModule.openVideoEditor();
}

async function startIosVideoEditorPIP() {
  return await VideoEditorModule.openVideoEditorPIP();
}

async function startIosVideoEditorTrimmer() {
  return await VideoEditorModule.openVideoEditorTrimmer();
}

async function startAndroidVideoEditor() {
  return await VideoEditorModule.openVideoEditor();
}

async function startAndroidVideoEditorPIP() {
  return await VideoEditorModule.openVideoEditorPIP();
}

async function startAndroidVideoEditorTrimmer() {
  return await VideoEditorModule.openVideoEditorTrimmer();
}

function initLicenseManager() {
    VideoEditorModule.initLicenseManager(SET YOUR LICENSE TOKEN);
}

export default class App extends Component {
  static errEditorNotInitialized = "ERR_VIDEO_EDITOR_NOT_INITIALIZED"
  static errEditorLicenseRevoked = "ERR_VIDEO_EDITOR_LICENSE_REVOKED"

  constructor() {
    super()
    initLicenseManager()
    this.state = {
      errorText: ''
    }
  }

  handleExportException(e) {
    var message = '';
    switch (e.code) {
      case App.errEditorNotInitialized:
        message = 'Banuba Video Editor SDK is not initialized: license token is unknown or incorrect.\nPlease check your license token or contact Banuba';
        break;
      case App.errEditorLicenseRevoked:
        message = 'License is revoked or expired. Please contact Banuba https://www.banuba.com/faq/kb-tickets/new';
        break;
      default:
        message = '';
        console.log(
          "Banuba " + Platform.OS.toUpperCase() + " Video Editor export video failed = " + e
        );
        break;
    }
    this.setState({ errorText: message });
  }

  render() {
    return (
      <View style={styles.container}>
        <Text style={{ padding: 16, textAlign: 'center' }}>
          Sample integration of Banuba Video Editor into React Native Expo project
        </Text>

        <Text style={{ padding: 16, textAlign: 'center', color: '#ff0000', fontSize: 16, fontWeight: "800" }}>
          {this.state.errorText}
        </Text>

        <View style={{ marginVertical: 8 }}>
          <Button
            title="Open Video Editor - Default"
            onPress={async () => {
              if (Platform.OS === 'android') {
                startAndroidVideoEditor()
                  .then((videoUri) => {
                    console.log(
                      "Banuba Android Video Editor export video completed successfully. Video uri = " +
                      videoUri
                    );
                  })
                  .catch((e) => {
                    this.handleExportException(e);
                  });
              } else {
                startIosVideoEditor()
                  .then((response) => {
                    const exportedVideoUri = response?.videoUri;
                    console.log(
                      "Banuba iOS Video Editor export video completed successfully. Video uri = " +
                      exportedVideoUri
                    );
                  })
                  .catch((e) => {
                    this.handleExportException(e);
                  });
              }
            }} />
        </View>

        <View style={{ marginVertical: 8 }}>
          <Button
            title="Open Video Editor - PIP"
            color="#00ab41"
            onPress={async () => {
              if (Platform.OS === 'android') {
                startAndroidVideoEditorPIP()
                  .then(videoUri => {
                    console.log(
                      'Banuba Android Video Editor export video completed successfully. Video uri = ' +
                      videoUri
                    );
                  })
                  .catch(e => {
                    this.handleExportException(e);
                  });
              } else {
                startIosVideoEditorPIP()
                  .then(response => {
                    const exportedVideoUri = response?.videoUri;
                    console.log(
                      'Banuba iOS Video Editor export video completed successfully. Video uri = ' +
                      exportedVideoUri
                    );
                  })
                  .catch(e => {
                    this.handleExportException(e);
                  });
              }
            }} />
        </View>

        <View style={{ marginVertical: 8 }}>
          <Button
            title="Open Video Editor - Trimmer"
            color="#ff0000"
            onPress={async () => {
              if (Platform.OS === 'android') {
                startAndroidVideoEditorTrimmer()
                  .then(videoUri => {
                    console.log(
                      'Banuba Android Video Editor export video completed successfully. Video uri = ' +
                      videoUri
                    );
                  })
                  .catch(e => {
                    this.handleExportException(e);
                  });
              } else {
                startIosVideoEditorTrimmer()
                  .then(response => {
                    const exportedVideoUri = response?.videoUri;
                    console.log(
                      'Banuba iOS Video Editor export video completed successfully. Video uri = ' +
                      exportedVideoUri
                    );
                  })
                  .catch(e => {
                    this.handleExportException(e);
                  });
              }
            }} />
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
    marginVertical: 24,
    alignItems: "center",
    justifyContent: "center",
  },
});
