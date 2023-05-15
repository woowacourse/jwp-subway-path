package subway.line.presentation.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import subway.line.application.dto.LineCreateCommand;

public class LineCreateRequest {

    @NotBlank(message = "노선 이름을 입력해야 합니다")
    private String lineName;

    @NotBlank(message = "상행 종점역 이름을 입력해야 합니다")
    private String upTerminalName;

    @NotBlank(message = "하행 종점역 이름을 입력해야 합니다")
    private String downTerminalName;

    @NotNull(message = "거리 정보가 있어야 합니다")
    private Integer distance;

    private LineCreateRequest() {
    }

    public LineCreateRequest(final String lineName,
                             final String upTerminalName,
                             final String downTerminalName,
                             final Integer distance) {
        this.lineName = lineName;
        this.upTerminalName = upTerminalName;
        this.downTerminalName = downTerminalName;
        this.distance = distance;
    }

    public LineCreateCommand toCommand() {
        return new LineCreateCommand(lineName, upTerminalName, downTerminalName, distance);
    }

    public String getLineName() {
        return lineName;
    }

    public String getUpTerminalName() {
        return upTerminalName;
    }

    public String getDownTerminalName() {
        return downTerminalName;
    }

    public Integer getDistance() {
        return distance;
    }
}
