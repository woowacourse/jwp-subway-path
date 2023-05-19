package subway.dto.path;

import org.hibernate.validator.constraints.Length;
import subway.validation.annotation.EndsWith;

public class PathRequest {
    @Length(
            min = 2,
            max = 10,
            message = "역의 이름은 {min}글자 이상, {max}글자를 미만이어야 합니다."
    )
    @EndsWith(suffix ="역", message = "역의 이름은 '{suffix}'으로 끝나야 합니다.")
    private String originStationName;

    @Length(
            min = 2,
            max = 10,
            message = "역의 이름은 {min}글자 이상, {max}글자를 미만이어야 합니다."
    )
    @EndsWith(suffix ="역", message = "역의 이름은 '{suffix}'으로 끝나야 합니다.")
    private String destinationStationName;

    public PathRequest(String originStationName, String destinationStationName) {
        this.originStationName = originStationName;
        this.destinationStationName = destinationStationName;
    }

    public String getOriginStationName() {
        return originStationName;
    }

    public String getDestinationStationName() {
        return destinationStationName;
    }
}
