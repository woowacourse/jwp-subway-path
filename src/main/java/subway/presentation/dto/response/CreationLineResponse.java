package subway.presentation.dto.response;

import subway.application.dto.CreationLineDto;

public class CreationLineResponse {

    private final Long id;
    private final String name;
    private final String color;

    private CreationLineResponse(final Long id, final String name, final String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static CreationLineResponse from(final CreationLineDto lineDto) {
        return new CreationLineResponse(lineDto.getId(), lineDto.getName(), lineDto.getColor());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
