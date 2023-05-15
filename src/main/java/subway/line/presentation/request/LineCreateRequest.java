package subway.line.presentation.request;

import javax.validation.constraints.NotBlank;
import subway.line.application.dto.LineCreateCommand;

public class LineCreateRequest {

    @NotBlank(message = "노선 이름을 입력해야 합니다")
    private String lineName;

    private Integer surcharge = 0;

    private LineCreateRequest() {
    }

    public LineCreateRequest(final String lineName, final Integer surcharge) {
        this.lineName = lineName;
        this.surcharge = surcharge;
    }

    public LineCreateCommand toCommand() {
        return new LineCreateCommand(lineName, surcharge);
    }

    public String getLineName() {
        return lineName;
    }

    public Integer getSurcharge() {
        return surcharge;
    }
}
