package app.user;

import app.Admin;
import app.audio.Collections.Podcast;
import app.utils.Enums;
import fileio.input.AnnouncementInput;
import fileio.input.CommandInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter@Setter
public class Host extends UserEntry {
    private final ArrayList<Podcast> podcasts;
    private final ArrayList<AnnouncementInput> announcements;
    public Host(final String username, final int age, final String city) {
        super(username, age, city);

        setUserType(Enums.UserType.HOST);
        podcasts = new ArrayList<>();
        announcements = new ArrayList<>();
    }

    /**
     * Retrieves and returns a list of podcasts associated with the current user.
     *
     * @return An ArrayList containing the podcasts associated with the current user.
     */
    public final ArrayList<Podcast> showPodcasts() {
        ArrayList<Podcast> shownPodcasts = new ArrayList<>(this.getPodcasts());

        return shownPodcasts;
    }

    /**
     * Adds a new announcement to the host's list of announcements based on the
     * provided CommandInput.
     *
     * @param command The input containing the host's username and details of the announcement.
     * @return A status message indicating the result of the announcement addition.
     */
    public static String addAnnouncement(final CommandInput command) {
        Admin admin = Admin.getInstance();
        // check if the specified username exists
        if (!admin.isUser(command.getUsername()) && !admin.isArtist(command.getUsername())
                && !admin.isHost(command.getUsername())) {
            return "The username " + command.getUsername() + " doesn't exist.";
        }

        // verify if the user is an artist
        if (!admin.isHost(command.getUsername())) {
            return command.getUsername() + " is not a host.";
        }

        // get the current host based on the specified username
        Host currentHost = admin.getHost(command.getUsername());

        // verify if host already has an announcement with the same name
        for (AnnouncementInput announcement : currentHost.getAnnouncements()) {
            if (announcement.getName().equals(command.getName())) {
                return command.getUsername()
                        + " has already added an announcement with this name.";
            }
        }

        // create a new announcement and add it to the host's list of announcements
        AnnouncementInput announcement = new AnnouncementInput(command.getUsername(),
                command.getName(), command.getDescription());
        currentHost.getAnnouncements().add(announcement);

        // return a success message indicating the announcement addition
        return command.getUsername() + " has successfully added new announcement.";
    }

    /**
     * Removes an announcement from the host's list of announcements based on the
     * provided CommandInput.
     *
     * @param command The input containing the host's username and the name of the
     *                announcement to be removed.
     * @return A status message indicating the result of the announcement removal.
     */
    public static String removeAnnouncement(final CommandInput command) {
        Admin admin = Admin.getInstance();
        // check if the specified username exists
        if (!admin.isUser(command.getUsername()) && !admin.isArtist(command.getUsername())
                && !admin.isHost(command.getUsername())) {
            return "The username " + command.getUsername() + " doesn't exist.";
        }

        // verify if the user is an artist
        if (!admin.isHost(command.getUsername())) {
            return command.getUsername() + " is not an host.";
        }

        // get the current host based on the specified username
        Host currentHost = admin.getHost(command.getUsername());

        // verify if the host already has an announcement with the same name
        boolean found = false;
        AnnouncementInput deletedAnnouncement = null;
        for (AnnouncementInput announcement : currentHost.getAnnouncements()) {
            if (announcement.getName().equals(command.getName())) {
                found = true;
                deletedAnnouncement = announcement;
                break;
            }
        }

        // if the announcement was not found, return an error message
        if (!found) {
            return command.getUsername() + " has no announcement with the given name.";
        }

        // remove the announcement from the host's list of announcements
        currentHost.getAnnouncements().remove(deletedAnnouncement);

        // return a success message indicating the announcement removal
        return command.getUsername() + " has successfully deleted the announcement.";
    }
}
