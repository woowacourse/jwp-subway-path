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
    
    private final int ageOfPassenger;
    
    public RouteRequest(String startStation, String endStation, int ageOfPassenger) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.ageOfPassenger = ageOfPassenger;
    }
    
    public String getStartStation() {
        return startStation;
    }

    public String getEndStation() {
        return endStation;
    }
    
    public int getAgeOfPassenger() {
        return ageOfPassenger;
    }
}
