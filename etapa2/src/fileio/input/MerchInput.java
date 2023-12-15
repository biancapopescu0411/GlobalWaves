package fileio.input;

import lombok.Getter;

@Getter
public class MerchInput {
    private String owner;
    private String name;
    private String description;
    private Integer price;

    public MerchInput(final String owner, final String name, final String description,
                      final Integer price) {
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
