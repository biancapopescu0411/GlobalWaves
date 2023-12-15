package fileio.input;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class EventInput {
    private String owner;
    private String name;
    private String description;
    private String date;

    public EventInput(final String owner, final String name, final String description,
                      final String date) {
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.date = date;
    }
}
