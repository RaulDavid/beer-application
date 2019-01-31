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

import static java.util.Arrays.asList;

@Slf4j
@Component
public class SpotifyClient {

    private SpotifyApi spotifyApi;

    public SpotifyClient(SpotifyApi spotifyApi) {
        this.spotifyApi = spotifyApi;
    }

    public Mono<Playlist> getPlaylistByName(String name) {
        return findByName(name)
                .flatMap(playlistSimplified ->
                        getTracks(playlistSimplified)
                                .map(tracks -> buildPlaylist(tracks, playlistSimplified.getName()))
                );
    }

    private Mono<PlaylistSimplified> findByName(String name) {
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

    private Mono<Paging<PlaylistTrack>> getTracks(PlaylistSimplified simplified) {
        try {
            return Mono.just(spotifyApi.getPlaylistsTracks(simplified.getId())
                    .limit(100)
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
