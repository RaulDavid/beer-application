package com.api.duff.domain;

import com.wrapper.spotify.model_objects.specification.Playlist;
import lombok.Data;

import java.io.Serializable;

import static java.util.Objects.requireNonNull;

@Data
public class PlaylistBeer implements Serializable {

    private final BeerStyle beerStyle;
    private final Playlist playlist;

    private PlaylistBeer(BeerStyle beerStyle, Playlist playlist) {
        this.beerStyle = beerStyle;
        this.playlist = playlist;
    }

    public static PlaylistBeer playlistBeerOf(BeerStyle beerStyle, Playlist playlist) {
        requireNonNull(beerStyle, "beer style must not be null");
        requireNonNull(playlist, "playlist must not be null");
        return new PlaylistBeer(beerStyle, playlist);
    }
}
