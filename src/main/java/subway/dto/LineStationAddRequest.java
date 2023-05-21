package subway.dto;

import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

public class LineStationAddRequest {

    @NotBlank
    @Length(min = 1, max = 10, message = "역의 이름은 {min}자 ~ {max}자여야 합니다")
    private final String baseStationName;

    @NotBlank
    @Length(min = 1, max = 10, message = "역의 이름은 {min}자 ~ {max}자여야 합니다")
    private final String newStationName;

    @NotBlank
    private final String directionOfBaseStation;

    @Range(min = 1, max = 100000, message = "역 사이의 거리는 {min}이상 {max}이하여야 합니다")
    private final int distance;

    public LineStationAddRequest(String baseStationName, String newStationName, String directionOfBaseStation,
            int distance) {
        this.baseStationName = baseStationName;
        this.newStationName = newStationName;
        this.directionOfBaseStation = directionOfBaseStation;
        this.distance = distance;
    }

    public String getBaseStationName() {
        return baseStationName;
    }

    public String getNewStationName() {
        return newStationName;
    }

    public String getDirectionOfBaseStation() {
        return directionOfBaseStation;
    }

    public int getDistance() {
        return distance;
    }

}
