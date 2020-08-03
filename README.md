# react-native-ffmpeg-media-metadata-retriever

Wraps FFmpegMediaMetadataRetriever for React Native.

## Installation

```sh
npm install react-native-ffmpeg-media-metadata-retriever
```

### Android

- in `android/app/build.gradle`:
```
dependencies {
    ...
+   implementation project(':reactnativeffmpegmediametadataretriever')
}
```

- in `android/settings.gradle`
```
+ include ':reactnativeffmpegmediametadataretriever'
+ project(':reactnativeffmpegmediametadataretriever').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-ffmpeg-media-metadata-retriever/android')
```

### iOS

Currently, iOS is not supported.

### Windows

Currently, Windows is not supported.

## Usage

```js
import fs from 'react-native-fs';
import readdirp from 'react-native-readdirp';
import FfmpegMediaMetadataRetriever from 'react-native-ffmpeg-media-metadata-retriever';

const uri = fs.ExternalStorageDirectoryPath + '/Music';
readdirp(uri).forEach(async (file) => {
  const metadata = await FfmpegMediaMetadataRetriever.getMetadata(
    file.path,
    ['title', 'artist']
  ));
  console.log(`${metadata.artist} - ${metadata.title}`);
});
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
