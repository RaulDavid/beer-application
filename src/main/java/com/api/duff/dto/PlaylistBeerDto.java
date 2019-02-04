package com.api.duff.dto;

import com.api.duff.domain.PlaylistBeer;
import lombok.Data;

import java.io.Serializable;

import static com.api.duff.dto.PlaylistDto.playlistDtoOf;

@Data
public class PlaylistBeerDto implements Serializable {

    private final String beerStyle;
    private final PlaylistDto playlist;

    private PlaylistBeerDto(String beerStyle, PlaylistDto playlist) {
        this.beerStyle = beerStyle;
        this.playlist = playlist;
    }

    public static PlaylistBeerDto playlistBeerDtoOf(PlaylistBeer playlistBeer) {
        return new PlaylistBeerDto(
                playlistBeer.getBeerStyle().getName(),
                playlistDtoOf(playlistBeer.getPlaylist()));
    }
}
