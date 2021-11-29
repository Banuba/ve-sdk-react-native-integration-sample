import { StatusBar } from 'expo-status-bar';
import React from 'react';
const { VideoEditorModule } = NativeModules;

const openEditor = (): Promise<{ videoUri: string } | null> => {
  return VideoEditorModule.openVideoEditor();
};

export const openVideoEditor = async (): Promise<string | null> => {
  const response = await openEditor();

  if (!response) {
    return null;
  }

  return response?.videoUri;
};

import { StyleSheet,
   Text,
    Button,
     View,
      Platform,
       Alert,
        NativeModules
       } from 'react-native';

export default function App() {
  return (
    <View style={styles.container}>
      <Text>Sample integration of Banuba Video Editor</Text>
      
      <Button 
      title="Open Video Editor"
      onPress={() => {
        if (Platform.OS === 'android') {
          NativeModules.ActivityStarter.navigateToVideoEditor()
        } else {
            const videoUri = openVideoEditor();
            console.log(videoUri)
        }
        }
      }
      />
      <StatusBar style="auto" />
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
