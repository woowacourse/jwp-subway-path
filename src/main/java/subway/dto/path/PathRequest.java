package subway.dto.path;

import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import subway.validation.annotation.EndsWith;

public class PathRequest {
    @NotBlank
    @Length(
            max = 10,
            message = "역의 이름은 {max}글자를 초과할 수 없습니다."
    )
    @EndsWith(suffix ="역", message = "역의 이름은 '{suffix}'으로 끝나야 합니다.")
    private String originStationName;

    @NotBlank
    @Length(
            max = 10,
            message = "역의 이름은 {max}글자를 초과할 수 없습니다."
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
