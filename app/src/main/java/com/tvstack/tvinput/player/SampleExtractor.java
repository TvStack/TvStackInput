package com.tvstack.tvinput.player;


import java.io.IOException;

/**
 * Extractor for reading track metadata and samples stored tracks
 */
public interface SampleExtractor {

    /**
     * Prepares the extractor for reading track metadata and samples.
     *
     * @return whether the source is ready; if {@code false}, this method must be called again.
     * @throws IOException thrown if the source can't be read
     */
    boolean prepare() throws IOException;
}
