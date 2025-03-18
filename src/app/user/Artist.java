package app.user;

import app.Admin;
import app.audio.Collections.Album;
import app.utils.Enums;
import checker.CheckerConstants;
import fileio.input.CommandInput;
import fileio.input.EventInput;
import fileio.input.MerchInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter@Setter
public final class Artist extends UserEntry {
    private final ArrayList<Album> albums;
    private final ArrayList<EventInput> events;
    private final ArrayList<MerchInput> merch;
    private Integer likes;
    public Artist(final String username, final int age, final String city) {
        super(username, age, city);

        albums = new ArrayList<>();
        setUserType(Enums.UserType.ARTIST);
        events = new ArrayList<>();
        merch = new ArrayList<>();
    }

    /**
     * Retrieves and returns a list of albums associated with the current user
     *
     * @return An ArrayList containing the albums associated with the current user
     */
    public ArrayList<Album> showAlbums() {
        ArrayList<Album> shownAlbums = new ArrayList<>(this.getAlbums());

        return shownAlbums;
    }

    /**
     * Adds a new event to the artist's list of events based on the provided CommandInput.
     *
     * @param command The input containing the artist's username and details of the event.
     * @return A status message indicating the result of the event addition.
     */
    public static String addEvent(final CommandInput command) {
        Admin admin = Admin.getInstance();
        // check if the specified username exists
        if (!admin.isUser(command.getUsername()) && !admin.isArtist(command.getUsername())
                && !admin.isHost(command.getUsername())) {
            return "The username " + command.getUsername() + " doesn't exist.";
        }

        //verify if the user is an artist
        if (!admin.isArtist(command.getUsername())) {
            return command.getUsername() + " is not an artist.";
        }

        // get the current artist based on the specified username
        Artist currentArtist = admin.getArtist(command.getUsername());
        // verify if the artist already has an album with the same name
        for (EventInput event : currentArtist.getEvents()) {
            if (event.getName().equals(command.getName())) {
                return command.getUsername() + " has another event with the same name.";
            }
        }

        // extract the day, month and year from the specified date
        int day = Integer.parseInt(command.getDate()
                .substring(CheckerConstants.PARSE_DATE_DAY_START,
                        CheckerConstants.PARSE_DATE_DAY_END));
        int month = Integer.parseInt(command.getDate()
                .substring(CheckerConstants.PARSE_DATE_MONTH_START,
                        CheckerConstants.PARSE_DATE_MONTH_END));
        int year = Integer.parseInt(command.getDate().
                substring(CheckerConstants.PARSE_DATE_YEAR_START,
                        CheckerConstants.PARSE_DATE_YEAR_END));

        // verify if the date is valid
        if (day > CheckerConstants.DAYS_IN_MONTH || month > CheckerConstants.MONTHS_IN_YEAR
                || year < CheckerConstants.INFERIOR_YEAR || year > CheckerConstants.SUPERIOR_YEAR
                || (month == CheckerConstants.FEBRUARY
                && day > CheckerConstants.MAX_DAYS_IN_FEBRUARY)) {
            return "Event for " + command.getUsername() + " does not have a valid date.";
        }

        // create a new event based on the specified details and add it to the artist's
        // list of events
        EventInput event = new EventInput(command.getUsername(), command.getName(),
                command.getDescription(), command.getDate());
        currentArtist.getEvents().add(event);

        // return a success message indicating the event addition
        return command.getUsername() + " has added new event successfully.";
    }

    /**
     * Adds new merch to the artist's list of merch based on the provided CommandInput.
     *
     * @param command The input containing the artist's username and details of the merch.
     * @return A status message indicating the result of the merch addition.
     */
    public static String addMerch(final CommandInput command) {
        Admin admin = Admin.getInstance();
        // check if the specified username exists
        if (!admin.isUser(command.getUsername()) && !admin.isArtist(command.getUsername())
                && !admin.isHost(command.getUsername())) {
            return "The username " + command.getUsername() + " doesn't exist.";
        }

        // verify if the user is an artist
        if (!admin.isArtist(command.getUsername())) {
            return command.getUsername() + " is not an artist.";
        }

        // get the current artist based on the specified username
        Artist currentArtist = admin.getArtist(command.getUsername());
        // verify if the artist already has an album with the same name
        for (MerchInput merch : currentArtist.getMerch()) {
            if (merch.getName().equals(command.getName())) {
                return command.getUsername() + " has merchandise with the same name.";
            }
        }

        // verify if the price is valid
        if (command.getPrice() < 0) {
            return "Price for merchandise can not be negative.";
        }

        // create a new merch based on the specified details and add it
        // to the artist's list of merch
        MerchInput merch = new MerchInput(command.getUsername(), command.getName(),
                command.getDescription(), command.getPrice());
        currentArtist.getMerch().add(merch);

        // return a success message indicating the merch addition
        return command.getUsername() + " has added new merchandise successfully.";
    }

    /**
     * Removes an event from the artist's list of events based on the provided CommandInput.
     *
     * @param commandInput The input containing the artist's username and the name of the
     *                     event to be removed.
     * @return A status message indicating the result of the event removal.
     */
    public static String removeEvent(final CommandInput commandInput) {
        Admin admin = Admin.getInstance();
        // check if the specified username exists
        if (!admin.isUser(commandInput.getUsername())
                && !admin.isArtist(commandInput.getUsername())
                && !admin.isHost(commandInput.getUsername())) {
            return "The username " + commandInput.getUsername() + " doesn't exist.";
        }

        // verify if the user is an artist
        if (!admin.isArtist(commandInput.getUsername())) {
            return commandInput.getUsername() + " is not an artist.";
        }

        // get the current artist based on the specified username
        Artist currentArtist = admin.getArtist(commandInput.getUsername());

        // verify if the artist already has an event with the specified name
        boolean found = false;
        EventInput deletedEvent = null;
        for (EventInput event : currentArtist.getEvents()) {
            if (event.getName().equals(commandInput.getName())) {
                found = true;
                deletedEvent = event;

                break;
            }
        }

        // if the event is not found, return an error message
        if (!found) {
            return commandInput.getUsername() + " doesn't have an event with the given name.";
        }

        // remove the event from the artist's list of events
        currentArtist.getEvents().remove(deletedEvent);

        // return a success message indicating the event removal
        return commandInput.getUsername() + " deleted the event successfully.";
    }
}
