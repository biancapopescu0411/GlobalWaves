package app;

import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.player.PlayerSource;
import app.user.User;
import app.user.Artist;
import app.user.Host;
import app.user.UserEntry;
import app.utils.Enums;
import checker.CheckerConstants;
import fileio.input.UserInput;
import fileio.input.SongInput;
import fileio.input.PodcastInput;
import fileio.input.EpisodeInput;
import fileio.input.CommandInput;
import fileio.input.MerchInput;
import fileio.input.EventInput;
import fileio.input.AnnouncementInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * The type Admin.
 */
@Getter@Setter
public final class Admin {
    private static Admin admin = null;
    private List<User> users = new ArrayList<>();
    private List<Artist> artists = new ArrayList<>();
    private List<Host> hosts = new ArrayList<>();
    private List<Song> songs = new ArrayList<>();
    private List<Podcast> podcasts = new ArrayList<>();
    private List<Album> albums = new ArrayList<>();
    private int timestamp = 0;
    private static final int LIMIT = 5;

    private Admin() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static Admin getInstance() {
        if (admin == null) {
            admin = new Admin();
        }
        return admin;
    }

    /**
     * Sets users.
     *
     * @param userInputList The user input list
     */
    public void setUsers(final List<UserInput> userInputList) {
        users = new ArrayList<>();
        for (UserInput userInput : userInputList) {
            users.add(new User(userInput.getUsername(), userInput.getAge(), userInput.getCity()));
        }
    }

    /**
     * Sets songs.
     *
     * @param songInputList The song input list
     */
    public void setSongs(final List<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }

    /**
     * Sets podcasts.
     *
     * @param podcastInputList The podcast input list
     */
    public void setPodcasts(final List<PodcastInput> podcastInputList) {
        podcasts = new ArrayList<>();
        for (PodcastInput podcastInput : podcastInputList) {
            List<Episode> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
                episodes.add(new Episode(episodeInput.getName(),
                                         episodeInput.getDuration(),
                                         episodeInput.getDescription()));
            }
            podcasts.add(new Podcast(podcastInput.getName(), podcastInput.getOwner(), episodes));
        }
    }

    /**
     * Gets songs.
     *
     * @return The songs
     */
    public List<Song> getSongs() {
        return new ArrayList<>(songs);
    }

    /**
     * Gets podcasts.
     *
     * @return The podcasts
     */
    public List<Podcast> getPodcasts() {
        return new ArrayList<>(podcasts);
    }

    /**
     * Gets playlists.
     *
     * @return The playlists
     */
    public List<Playlist> getPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        for (User user : users) {
            playlists.addAll(user.getPlaylists());
        }
        return playlists;
    }

    /**
     * Gets user.
     *
     * @param username The username
     * @return The user
     */
    public User getUser(final String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Retrieves an artist from the list of artists based on the username.
     *
     * @param username The username of the artist to be retrieved
     * @return The artist with the specified username, or null if not found
     */
    public Artist getArtist(final String username) {
        // iterate through the list of artists
        for (Artist artist : artists) {
            // if the current artist has the specified username
            if (artist.getUsername().equals(username)) {
                // return the current artist
                return artist;
            }
        }
        // if no artist with the specified username was found, return null
        return null;
    }

    /**
     * Retrieves a host from the list of hosts based on the username.
     *
     * @param username The username of the host to be retrieved
     * @return The host with the specified username, or null if not found
     */
    public Host getHost(final String username) {
        // iterate through the list of hosts
        for (Host host : hosts) {
            // if the current host has the specified username
            if (host.getUsername().equals(username)) {
                // return the current host
                return host;
            }
        }
        // if no host with the specified username was found, return null
        return null;
    }

    /**
     * Update timestamp.
     *
     * @param newTimestamp The new timestamp
     */
    public void updateTimestamp(final int newTimestamp) {
        int elapsed = newTimestamp - timestamp;
        timestamp = newTimestamp;
        if (elapsed == 0) {
            return;
        }

        for (User user : users) {
            user.simulateTime(elapsed);
        }
    }

    /**
     * Gets top 5 songs.
     *
     * @return The top 5 songs
     */
    public List<String> getTop5Songs() {
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= LIMIT) {
                break;
            }
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }

    /**
     * Gets top 5 playlists.
     *
     * @return The top 5 playlists
     */
    public List<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(getPlaylists());
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= LIMIT) {
                break;
            }
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }

    /**
     * Gets top 5 albums based on the number of likes.
     *
     * @return The top 5 albums
     */
    public List<String> getTop5Albums() {
        // calculate the number of likes for each album
        for (Album album : albums) {
            Integer albumLikes = 0;

            // iterate through the songs of the current album
            for (Song song : album.getSongs()) {
                // add the number of likes of the current song to the total number of likes
                albumLikes += song.getLikes();
            }

            // set the number of likes for the current album
            album.setLikes(albumLikes);
        }

        // create a list of albums sorted by the number of likes
        List<Album> sortedAlbums = new ArrayList<>(albums);

        // if two albums have the same number of likes,
        // the one lexico-graphically smaller is placed first
        sortedAlbums.sort(Comparator.comparingInt(Album::getLikes).reversed()
                .thenComparing(Album::getName, Comparator.naturalOrder()));

        // create a list to store the names of the top 5 albums
        List<String> topAlbums = new ArrayList<>();

        int count = 0;
        // iterate through the sorted list of albums
        for (Album album : sortedAlbums) {
            // if the top 5 albums have been found, stop iterating
            if (count >= LIMIT) {
                break;
            }

            // add the name of the current album to the list of top albums
            topAlbums.add(album.getName());
            // increment the number of albums found
            count++;
        }

        // return the list of top albums
        return topAlbums;
    }

    /**
     * Gets top 5 artists based on the number of likes.
     *
     * @return The top 5 artists
     */
    public List<String> getTop5Artists() {
        // calculate the number of likes for each artist
        for (Artist artist : artists) {
            Integer artistLikes = 0;

            // iterate through the albums of the current artist
            for (Album album : artist.getAlbums()) {
                // iterate through the songs of the current album
                for (Song song : album.getSongs()) {
                    // add the number of likes of the current song to the total number of likes
                    artistLikes += song.getLikes();
                }
            }

            // set the number of likes for the current artist
            artist.setLikes(artistLikes);
        }

        // create a list of artists sorted by the number of likes
        List<Artist> sortedArtists = new ArrayList<>(artists);
        // create a list to store the names of the top 5 artists
        List<String> topArtists = new ArrayList<>();

        // sort the list of artists by the number of likes
        sortedArtists.sort(Comparator.comparingInt(Artist::getLikes).reversed());

        int count = 0;
        // iterate through the sorted list of artists
        for (Artist artist : sortedArtists) {
            // if the top 5 artists have been found, stop iterating
            if (count >= LIMIT) {
                break;
            }

            // add the name of the current artist to the list of top artists
            topArtists.add(artist.getUsername());
            // increment the number of artists found
            count++;
        }

        // return the list of top artists
        return topArtists;
    }

    /**
     * Retrieves a list of users that are currently online.
     *
     * @return The list of users that are currently online
     */
    public List<String> getOnlineUsers() {
        // create a list to store the usernames of the online users
        List<String> onlineUsers = new ArrayList<>();

        // iterate through the list of users
        for (User user : users) {
            // if the current user is online, add its username to the list
            if (user.getStatus() == Enums.ConnectionStatus.ONLINE) {
                onlineUsers.add(user.getUsername());
            }
        }

        // return the list of online users
        return onlineUsers;
    }

    /**
     * Checks if a user with the specified username exists.
     * @param username The username of the user to be checked
     * @return True if a user with the specified username exists, false otherwise
     */
    public boolean isUser(final String username) {
        // iterate through the list of users
        for (User user : users) {
            // if the current user has the specified username, return true
            if (user.getUsername().equals(username)) {
                return true;
            }
        }

        // if no user with the specified username was found, return false
        return false;
    }

    /**
     * Checks if an artist with the specified username exists.
     * @param username The username of the artist to be checked
     * @return True if an artist with the specified username exists, false otherwise
     */
    public boolean isArtist(final String username) {
        // iterate through the list of artists
        for (Artist artist : artists) {
            // if the current artist has the specified username, return true
            if (artist.getUsername().equals(username)) {
                return true;
            }
        }

        // if no artist with the specified username was found, return false
        return false;
    }

    /**
     * Checks if a host with the specified username exists.
     * @param username The username of the host to be checked
     * @return True if a host with the specified username exists, false otherwise
     */
    public boolean isHost(final String username) {
        // iterate through the list of hosts
        for (Host host : hosts) {
            // if the current host has the specified username, return true
            if (host.getUsername().equals(username)) {
                return true;
            }
        }

        // if no host with the specified username was found, return false
        return false;
    }

    /**
     * Adds a new user, artist or host based on the type specified.
     * @param commandInput The input containing user details and type
     * @return A status message indicating the result of the operation
     */
    public String addUser(final CommandInput commandInput) {
        // check if the username is already taken by a user, artist or host
        if (isUser(commandInput.getUsername()) || isArtist(commandInput.getUsername())
               || isHost(commandInput.getUsername())) {
           return "The username " + commandInput.getUsername() + " is already taken.";
        }

        // check the type of the user to be added
        if (commandInput.getType().equals("user")) {
            // if the user is a simple user, create a new user and add it to the list of users
            User user = new User(commandInput.getUsername(), commandInput.getAge(),
                   commandInput.getCity());

            users.add(user);
        } else if (commandInput.getType().equals("artist")) {
            // if the user is an artist, create a new artist and add it to the list of artists
            Artist artist = new Artist(commandInput.getUsername(), commandInput.getAge(),
                   commandInput.getCity());

            artists.add(artist);
        } else if (commandInput.getType().equals("host")) {
            // if the user is a host, create a new host and add it to the list of hosts
            Host host = new Host(commandInput.getUsername(), commandInput.getAge(),
                   commandInput.getCity());

            hosts.add(host);
        }

        // if the user was added successfully, return a success message
        return "The username " + commandInput.getUsername() + " has been added successfully.";
    }

    /**
     * Adds a new album to an artist's list of albums.
     * @param commandInput The input containing details about the album to be added
     * @return A status message indicating the result of the operation
     */
    public String addAlbum(final CommandInput commandInput) {
        // check if the username is already taken by a user, artist or host
        if (!isUser(commandInput.getUsername()) && !isArtist(commandInput.getUsername())
                && !isHost(commandInput.getUsername())) {
            return "The username " + commandInput.getUsername() + " does not exist.";
        }

        // verify if user is an artist
        if (!isArtist(commandInput.getUsername())) {
            // if not, return an error message
            return commandInput.getUsername() + " is not an artist.";
        }

        // retrieve the artist with the specified username
        Artist currentArtist = getArtist(commandInput.getUsername());

        // verify if artist already has an album with the same name
        for (Album album : currentArtist.getAlbums()) {
            if (album.getName().equals(commandInput.getName())) {
                return commandInput.getUsername() + " has another album with the same name.";
            }
        }

        int songIndex = 0;
        // check for duplicate songs in the album
        for (SongInput songInput : commandInput.getSongs()) {
            for (SongInput songInput1 : commandInput.getSongs().subList(0, songIndex)) {
                if (songInput.getName().equals(songInput1.getName())) {
                    return commandInput.getUsername()
                            + " has the same song at least twice in this album.";
                }
            }

            songIndex++;
        }

        // create a new album with the specified name and add it to the artist's list of albums
        Album album = new Album(commandInput.getName(), commandInput.getUsername());
        currentArtist.getAlbums().add(album);
        albums.add(album);

        // add the specified songs to the album and to the list of songs
        for (SongInput songInput : commandInput.getSongs()) {
            Song newSong = new Song(songInput.getName(), songInput.getDuration(),
                    songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist());
            album.getSongs().add(newSong);
            songs.add(newSong);
        }

        // if the album was added successfully, return a success message
        return commandInput.getUsername() + " has added new album successfully.";
    }

    /**
     * Removes an album for the specified artist.
     *
     * @param commandInput The input containing album details and artist username.
     * @return A status message indicating the result of the album removal.
     */
    public String removeAlbum(final CommandInput commandInput) {
        // check if the username is already taken by a user, artist or host
        if (!isUser(commandInput.getUsername()) && !isArtist(commandInput.getUsername())
                && !isHost(commandInput.getUsername())) {
            return "The username " + commandInput.getUsername() + " doesn't exist.";
        }

        // verify if user is an artist
        if (!isArtist(commandInput.getUsername())) {
            return commandInput.getUsername() + " is not an artist.";
        }

        // retrieve the artist with the specified username
        Artist currentArtist = getArtist(commandInput.getUsername());

        // verify if the artist already has an album with the same name
        boolean found = false;
        Album deletedAlbum = null;
        // iterate through the albums of the current artist
        for (Album album : currentArtist.getAlbums()) {
            // check if the current album has the specified name
            if (album.getName().equals(commandInput.getName())) {
                // mark the album as found
                found = true;
                // store the album to be deleted
                deletedAlbum = album;

                break;
            }
        }

        // check if the album was not found, return an error message
        if (!found) {
            return commandInput.getUsername() + " doesn't have an album with the given name.";
        }

        // iterate through the users
        for (User user : users) {
            // retrieve the player source of the current user
            PlayerSource userSource = user.getPlayer().getSource();

            // check if the album is playing
            if (userSource != null && userSource.getAudioCollection() == deletedAlbum) {
                // return an error message
                return commandInput.getUsername() + " can't delete this album.";
            }

            // iterate through the songs of the album
            for (Song song : deletedAlbum.getSongs()) {
                // check if a song is playing
                if (userSource != null && userSource.getAudioFile() == song) {
                    // return an error message
                    return commandInput.getUsername() + " can't delete this album.";
                }
            }

            // check if a song from the album is in a playlist
            for (Playlist playlist : user.getPlaylists()) {
                for (Song song : deletedAlbum.getSongs()) {
                    if (playlist.getSongs().contains(song)) {
                        return commandInput.getUsername() + " can't delete this album.";
                    }
                }
            }
        }
        // if the album is not playing, delete it
        currentArtist.getAlbums().remove(deletedAlbum);
        albums.remove(deletedAlbum);

        // remove the album's songs and references from playlists and liked songs of users
        for (Song song : deletedAlbum.getSongs()) {
            songs.remove(song);

            for (Playlist playlist : getPlaylists()) {
                playlist.getSongs().remove(song);
            }
            for (User user : users) {
                user.getLikedSongs().remove(song);
            }
        }

        // if the album was deleted successfully, return a success message
        return commandInput.getUsername() + " deleted the album successfully.";
    }

    /**
     * Adds a new podcast to a host's list of podcasts
     * @param command The input containing details about the podcast to be added
     * @return A status message indicating the result of the operation
     */
    public String addPodcast(final CommandInput command) {
        // check if the username is already taken by a user, artist or host
        if (!isUser(command.getUsername()) && !isArtist(command.getUsername())
                && !isHost(command.getUsername())) {
            return "The username " + command.getUsername() + " does not exist.";
        }

        // verify if user is a host
        if (!isHost(command.getUsername())) {
            // if not, return an error message
            return command.getUsername() + " is not a host.";
        }

        // retrieve the host with the specified username
        Host currentHost = getHost(command.getUsername());

        // verify if the host already has a podcast with the same name
        for (Podcast podcast : currentHost.getPodcasts()) {
            if (podcast.getName().equals(command.getName())) {
                return command.getUsername() + " has another podcast with the same name.";
            }
        }

        // check for duplicate episodes in the podcast
        int episodeIndex = 0;
        for (EpisodeInput episodeInput : command.getEpisodes()) {
            for (EpisodeInput episodeInput1 : command.getEpisodes().subList(0, episodeIndex)) {
                if (episodeInput.getName().equals(episodeInput1.getName())) {
                    return command.getUsername() + " has the same episode in this podcast.";
                }
            }

            episodeIndex++;
        }

        // create a new podcast with the specified name and add it to the host's list of podcasts
        Podcast podcast = new Podcast(command.getName(), command.getUsername(), new ArrayList<>());
        currentHost.getPodcasts().add(podcast);
        podcasts.add(podcast);

        // add the specified episodes to the podcast and to the list of episodes
        for (EpisodeInput episodeInput : command.getEpisodes()) {
            Episode newEpisode = new Episode(episodeInput.getName(), episodeInput.getDuration(),
                    episodeInput.getDescription());
            podcast.getEpisodes().add(newEpisode);
        }

        // if the podcast was added successfully, return a success message
        return command.getUsername() + " has added new podcast successfully.";
    }

    /**
     * Removes a podcast for the specified host based on the provided CommandInput
     *
     * @param commandInput The input containing podcast details and host username
     * @return A status message indicating the result of the podcast removal
     */
    public String removePodcast(final CommandInput commandInput) {
        // check if the username is already taken by a user, artist or host
        if (!isUser(commandInput.getUsername()) && !isArtist(commandInput.getUsername())
                && !isHost(commandInput.getUsername())) {
            return "The username " + commandInput.getUsername() + " doesn't exist.";
        }

        //verify if the user is a host
        if (!isHost(commandInput.getUsername())) {
            return commandInput.getUsername() + " is not a host.";
        }

        // retrieve the host with the specified username
        Host currentHost = getHost(commandInput.getUsername());

        // verify if the host already has a podcast with the same name
        boolean found = false;
        Podcast deletedPodcast = null;
        // iterate through the podcasts of the current host
        for (Podcast podcast : currentHost.getPodcasts()) {
            // check if the current podcast has the specified name
            if (podcast.getName().equals(commandInput.getName())) {
                // mark the podcast as found
                found = true;
                // store the podcast to be deleted
                deletedPodcast = podcast;

                break;
            }
        }

        // return an error message if the podcast was not found
        if (!found) {
            return commandInput.getUsername() + " doesn't have a podcast with the given name.";
        }

        // iterate through the users
        for (User user : users) {
            // retrieve the player source of the current user
            PlayerSource userSource = user.getPlayer().getSource();

            // check if the podcast is playing
            if (userSource != null && userSource.getAudioCollection() == deletedPodcast) {
                // if the podcast is playing, return an error message
                return commandInput.getUsername() + " can't delete this podcast.";
            }
        }
        // if the podcast is not playing, delete it
        currentHost.getPodcasts().remove(deletedPodcast);
        podcasts.remove(deletedPodcast);

        // if the podcast was deleted successfully, return a success message
        return commandInput.getUsername() + " deleted the podcast successfully.";
    }

    /**
     * Generates a StringBuilder containing the user's liked songs and followed
     * playlists for the home page
     *
     * @param username The username of the user for whom the home page is generated
     * @return A StringBuilder containing the user's liked songs and followed playlists
     */
    public StringBuilder printHomePage(final String username) {
        // create a StringBuilder to store the home page
        StringBuilder result = new StringBuilder();
        // retrieve the user with the specified username
        User user = getUser(username);

        // sort the user's liked songs by the number of likes
        List<Song> likedSongs = new ArrayList<>(user.getLikedSongs());
        likedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        int toIndexSongs = Math.min(CheckerConstants.MAX_SIZE, user.getLikedSongs().size());
        likedSongs = likedSongs.subList(0, toIndexSongs);

        // calculate the number of likes for each playlist
        List<Playlist> followedPlaylists = new ArrayList<>(user.getFollowedPlaylists());
        for (Playlist playlist : user.getFollowedPlaylists()) {
            for (Song song : playlist.getSongs()) {
                playlist.setLikes(playlist.getLikes() + song.getLikes());
            }
        }

        // sort the user's followed playlists by the number of likes
        followedPlaylists.sort(Comparator.comparingInt(Playlist::getLikes).reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        int toIndexPlaylist = Math.min(CheckerConstants.MAX_SIZE,
                user.getFollowedPlaylists().size());
        followedPlaylists = followedPlaylists.subList(0, toIndexPlaylist);

        result.append("Liked songs:\n\t[");

        // iterate through the user's liked songs
        for (Song song : likedSongs) {
            // add the name of the current song to the home page
            result.append(song.getName());

            // if the current song is not the last one, add a comma and a space
            if (likedSongs.indexOf(song) != likedSongs.size() - 1) {
                result.append(", ");
            }
        }

        result.append("]\n\nFollowed playlists:\n\t[");

        // iterate through the user's followed playlists
        for (Playlist playlist : followedPlaylists) {
            // add the name of the current playlist to the home page
            result.append(playlist.getName());

            // if the current playlist is not the last one, add a comma and a space
            if (followedPlaylists.indexOf(playlist) != followedPlaylists.size() - 1) {
                result.append(", ");
            }
        }

        result.append("]");

        // return the final StringBuilder
        return result;
    }

    /**
     * Generates a StringBuilder containing the user's liked songs and followed
     * playlists for the liked content page
     *
     * @param username The username of the user for whom the liked content page is generated
     * @return A StringBuilder containing the user's liked songs and followed playlists
     */
    public StringBuilder printLikedContentPage(final String username) {
        // create a StringBuilder to store the liked content page
        StringBuilder result = new StringBuilder();
        // retrieve the user with the specified username
        User user = getUser(username);

        result.append("Liked songs:\n\t[");

        // iterate through the user's liked songs
        for (Song song : user.getLikedSongs()) {
            // add the name of the current song to the liked content page, along with its artist
            result.append(song.getName()).append(" - ").append(song.getArtist());

            // if the current song is not the last one, add a comma and a space
            if (user.getLikedSongs().indexOf(song) != user.getLikedSongs().size() - 1) {
                result.append(", ");
            }
        }

        result.append("]\n\nFollowed playlists:\n\t[");

        // iterate through the user's followed playlists
        for (Playlist playlist : user.getFollowedPlaylists()) {
            // add the name of the current playlist to the liked content page, along with its owner
            result.append(playlist.getName()).append(" - ").append(playlist.getOwner());

            // if the current playlist is not the last one, add a comma and a space
            if (user.getFollowedPlaylists().indexOf(playlist)
                    != user.getFollowedPlaylists().size() - 1) {
                result.append(", ");
            }
        }

        result.append("]");

        // return the final StringBuilder
        return result;
    }

    /**
     * Generates a StringBuilder containing the artist's albums, merch and events
     *
     * @param username The username of the artist for whom the page is generated
     * @return A StringBuilder containing the artist's albums, merch and events
     */
    public StringBuilder printArtistPage(final String username) {
        // create a StringBuilder to store the artist page
        StringBuilder result = new StringBuilder();
        // retrieve the artist with the specified username
        Artist artist = getArtist(username);

        result.append("Albums:\n\t[");

        // iterate through the artist's albums
        for (Album album : artist.getAlbums()) {
            // add the name of the current album to the artist page
            result.append(album.getName());

            // if the current album is not the last one, add a comma and a space
            if (artist.getAlbums().indexOf(album) != artist.getAlbums().size() - 1) {
                result.append(", ");
            }
        }

        result.append("]\n\nMerch:\n\t[");

        // iterate through the artist's merch
        for (MerchInput merch : artist.getMerch()) {
            // add the name of the current merch to the artist page,
            // along with its price and description
            result.append(merch.getName()).append(" - ").append(merch.getPrice()).append(":\n\t")
                    .append(merch.getDescription());

            // if the current merch is not the last one, add a comma and a space
            if (artist.getMerch().indexOf(merch) != artist.getMerch().size() - 1) {
                result.append(", ");
            }
        }

        result.append("]\n\nEvents:\n\t[");

        // iterate through the artist's events
        for (EventInput event : artist.getEvents()) {
            // add the name of the current event to the artist page,
            // along with its date and description
            result.append(event.getName()).append(" - ").append(event.getDate()).append(":\n\t")
                    .append(event.getDescription());

            // if the current event is not the last one, add a comma and a space
            if (artist.getEvents().indexOf(event) != artist.getEvents().size() - 1) {
                result.append(", ");
            }
        }

        result.append("]");

        // return the final StringBuilder
        return result;
    }

    /**
     * Generates a StringBuilder containing the host's podcasts and announcements
     *
     * @param username The username of the host for whom the page is generated
     * @return A StringBuilder containing the host's podcasts and announcements
     */
    public StringBuilder printHostPage(final String username) {
        // create a StringBuilder to store the host page
        StringBuilder result = new StringBuilder();
        // retrieve the host with the specified username
        Host host = getHost(username);

        result.append("Podcasts:\n\t[");

        // iterate through the host's podcasts
        for (Podcast podcast : host.getPodcasts()) {
            // add the name of the current podcast to the host page
            result.append(podcast.getName()).append(":\n\t[");

            // iterate through the episodes of the current podcast
            for (Episode episode : podcast.getEpisodes()) {
                // add the name of the current episode to the host page,
                // along with its description
                result.append(episode.getName()).append(" - ").append(episode.getDescription());

                // if the current episode is not the last one, add a comma and a space
                if (podcast.getEpisodes().indexOf(episode) != podcast.getEpisodes().size() - 1) {
                    result.append(", ");
                }
            }

            result.append("]\n");

            // if the current podcast is not the last one, add a comma and a space
            if (host.getPodcasts().indexOf(podcast) != host.getPodcasts().size() - 1) {
                result.append(", ");
            }
        }

        result.append("]\n\nAnnouncements:\n\t[");

        // iterate through the host's announcements
        for (AnnouncementInput announcement : host.getAnnouncements()) {
            // add the name of the current announcement to the host page,
            // along with its description
            result.append(announcement.getName()).append(":\n\t").append(announcement
                    .getDescription()).append("\n");

            // if the current announcement is not the last one, add a comma and a space
            if (host.getAnnouncements().indexOf(announcement)
                    != host.getAnnouncements().size() - 1) {
                result.append(", ");
            }
        }

        result.append("]");

        // return the final StringBuilder
        return result;
    }

    /**
     * Generates a StringBuilder containing content based on the user's current page
     *
     * @param username The username of the user for whom the current page is generated
     * @return A StringBuilder containing content based on the user's current page
     */
    public StringBuilder printCurrentPage(final String username) {
        // retrieve the user with the specified username
        User user = getUser(username);

        // check if the user is offline
        if (user.getStatus().equals(Enums.ConnectionStatus.OFFLINE)) {
            // if the user is offline, return an error message
            return new StringBuilder(username + " is offline.");
        }

        // check the user's current page and generate the corresponding StringBuilder
        if (user.getCurrentPage() == Enums.CurrentPage.HOME_PAGE) {
            return printHomePage(username);
        } else if (user.getCurrentPage() == Enums.CurrentPage.ARTIST_PAGE) {
            String artistName = user.getSearchBar().getLastSelectedUser().getUsername();
            return printArtistPage(artistName);
        } else if (user.getCurrentPage() == Enums.CurrentPage.HOST_PAGE) {
            String hostName = user.getSearchBar().getLastSelectedUser().getUsername();
            return printHostPage(hostName);
        } else {
            return printLikedContentPage(username);
        }
    }

    /**
     * Changes the user's current page based on the provided CommandInput
     *
     * @param commandInput The input containing the desired next page and user information
     * @return A status message indicating the result of the page change
     */
    public String changePage(final CommandInput commandInput) {
        // check if the next page is valid
        if (!commandInput.getNextPage().equals("Home") && !commandInput.getNextPage()
                .equals("LikedContent")) {
            // if not, return an error message
            return commandInput.getUsername() + " is trying to access a non-existent page.";
        }

        // get the user with the specified username
        User user = getUser(commandInput.getUsername());

        // check if the user is offline
        if (user.getStatus().equals(Enums.ConnectionStatus.OFFLINE)) {
            // if so, return an error message
            return commandInput.getUsername() + " is offline.";
        }

        // change the user's current page based on the desired next page
        if (commandInput.getNextPage().equals("Home")) {
            user.setCurrentPage(Enums.CurrentPage.HOME_PAGE);
            user.getSearchBar().setLastSelectedUser(null);
        } else {
            user.setCurrentPage(Enums.CurrentPage.LIKED_CONTENT_PAGE);
            user.getSearchBar().setLastSelectedUser(null);
        }

        // if the page was changed successfully, return a success message
        return commandInput.getUsername() + " accessed " + commandInput.getNextPage()
                + " successfully.";
    }

    /**
     * Retrieves a list of usernames for all users, artists, and hosts in the system,
     * in this order
     *
     * @return A list of usernames for all users, artists, and hosts
     */
    public List<String> getAllUsers() {
        // create a list to store the usernames of all users, artists, and hosts
        List<String> allUsers = new ArrayList<>();

        // iterate through the users
        for (User user : users) {
            // add the username of the current user to the list
            allUsers.add(user.getUsername());
        }

        // iterate through the artists
        for (Artist artist : artists) {
            // add the username of the current artist to the list
            allUsers.add(artist.getUsername());
        }

        // iterate through the hosts
        for (Host host : hosts) {
            // add the username of the current host to the list
            allUsers.add(host.getUsername());
        }

        // return the list of usernames
        return allUsers;
    }

    /**
     * Deletes a user, artist, or host based on the provided CommandInput
     *
     * @param command The input containing the username and type of the entity to be deleted
     * @return A status message indicating the result of the deletion
     */
    public String deleteUser(final CommandInput command) {
        // check if the specified username exists and belongs to a user, artist, or host
        if (!isUser(command.getUsername()) && !isArtist(command.getUsername())
                && !isHost(command.getUsername())) {
            return "The username " + command.getUsername() + " doesn't exist.";
        }

        // check if the entity to be deleted is a user
        if (isUser(command.getUsername())) {
            User currentUser = getUser(command.getUsername());

            // check if any of the user's playlists are currently being played by other users
            for (Playlist playlist : currentUser.getPlaylists()) {
                for (User user : users) {
                    if (user != currentUser) {
                        PlayerSource userSource = user.getPlayer().getSource();
                        if (userSource != null && userSource.getAudioCollection() == playlist) {
                            return command.getUsername() + " can't be deleted.";
                        }
                    }
                }
            }

            // if no playlists are currently being played, proceed with user deletion
            users.remove(currentUser);

            // dislike all songs liked by the user
            for (Song likedSong : currentUser.getLikedSongs()) {
                likedSong.dislike();
            }

            // decrease the number of followers for all playlists followed by the user
            for (Playlist followedPlaylist : currentUser.getFollowedPlaylists()) {
                followedPlaylist.decreaseFollowers();
            }

            // remove the user from the list of followers for all playlists followed by the user
            for (Playlist playlist : currentUser.getPlaylists()) {
                for (User user : users) {
                    user.getFollowedPlaylists().remove(playlist);
                }
            }

            // if the user was deleted successfully, return a success message
            return command.getUsername() + " was successfully deleted.";
        } else if (isArtist(command.getUsername())) { // if the entity to be deleted is an artist
            Artist currentArtist = getArtist(command.getUsername());

            // check if the artist's page has been searched or selected by other users
            for (User user : users) {
                if (user.getSearchBar().getLastSearchType() != null
                        && user.getSearchBar().getLastSearchType().equals("artist")) {
                    for (UserEntry searchedArtist : user.getSearchBar().getUserResults()) {
                        if (searchedArtist.getUsername().equals(command.getUsername())) {
                            return command.getUsername() + " can't be deleted.";
                        }
                    }
                }

                if (user.getSearchBar().getLastSelectedUser() != null
                        && user.getSearchBar().getLastSelectedUser().getUsername()
                        .equals(command.getUsername())) {
                    return command.getUsername() + " can't be deleted.";
                }
            }

            // check if any albums or songs associated with the artist are currently
            // being played by other users
            for (Album album : currentArtist.getAlbums()) {
                for (User user : users) {
                    PlayerSource userSource = user.getPlayer().getSource();

                    // check if an album is playing
                    if (userSource != null && userSource.getAudioCollection() == album) {
                        return command.getUsername() + " can't be deleted.";
                    }

                    // check if a song from the album is playing
                    for (Song song : album.getSongs()) {
                        if (userSource != null && userSource.getAudioFile() == song) {
                            return command.getUsername() + " can't be deleted.";
                        }
                    }
                }
            }

            // if no albums or songs are currently being played, proceed with artist deletion
            artists.remove(currentArtist);

            // iterate through the artist's albums
            for (Album album : currentArtist.getAlbums()) {
                // remove the album from the list of albums
                albums.remove(album);

                // remove the album's songs and references from playlists and liked songs of users
                for (Song song : album.getSongs()) {
                    songs.remove(song);

                    for (Playlist playlist : getPlaylists()) {
                        playlist.getSongs().remove(song);
                    }

                    for (User user : users) {
                        user.getLikedSongs().remove(song);
                    }
                }
            }

            // if the artist was deleted successfully, return a success message
            return command.getUsername() + " was successfully deleted.";
        } else { // if the entity to be deleted is a host
            Host currentHost = getHost(command.getUsername());

            // check if the host's page has been searched or selected by other users
            for (User user : users) {
                if (user.getSearchBar().getLastSearchType() != null
                        && user.getSearchBar().getLastSearchType().equals("host")) {
                    for (UserEntry searchedHost : user.getSearchBar().getUserResults()) {
                        if (searchedHost.getUsername().equals(command.getUsername())) {
                            return command.getUsername() + " can't be deleted.";
                        }
                    }
                }

                if (user.getSearchBar().getLastSelectedUser() != null
                        && user.getSearchBar().getLastSelectedUser().getUsername()
                        .equals(command.getUsername())) {
                    return command.getUsername() + " can't be deleted.";
                }
            }

            // check if any podcasts associated with the host are currently
            // being played by other users
            for (Podcast podcast : currentHost.getPodcasts()) {
                for (User user : users) {
                    PlayerSource userSource = user.getPlayer().getSource();
                    if (userSource != null && userSource.getAudioCollection() == podcast) {
                        return command.getUsername() + " can't be deleted.";
                    }
                }
            }

            // if no podcasts are currently being played, proceed with host deletion
            hosts.remove(currentHost);

            // if the host was deleted successfully, return a success message
            return command.getUsername() + " was successfully deleted.";
        }
    }

    /**
     * Reset.
     */
    public void reset() {
        users = new ArrayList<>();
        artists = new ArrayList<>();
        hosts = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        albums = new ArrayList<>();
        timestamp = 0;
    }
}
