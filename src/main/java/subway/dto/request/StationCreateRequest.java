package subway.dto.request;

import javax.validation.constraints.NotBlank;
import subway.domain.Station;

public class StationCreateRequest {

    @NotBlank(message = "역을 입력해주세요.")
    private String name;

    private StationCreateRequest() {
    }

    public StationCreateRequest(final String name) {
        this.name = name;
    }

    public Station toDomain() {
        return new Station(name);
    }

    public String getName() {
        return name;
    }
}
