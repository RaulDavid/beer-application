package com.api.duff.dto;

import com.wrapper.spotify.model_objects.specification.Playlist;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

@Data
class PlaylistDto implements Serializable {

    private final String name;
    private final List<PlaylistTrackDto> tracks;

    private PlaylistDto(String name, List<PlaylistTrackDto> tracks) {
        this.name = name;
        this.tracks = tracks;
    }

    static PlaylistDto playlistDtoOf(Playlist playlist) {
        requireNonNull(playlist, "playlist must not be null");
        var tracks = requireNonNull(playlist.getTracks(), "playlist tracks must not be null");
        var tracksItems = requireNonNull(tracks.getItems(), "playlist tracks items must not be null");
        var playlistTracks = stream(tracksItems)
                .map(PlaylistTrackDto::playlistTrackDtoOf)
                .collect(toList());

        return new PlaylistDto(playlist.getName(), playlistTracks);
    }
}
