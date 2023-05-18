package subway.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class StationRequest {

    @Size(min = 1, max = 15, message = "역 이름은 1자 이상 15자 이하만 가능합니다.")
    @NotBlank(message = "역 이름은 공백을 입력할 수 없습니다.")
    private String name;

    public StationRequest() {
    }

    public StationRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
