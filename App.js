import { StatusBar } from "expo-status-bar";
import React from "react";
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

export default function App() {
  return (
    <View style={styles.container}>
      <Text style={{ padding: 16, textAlign: "center" }}>
        Sample integration of Banuba Video Editor into React Native Expo project
      </Text>

      <View style={{ marginVertical: 16, width: 240 }}>
        <Button
          title="Open Video Editor - Default"
          onPress={async () => {
            if (Platform.OS === "android") {
              startAndroidVideoEditor()
                .then((videoUri) => {
                  console.log(
                    "Banuba Android Video Editor export video completed successfully. Video uri = " +
                      videoUri
                  );
                })
                .catch((e) => {
                  console.log(
                    "Banuba Android Video Editor export video failed = " + e
                  );
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
                  console.log(
                    "Banuba iOS Video Editor export video failed = " + e
                  );
                });
            }
          }}
        />
      </View>

      <View style={{ marginVertical: 16, width: 240 }}>
        <Button
          title="Open Video Editor - PIP"
          color="#00ab41"
          onPress={async () => {
            if (Platform.OS === "android") {
              startAndroidVideoEditorPIP()
                .then((videoUri) => {
                  console.log(
                    "Banuba Android Video Editor export video completed successfully. Video uri = " +
                      videoUri
                  );
                })
                .catch((e) => {
                  console.log(
                    "Banuba Android Video Editor export video failed = " + e
                  );
                });
            } else {
              startIosVideoEditorPIP()
                .then((response) => {
                  const exportedVideoUri = response?.videoUri;
                  console.log(
                    "Banuba iOS Video Editor export video completed successfully. Video uri = " +
                      exportedVideoUri
                  );
                })
                .catch((e) => {
                  console.log(
                    "Banuba iOS Video Editor export video failed = " + e
                  );
                });
            }
          }}
        />
      </View>

      <View style={{ marginVertical: 16, width: 240 }}>
        <Button
          title="Open Video Editor - Trimmer"
          color="#FF0000"
          onPress={async () => {
            if (Platform.OS === "android") {
              startAndroidVideoEditorTrimmer()
                .then((videoUri) => {
                  console.log(
                    "Banuba Android Video Editor export video completed successfully. Video uri = " +
                      videoUri
                  );
                })
                .catch((e) => {
                  console.log(
                    "Banuba Android Video Editor export video failed = " + e
                  );
                });
            } else {
              startIosVideoEditorTrimmer()
                .then((response) => {
                  const exportedVideoUri = response?.videoUri;
                  console.log(
                    "Banuba iOS Video Editor export video completed successfully. Video uri = " +
                      exportedVideoUri
                  );
                })
                .catch((e) => {
                  console.log(
                    "Banuba iOS Video Editor export video failed = " + e
                  );
                });
            }
          }}
        />
      </View>
    </View>
  );
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
