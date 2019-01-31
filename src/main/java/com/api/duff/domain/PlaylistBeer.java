package com.api.duff.domain;

import com.wrapper.spotify.model_objects.specification.Playlist;
import lombok.Data;

import java.io.Serializable;

@Data
public class PlaylistBeer implements Serializable {

    private BeerStyle beerStyle;
    private Playlist playlist;

    private PlaylistBeer(BeerStyle beerStyle, Playlist playlist) {
        this.beerStyle = beerStyle;
        this.playlist = playlist;
    }

    public static PlaylistBeer playlistBeerOf(BeerStyle beerStyle, Playlist playlist) {
        return new PlaylistBeer(beerStyle, playlist);
    }
}
