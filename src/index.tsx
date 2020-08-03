import { NativeModules } from 'react-native';

interface Metadata {
  album?: string;
  albumArtist?: string;
  artist?: string;
  audioCodec?: string;
  comment?: string;
  composer?: string;
  copyright?: string;
  creationTime?: string;
  date?: string;
  disc?: string;
  duration?: number;
  encodedBy?: string;
  encoder?: string;
  filename?: string;
  filesize?: number;
  framerate?: number;
  genre?: string;
  icyMetadata?: string;
  language?: string;
  performer?: string;
  publisher?: string;
  serviceName?: string;
  title?: string;
  track?: string;
  variantBitrate?: number;
  videoCodec?: string;
  videoRotation?: string;
}

type metadatum = keyof Metadata;

type FfmpegMediaMetadataRetrieverType = {
  getMetadata(uri: string, requiredMetadata: metadatum[]): Promise<Metadata>;
};

const { FfmpegMediaMetadataRetriever } = NativeModules;

export default FfmpegMediaMetadataRetriever as FfmpegMediaMetadataRetrieverType;
