package fileio.input;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class AnnouncementInput {
    private String owner;
    private String name;
    private String description;

    public AnnouncementInput(final String owner, final String name, final String description) {
        this.owner = owner;
        this.name = name;
        this.description = description;
    }
}
