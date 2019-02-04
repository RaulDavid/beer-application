package com.api.duff.config;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class SpotifyApiConfig {

    private static SpotifyApi api;

    private SpotifyApiConfig() {
    }

    public static SpotifyApi buildSpotifyApiAccess() {
        if(api == null || isExpiredToken(api)) {
            api = SpotifyApi.builder()
                    .setClientId("c810ba7e512748f1be17c95919a47dac")
                    .setClientSecret("9a0118c766a04a6e8a58dcd67d3d2a43")
                    .build();
            return applyAccessToken(api);
        }
        return api;
    }

    private static SpotifyApi applyAccessToken(SpotifyApi api) {
        try {
            var executeClientCredentials = api.clientCredentials().build().execute();
            api.setAccessToken(executeClientCredentials.getAccessToken());
            log.info("spotify accessToken expire-in={} seconds", executeClientCredentials.getExpiresIn());
        } catch (IOException | SpotifyWebApiException e) {
            log.error("error getting accessToken message={}", e.getMessage());
        }
        return api;
    }

    private static boolean isExpiredToken(SpotifyApi api) {
        try {
            return api.clientCredentials().build().execute().getExpiresIn() <= 0;
        } catch (IOException | SpotifyWebApiException e) {
            log.error("error getting expire in spotify token message={}", e.getMessage());
            return true;
        }
    }
}
