package subway.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

public class LineAddRequest {

    @NotBlank(message = "노선명을 입력해주세요.")
    private String name;

    @NotBlank(message = "노선 색을 입력해주세요.")
    private String color;

    @PositiveOrZero(message = "추가 운임은 0원 이상이어야 합니다.")
    private int surcharge;

    public LineAddRequest(final String name, final String color, final int surcharge) {
        this.name = name;
        this.color = color;
        this.surcharge = surcharge;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Integer getSurcharge() {
        return surcharge;
    }
}
