package subway.ui.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreationLineRequest {

    private final String name;
    private final String color;

    @JsonCreator
    private CreationLineRequest(@JsonProperty(value = "name") final String name,
                                @JsonProperty(value = "color") final String color) {
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
