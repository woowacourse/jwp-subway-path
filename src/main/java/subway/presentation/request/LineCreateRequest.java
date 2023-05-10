package subway.presentation.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import subway.application.dto.LineCreateCommand;

public class LineCreateRequest {

    @NotBlank
    private String lineName;

    @NotBlank
    private String upTerminalName;

    @NotBlank
    private String downTerminalName;

    @NotNull
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
