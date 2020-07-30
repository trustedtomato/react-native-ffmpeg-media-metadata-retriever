import { NativeModules } from 'react-native';

type FfmpegMediaMetadataRetrieverType = {
  multiply(a: number, b: number): Promise<number>;
};

const { FfmpegMediaMetadataRetriever } = NativeModules;

export default FfmpegMediaMetadataRetriever as FfmpegMediaMetadataRetrieverType;
