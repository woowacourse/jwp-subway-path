package subway.dto;

import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class RouteRequest {

    @NotBlank
    @Length(min = 1, max = 10, message = "역의 이름은 {min}자 ~ {max}자여야 합니다")
    private final String startStation;

    @NotBlank
    @Length(min = 1, max = 10, message = "역의 이름은 {min}자 ~ {max}자여야 합니다")
    private final String endStation;

    public RouteRequest(String startStation, String endStation) {
        this.startStation = startStation;
        this.endStation = endStation;
    }

    public String getStartStation() {
        return startStation;
    }

    public String getEndStation() {
        return endStation;
    }

}
