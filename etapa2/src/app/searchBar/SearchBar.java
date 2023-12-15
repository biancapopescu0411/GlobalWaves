package app.searchBar;


import app.Admin;
import app.audio.LibraryEntry;
import app.user.UserEntry;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static app.searchBar.FilterUtils.filterLibraryByName;
import static app.searchBar.FilterUtils.filterByAlbum;
import static app.searchBar.FilterUtils.filterByTags;
import static app.searchBar.FilterUtils.filterByLyrics;
import static app.searchBar.FilterUtils.filterByGenre;
import static app.searchBar.FilterUtils.filterByReleaseYear;
import static app.searchBar.FilterUtils.filterByArtist;
import static app.searchBar.FilterUtils.filterByPlaylistVisibility;
import static app.searchBar.FilterUtils.filterByOwner;
import static app.searchBar.FilterUtils.filterByFollowers;
import static app.searchBar.FilterUtils.filterUsersByName;

/**
 * The type Search bar.
 */
@Getter@Setter
public final class SearchBar {
    private List<LibraryEntry> libraryResults;
    private List<UserEntry> userResults;
    private final String user;
    private static final Integer MAX_RESULTS = 5;
    private String lastSearchType;
    private String lastSearchTypeUser;
    private LibraryEntry lastSelected;
    private UserEntry lastSelectedUser;

    /**
     * Instantiates a new Search bar.
     *
     * @param user the user
     */
    public SearchBar(final String user) {
        this.libraryResults = new ArrayList<>();
        this.userResults = new ArrayList<>();
        this.user = user;
    }

    /**
     * Clear selection.
     */
    public void clearSelection() {
        lastSelected = null;
        lastSearchType = null;
        //lastSelectedUser = null;
    }

    /**
     * Search list.
     *
     * @param filters the filters
     * @param type    the type
     * @return the list
     */
    public List<LibraryEntry> searchLibrary(final Filters filters, final String type) {
        Admin admin = Admin.getInstance();
        List<LibraryEntry> entries;

        switch (type) {
            case "song":
                entries = new ArrayList<>(admin.getSongs());

                if (filters.getName() != null) {
                    entries = filterLibraryByName(entries, filters.getName());
                }

                if (filters.getAlbum() != null) {
                    entries = filterByAlbum(entries, filters.getAlbum());
                }

                if (filters.getTags() != null) {
                    entries = filterByTags(entries, filters.getTags());
                }

                if (filters.getLyrics() != null) {
                    entries = filterByLyrics(entries, filters.getLyrics());
                }

                if (filters.getGenre() != null) {
                    entries = filterByGenre(entries, filters.getGenre());
                }

                if (filters.getReleaseYear() != null) {
                    entries = filterByReleaseYear(entries, filters.getReleaseYear());
                }

                if (filters.getArtist() != null) {
                    entries = filterByArtist(entries, filters.getArtist());
                }

                break;
            case "playlist":
                entries = new ArrayList<>(admin.getPlaylists());

                entries = filterByPlaylistVisibility(entries, user);

                if (filters.getName() != null) {
                    entries = filterLibraryByName(entries, filters.getName());
                }

                if (filters.getOwner() != null) {
                    entries = filterByOwner(entries, filters.getOwner());
                }

                if (filters.getFollowers() != null) {
                    entries = filterByFollowers(entries, filters.getFollowers());
                }

                break;
            case "podcast":
                entries = new ArrayList<>(admin.getPodcasts());

                if (filters.getName() != null) {
                    entries = filterLibraryByName(entries, filters.getName());
                }

                if (filters.getOwner() != null) {
                    entries = filterByOwner(entries, filters.getOwner());
                }

                break;
            case "album":
                entries = new ArrayList<>(admin.getAlbums());

                if (filters.getName() != null) {
                    entries = filterLibraryByName(entries, filters.getName());
                }

                if (filters.getOwner() != null) {
                    entries = filterByOwner(entries, filters.getOwner());
                }

                break;
            default:
                entries = new ArrayList<>();
        }

        while (entries.size() > MAX_RESULTS) {
            entries.remove(entries.size() - 1);
        }

        this.libraryResults = entries;
        this.lastSearchType = type;
        return this.libraryResults;
    }

    /**
     * Search for user entries based on specified filters and user type.
     *
     * @param filters the filters to be applied
     * @param type    the type of user to be searched
     * @return the list of users that match the filters
     */
    public List<UserEntry> searchUser(final Filters filters, final String type) {
        Admin admin = Admin.getInstance();
        // list for storing the search results
        List<UserEntry> entries;

        // search for the specified user type
        switch (type) {
            // case for searching artists
            case "artist":
                // get all artists
                entries = new ArrayList<>(admin.getArtists());

                // filter by name if specified
                if (filters.getName() != null) {
                    entries = filterUsersByName(entries, filters.getName());
                }

                break;
            // case for searching hosts
            case "host":
                // get all hosts
                entries = new ArrayList<>(admin.getHosts());

                // filter by name if specified
                if (filters.getName() != null) {
                    entries = filterUsersByName(entries, filters.getName());
                }

                break;
            // default case for unknown user type
            default:
                // initialize empty list
                entries = new ArrayList<>();
        }

        // remove entries that exceed the maximum number of results, which is 5
        while (entries.size() > MAX_RESULTS) {
            entries.remove(entries.size() - 1);
        }

        // store the results
        userResults = new ArrayList<>(entries);
        // store the last search type
        lastSearchTypeUser = type;
        // return the final result
        return userResults;
    }

    /**
     * Select library entry.
     *
     * @param itemNumber the item number
     * @return the library entry
     */
    public LibraryEntry selectLibrary(final Integer itemNumber) {
        if (this.libraryResults.size() < itemNumber) {
            libraryResults.clear();

            return null;
        } else {
            lastSelected =  this.libraryResults.get(itemNumber - 1);
            libraryResults.clear();

            return lastSelected;
        }
    }

    /**
     * Select a user entry based on the specified item number in the search results.
     *
     * @param itemNumber the item number of the user to be selected
     * @return the selected user entry or null if the item number is invalid
     */
    public UserEntry selectUser(final Integer itemNumber) {
        // check if the item number is valid
        if (this.userResults.size() < itemNumber) {
            // if not, clear the search results and return null
            userResults.clear();

            return null;
        } else {
            // if the item number is valid, store the selected user entry
            lastSelectedUser =  this.userResults.get(itemNumber - 1);
            // clear the search results after selecting the user
            userResults.clear();

            // return the selected user entry
            return lastSelectedUser;
        }
    }
}
