import { StatusBar } from 'expo-status-bar';
import React from 'react';
const { VideoEditorModule } = NativeModules;
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
          VideoEditorModule.openVideoEditor(
              (url) => {
                console.log(`url ${url} returned`);
              }
          );
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
