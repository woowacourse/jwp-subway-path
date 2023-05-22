package subway.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.NotBlank;

public class StationRequest {

    @NotBlank(message = "역의 이름을 입력해주세요.")
    private final String name;

    @JsonCreator
    public StationRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
