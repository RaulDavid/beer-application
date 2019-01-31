package com.api.duff.config;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Slf4j
@Configuration
public class SpotifyApiConfig {

    @Bean
    public SpotifyApi spotifyApiClient() {
        var api = SpotifyApi.builder()
                .setClientId("c810ba7e512748f1be17c95919a47dac")
                .setClientSecret("9a0118c766a04a6e8a58dcd67d3d2a43")
                .build();
        return applyAccessToken(api);
    }

    private SpotifyApi applyAccessToken(SpotifyApi api) {
        try {
            api.setAccessToken(api.clientCredentials().build().execute().getAccessToken());
        } catch (IOException | SpotifyWebApiException e) {
            log.error("error getting accessToken message={}", e.getMessage());
        }
        return api;
    }
}
