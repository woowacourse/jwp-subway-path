package subway.ui.dto.request;

import javax.validation.constraints.NotBlank;

public class CreationLineRequest {

    @NotBlank
    private final String name;
    @NotBlank
    private final String color;

    private CreationLineRequest(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public static CreationLineRequest of(final String name, final String color) {
        return new CreationLineRequest(name, color);
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
