package subway.presentation.dto.request;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class LineRequest {
    @NotNull
    @Length(min = 1, max = 10, message = "노선의 이름은 1글자 이상, 10글자 이하여야 합니다.")
    private String lineName;

    public LineRequest() {
    }

    public LineRequest(String lineName) {
        this.lineName = lineName;
    }

    public String getLineName() {
        return lineName;
    }
}
