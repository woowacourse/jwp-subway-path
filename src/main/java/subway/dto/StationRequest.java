package subway.dto;

import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class StationRequest {

    @NotBlank(message = "역의 이름은 비어있을 수 없습니다.")
    @Length(min = 1, max = 10, message = "노선의 이름은 {min}자 ~ {max}자여야 합니다")
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
