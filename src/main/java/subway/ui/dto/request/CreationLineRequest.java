package subway.ui.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class CreationLineRequest {

    @NotBlank
    private final String name;
    @NotBlank
    private final String color;

    @JsonCreator
    private CreationLineRequest(@JsonProperty("name") final String name,
                                @JsonProperty("color") final String color) {
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
