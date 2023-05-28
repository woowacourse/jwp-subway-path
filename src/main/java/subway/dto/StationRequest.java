package subway.dto;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class StationRequest {

    @NotNull(message = "역 이름을 입력해주세요")
    @Size(max = 10, message = "역이름은 10자 이하로 입력해주세요")
    private String name;

    public StationRequest() {
    }

    public StationRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
