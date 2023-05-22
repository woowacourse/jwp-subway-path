package subway.ui.dto.station;

import com.fasterxml.jackson.annotation.JsonCreator;
import javax.validation.constraints.NotBlank;

public class StationCreateRequest {

    @NotBlank(message = "역 이름은 비어있을 수 없습니다.")
    private String name;

    public StationCreateRequest() {
    }

    @JsonCreator
    public StationCreateRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
