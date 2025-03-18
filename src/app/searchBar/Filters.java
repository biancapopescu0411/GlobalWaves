package app.searchBar;

import fileio.input.FiltersInput;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Data@Getter@Setter
public final class Filters {
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(final String album) {
        this.album = album;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(final ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(final String lyrics) {
        this.lyrics = lyrics;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(final String genre) {
        this.genre = genre;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(final String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(final String artist) {
        this.artist = artist;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(final String followers) {
        this.followers = followers;
    }

    private String name;
    private String album;
    private ArrayList<String> tags;
    private String lyrics;
    private String genre;
    private String releaseYear;
    private String artist;
    private String owner;
    private String followers;

    public Filters(final FiltersInput filters) {
        this.name = filters.getName();
        this.album = filters.getAlbum();
        this.tags = filters.getTags();
        this.lyrics = filters.getLyrics();
        this.genre = filters.getGenre();
        this.releaseYear = filters.getReleaseYear();
        this.artist = filters.getArtist();
        this.owner = filters.getOwner();
        this.followers = filters.getFollowers();
    }


}
