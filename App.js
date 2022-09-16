import { StatusBar } from 'expo-status-bar';
import React from 'react';
import { StyleSheet,
   Text,
    Button,
     View,
      Platform,
       Alert,
        NativeModules
       } from 'react-native';
const { VideoEditorModule } = NativeModules;

async function startIosVideoEditor() {
  return await VideoEditorModule.openVideoEditor();
};

async function startAndroidVideoEditor() {
  return await VideoEditorModule.openVideoEditor();
}

export default function App() {
  return (
      <View style={styles.container}>
        <Text style={{padding: 16, textAlign: 'center'}}>Sample integration of Banuba Video Editor into React Native Expo project</Text>

        <Button
            title = "Open Video Editor"

            onPress={async () => {
                if (Platform.OS === 'android') {
                    startAndroidVideoEditor().then(videoUri => {
                        console.log('Banuba Android Video Editor export video completed successfully. Video uri = ' + videoUri)
                    }).catch(e => {
                        console.log('Banuba Android Video Editor export video failed = ' + e)
                    })
                } else {
                    startIosVideoEditor().then(response => {
                        const exportedVideoUri = response?.videoUri;
                        console.log('Banuba iOS Video Editor export video completed successfully. Video uri = ' + exportedVideoUri)
                    }).catch(e => {
                        console.log('Banuba iOS Video Editor export video failed = ' + e)
                    })
                }
              }
            }
        />
      </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    marginVertical: 16,
    alignItems: 'center',
    justifyContent: 'center',
  }
});
