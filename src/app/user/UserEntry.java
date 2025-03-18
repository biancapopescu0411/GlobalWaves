package app.user;

import app.utils.Enums;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class UserEntry {
    private String username;
    private int age;
    private String city;
    private Enums.UserType userType;

    public UserEntry(final String username, final int age, final String city) {
        this.username = username;
        this.age = age;
        this.city = city;
    }

    /**
     * Checks if the username matches the provided name (case-insensitive).
     *
     * @param name The name to compare against the username.
     * @return true if the username starts with the provided name (case-insensitive),
     * false otherwise.
     */
    public final boolean matchesName(final String name) {
        return getUsername().toLowerCase().startsWith(name.toLowerCase());
    }
}
