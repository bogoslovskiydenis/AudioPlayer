package com.example.audioplayer.utils;

import android.media.MediaMetadataRetriever;

public interface BytesFromUri {

    byte[] albumArt(String uri);

    class Base implements BytesFromUri{

        @Override
        public byte[] albumArt(String uri) {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(uri);
            byte[] art = retriever.getEmbeddedPicture();
            retriever.release();
            return art;
        }
    }
}
