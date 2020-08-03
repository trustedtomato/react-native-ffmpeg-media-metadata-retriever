import * as React from 'react';
import { StyleSheet, View, Text } from 'react-native';
import fs from 'react-native-fs';
import readdirp from 'react-native-readdirp';
import FfmpegMediaMetadataRetriever from 'react-native-ffmpeg-media-metadata-retriever';
import { PermissionsAndroid } from 'react-native';

const requestStorageAccess = async () => {
  const granted = await PermissionsAndroid.request(
    PermissionsAndroid.PERMISSIONS.READ_EXTERNAL_STORAGE,
    {
      title: 'Storage access permission',
      message: 'To use the storage, storage access is required.',
      buttonPositive: 'OK',
    }
  );
  if (granted === PermissionsAndroid.RESULTS.GRANTED) {
    return true;
  } else {
    throw new Error('Not granted!');
  }
};

export default function App() {
  const [result, setResult] = React.useState<string | undefined>();

  React.useEffect(() => {
    requestStorageAccess().then(async () => {
      const uri = fs.ExternalStorageDirectoryPath + '/Music';
      await readdirp(uri).forEach(async (file) => {
        const metadata = await FfmpegMediaMetadataRetriever.getMetadata(
          file.path,
          ['title', 'artist']
        ).catch((err) => ({
          artist: 'Error',
          title: err.message,
        }));
        setResult(`${metadata.artist} - ${metadata.title}`);
      });
      setResult('Finished!');
    });
  }, []);

  return (
    <View style={styles.container}>
      <Text>Result: {result}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
});
