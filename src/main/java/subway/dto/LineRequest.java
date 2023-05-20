package subway.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

public class LineRequest {

    @NotBlank
    @Size(max = 10, message = "노선의 이름 최대 {max}자 까지만 가능합니다.")
    private String name;

    @NotBlank
    private String color;

    @NotNull
    @Range(min = 0, message = "추가요금은 {min} 이상이여야 합니다.")
    private Integer additionalFare;

    LineRequest() {
    }

    public LineRequest(String name, String color, Integer additionalFare) {
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Integer getAdditionalFare() {
        return additionalFare;
    }
}
