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

// Set Banuba license token for Video Editor SDK
const LICENSE_TOKEN = SET LICENSE TOKEN

const ERR_SDK_NOT_INITIALIZED_CODE = 'ERR_VIDEO_EDITOR_NOT_INITIALIZED';
const ERR_SDK_NOT_INITIALIZED_MESSAGE = 'Banuba Video Editor SDK is not initialized: license token is unknown or incorrect.\nPlease check your license token or contact Banuba';

const ERR_LICENSE_REVOKED_CODE = 'ERR_VIDEO_EDITOR_LICENSE_REVOKED';
const ERR_LICENSE_REVOKED_MESSAGE = 'License is revoked or expired. Please contact Banuba https://www.banuba.com/faq/kb-tickets/new';

function initVideoEditor() {
  VideoEditorModule.initVideoEditor(LICENSE_TOKEN);
}

async function startIosVideoEditor() {
  initVideoEditor();
  return await VideoEditorModule.openVideoEditor();
}

async function startIosVideoEditorPIP() {
  initVideoEditor();
  return await VideoEditorModule.openVideoEditorPIP();
}

async function startIosVideoEditorTrimmer() {
  initVideoEditor();
  return await VideoEditorModule.openVideoEditorTrimmer();
}

async function startAndroidVideoEditor() {
  initVideoEditor();
  return await VideoEditorModule.openVideoEditor();
}

async function startAndroidVideoEditorVideoOnly() {
  initVideoEditor();
  return await VideoEditorModule.openVideoEditorVideoOnly();
}

async function startAndroidVideoEditorPhotoOnly() {
  initVideoEditor();
  return await VideoEditorModule.openVideoEditorPhotoOnly();
}

async function startAndroidVideoEditorPIP() {
  initVideoEditor();
  return await VideoEditorModule.openVideoEditorPIP();
}

async function startAndroidVideoEditorTrimmer() {
  initVideoEditor();
  return await VideoEditorModule.openVideoEditorTrimmer();
}

export default class App extends Component {
  constructor() {
    super()
    this.state = {
      errorText: ''
    }
  }

  handleExportException(e) {
    var message = '';
    switch (e.code) {
      case ERR_SDK_NOT_INITIALIZED_CODE:
        message = ERR_SDK_NOT_INITIALIZED_MESSAGE;
        break;
        case ERR_LICENSE_REVOKED_CODE:
          message = ERR_LICENSE_REVOKED_MESSAGE;
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

        <View style={{ marginVertical: 8 }}>
          <Button
            title="Open Video Editor Video Only"
            color="#a8328b"
            onPress={async () => {
              if (Platform.OS === 'android') {
                startAndroidVideoEditorVideoOnly()
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

              }
            }} />
        </View>

        <View style={{ marginVertical: 8 }}>
          <Button
            title="Open Video Editor Photo Only"
            color="#32a8a4"
            onPress={async () => {
              if (Platform.OS === 'android') {
                startAndroidVideoEditorPhotoOnly()
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
