package subway.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class StationRequest {

    @NotBlank
    @Size(max = 10, message = "역 이름 최대 {max}자 까지만 가능합니다.")
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
