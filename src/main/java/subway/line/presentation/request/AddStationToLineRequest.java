package subway.line.presentation.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import subway.line.application.dto.AddStationToLineCommand;

public class AddStationToLineRequest {

    @NotBlank(message = "노선 이름을 입력해야 합니다")
    private String lineName;

    @NotBlank(message = "상행역 이름을 입력해야 합니다")
    private String upStationName;

    @NotBlank(message = "하행역 이름을 입력해야 합니다")
    private String downStationName;

    @NotNull(message = "거리 정보가 있어야 합니다")
    private Integer distance;

    private AddStationToLineRequest() {
    }

    public AddStationToLineRequest(final String lineName,
                                   final String upStationName,
                                   final String downStationName,
                                   final Integer distance) {
        this.lineName = lineName;
        this.upStationName = upStationName;
        this.downStationName = downStationName;
        this.distance = distance;
    }

    public AddStationToLineCommand toCommand() {
        return new AddStationToLineCommand(lineName, upStationName, downStationName, distance);
    }

    public String getLineName() {
        return lineName;
    }

    public String getUpStationName() {
        return upStationName;
    }

    public String getDownStationName() {
        return downStationName;
    }

    public Integer getDistance() {
        return distance;
    }
}
