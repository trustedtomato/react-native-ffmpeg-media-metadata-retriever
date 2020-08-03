package com.reactnativeffmpegmediametadataretriever

import com.facebook.react.bridge.*
import wseemann.media.FFmpegMediaMetadataRetriever

// Metadatum: What we get from the user
// MetadatumHandler: What handles the metadatum
// MetadataKey: What we send to extractMetadata()
// RawMetadatumValue: What

sealed class ParsedMetadatumValue
data class StringMetadatum(val string: String): ParsedMetadatumValue()
data class IntMetadatum(val int: Int): ParsedMetadatumValue()

data class TextMetadatumHandler(val metadataKey: String, val metadatumValueHandler: (metadatumValue: String) -> ParsedMetadatumValue)

class FfmpegMediaMetadataRetrieverModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return "FfmpegMediaMetadataRetriever"
    }

    fun textMetadatumToTextMetadatumHandler(str: String?): TextMetadatumHandler? {
      return when (str) {
        "album" -> TextMetadatumHandler(FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM) { StringMetadatum(str) }
        "albumArtist" -> TextMetadatumHandler(FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM_ARTIST) { StringMetadatum(it) }
        "artist" -> TextMetadatumHandler(FFmpegMediaMetadataRetriever.METADATA_KEY_ARTIST) { StringMetadatum(it) }
        "audioCodec" -> TextMetadatumHandler(FFmpegMediaMetadataRetriever.METADATA_KEY_AUDIO_CODEC) { StringMetadatum(it) }
        "comment" -> TextMetadatumHandler(FFmpegMediaMetadataRetriever.METADATA_KEY_COMMENT) { StringMetadatum(it) }
        "composer" -> TextMetadatumHandler(FFmpegMediaMetadataRetriever.METADATA_KEY_COMPOSER) { StringMetadatum(it) }
        "copyright" -> TextMetadatumHandler(FFmpegMediaMetadataRetriever.METADATA_KEY_COPYRIGHT) { StringMetadatum(it) }
        "creationTime" -> TextMetadatumHandler(FFmpegMediaMetadataRetriever.METADATA_KEY_CREATION_TIME) { StringMetadatum(it) }
        "date" -> TextMetadatumHandler(FFmpegMediaMetadataRetriever.METADATA_KEY_DATE) { StringMetadatum(it) }
        "disc" -> TextMetadatumHandler(FFmpegMediaMetadataRetriever.METADATA_KEY_DISC) { StringMetadatum(it) }
        "duration" -> TextMetadatumHandler(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION) { IntMetadatum(it.toInt()) }
        "encodedBy" -> TextMetadatumHandler(FFmpegMediaMetadataRetriever.METADATA_KEY_ENCODED_BY) { StringMetadatum(it) }
        "encoder" -> TextMetadatumHandler(FFmpegMediaMetadataRetriever.METADATA_KEY_ENCODER) { StringMetadatum(it) }
        "filename" -> TextMetadatumHandler(FFmpegMediaMetadataRetriever.METADATA_KEY_FILENAME) { StringMetadatum(it) }
        "filesize" -> TextMetadatumHandler(FFmpegMediaMetadataRetriever.METADATA_KEY_FILESIZE) { IntMetadatum(it.toInt()) }
        "framerate" -> TextMetadatumHandler(FFmpegMediaMetadataRetriever.METADATA_KEY_FRAMERATE) { IntMetadatum(it.toInt()) }
        "genre" -> TextMetadatumHandler(FFmpegMediaMetadataRetriever.METADATA_KEY_GENRE) { StringMetadatum(it) }
        "icyMetadata" -> TextMetadatumHandler(FFmpegMediaMetadataRetriever.METADATA_KEY_ICY_METADATA) { StringMetadatum(it) }
        "language" -> TextMetadatumHandler(FFmpegMediaMetadataRetriever.METADATA_KEY_LANGUAGE) { StringMetadatum(it) }
        "performer" -> TextMetadatumHandler(FFmpegMediaMetadataRetriever.METADATA_KEY_PERFORMER) { StringMetadatum(it) }
        "publisher" -> TextMetadatumHandler(FFmpegMediaMetadataRetriever.METADATA_KEY_PUBLISHER) { StringMetadatum(it) }
        "serviceName" -> TextMetadatumHandler(FFmpegMediaMetadataRetriever.METADATA_KEY_SERVICE_NAME) { StringMetadatum(it) }
        "title" -> TextMetadatumHandler(FFmpegMediaMetadataRetriever.METADATA_KEY_TITLE) { StringMetadatum(it) }
        "track" -> TextMetadatumHandler(FFmpegMediaMetadataRetriever.METADATA_KEY_TRACK) { StringMetadatum(it) }
        "variantBitrate" -> TextMetadatumHandler(FFmpegMediaMetadataRetriever.METADATA_KEY_VARIANT_BITRATE) { IntMetadatum(it.toInt()) }
        "videoCodec" -> TextMetadatumHandler(FFmpegMediaMetadataRetriever.METADATA_KEY_VIDEO_CODEC) { StringMetadatum(it) }
        "videoRotation" -> TextMetadatumHandler(FFmpegMediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION) { StringMetadatum(it) }
        else -> null
      }
    }

    @ReactMethod
    fun getMetadata(uri: String, requiredMetadata: ReadableArray, promise: Promise) {
      val mmr = FFmpegMediaMetadataRetriever()

      try {
        mmr.setDataSource(uri)
        val metadata = Arguments.createMap()

        for (i in 0 until requiredMetadata.size()) {
          val metadatum = requiredMetadata.getString(i)
          textMetadatumToTextMetadatumHandler(metadatum)?.also { textMetadatumHandler ->
            val rawMetadatumValue = mmr.extractMetadata(textMetadatumHandler.metadataKey)
            val metadatumValue = textMetadatumHandler.metadatumValueHandler(rawMetadatumValue)

            when (metadatumValue) {
              is StringMetadatum -> metadata.putString(metadatum!!, metadatumValue.string)
              is IntMetadatum -> metadata.putInt(metadatum!!, metadatumValue.int)
            }
          }
        }

        mmr.release()
        promise.resolve(metadata)
      } catch (err: Throwable) {
        mmr.release()
        promise.reject(err)
      }
    }


}
