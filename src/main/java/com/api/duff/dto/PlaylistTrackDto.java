package com.api.duff.dto;

import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import lombok.Data;

import java.io.Serializable;

import static java.util.Objects.requireNonNull;

@Data
class PlaylistTrackDto implements Serializable {

    private final String name;
    private final String artist;
    private final String link;

    private PlaylistTrackDto(String name, String artist, String link) {
        this.name = name;
        this.artist = artist;
        this.link = link;
    }

    static PlaylistTrackDto playlistTrackDtoOf(PlaylistTrack playlistTrack) {
        requireNonNull(playlistTrack, "playlist track must not be null");
        var track = requireNonNull(playlistTrack.getTrack(), "playlist track must not be null");
        var trackArtist = track.getArtists()[0];
        return new PlaylistTrackDto(track.getName(), trackArtist.getName(), trackArtist.getHref());
    }
}
