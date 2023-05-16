package subway.line.presentation.request;

import javax.validation.constraints.NotBlank;
import subway.line.application.dto.LineCreateCommand;

public class LineCreateRequest {

    @NotBlank(message = "노선 이름을 입력해야 합니다")
    private String lineName;

    private LineCreateRequest() {
    }

    public LineCreateRequest(final String lineName) {
        this.lineName = lineName;
    }

    public LineCreateCommand toCommand() {
        return new LineCreateCommand(lineName);
    }

    public String getLineName() {
        return lineName;
    }
}
