package subway.presentation.dto.response;

import subway.application.dto.CreationLineDto;

public class CreateLineResponse {

    private final Long id;
    private final String name;
    private final String color;

    private CreateLineResponse(final Long id, final String name, final String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static CreateLineResponse from(final CreationLineDto lineDto) {
        return new CreateLineResponse(lineDto.getId(), lineDto.getName(), lineDto.getColor());
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
