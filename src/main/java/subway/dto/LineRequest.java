package subway.dto;

import subway.domain.entity.LineEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class LineRequest {

    @NotBlank(message = "몇 호선인지 입력해주세요.(공백만 입력할 수 없습니다.)")
    private String name;

    @NotBlank(message = "역 색상을 입력해주세요.(공백만 입력할 수 없습니다.)")
    private String color;

    @NotNull(message = "상행 종점과 하행 종점 사이의 거리를 입력해주세요.")
    @Positive(message = "종점 사이 거리는 양수만 입력할 수 있습니다.")
    private int distance;

    public LineRequest() {
    }

    public LineRequest(final String name, final String color, final int distance) {
        this.name = name;
        this.color = color;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getDistance() {
        return distance;
    }

    public LineEntity toEntity() {
        return LineEntity.of(name, color);
    }

}
