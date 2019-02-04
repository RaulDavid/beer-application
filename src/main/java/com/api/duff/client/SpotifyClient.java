package com.api.duff.client;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static com.api.duff.config.SpotifyApiConfig.buildSpotifyApiAccess;
import static java.util.Arrays.asList;

@Slf4j
@Component
public class SpotifyClient {

    public Mono<Playlist> getPlaylistByName(String name) {
        var spotifyApi = buildSpotifyApiAccess();
        return findByName(name, spotifyApi)
                .flatMap(playlistSimplified ->
                        getTracks(playlistSimplified, spotifyApi)
                                .map(tracks -> buildPlaylist(tracks, playlistSimplified.getName()))
                );
    }

    private Mono<PlaylistSimplified> findByName(String name, SpotifyApi spotifyApi) {
        try {
            return Flux.fromIterable(asList(spotifyApi.searchPlaylists(name)
                    .limit(1)
                    .build()
                    .execute()
                    .getItems()))
                    .next();
        } catch (IOException | SpotifyWebApiException e) {
            log.error("error getting playlist playListName={}, message={}", name, e.getMessage());
            return Mono.error(e);
        }
    }

    private Mono<Paging<PlaylistTrack>> getTracks(PlaylistSimplified simplified, SpotifyApi spotifyApi) {
        try {
            return Mono.just(spotifyApi.getPlaylistsTracks(simplified.getId())
                    .limit(10)
                    .build()
                    .execute());
        } catch (IOException | SpotifyWebApiException e) {
            log.error("error getting playlist tracks playListName={}, message={}", simplified.getName(), e.getMessage());
            return Mono.error(e);
        }
    }

    private Playlist buildPlaylist(Paging<PlaylistTrack> tracks, String playlistName) {
        return new Playlist.Builder()
                .setName(playlistName)
                .setTracks(tracks)
                .build();
    }
}
