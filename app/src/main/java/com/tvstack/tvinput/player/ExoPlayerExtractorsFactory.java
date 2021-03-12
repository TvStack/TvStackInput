package com.tvstack.tvinput.player;

import android.net.Uri;

import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;

import java.util.List;
import java.util.Map;

public class ExoPlayerExtractorsFactory implements ExtractorsFactory {
    @Override
    public Extractor[] createExtractors() {
        return new Extractor[0];
    }

    @Override
    public Extractor[] createExtractors(Uri uri, Map<String, List<String>> responseHeaders) {
        return new Extractor[0];
    }
}
