package subway.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

public class ShortestPathRequest {

    @NotBlank(message = "시작역을 입력해주세요.")
    private final String start;

    @NotBlank(message = "도착역을 입력해주세요.")
    private final String end;

    @PositiveOrZero(message = "나이는 0살 이상이어야 합니다.")
    private final Integer age;

    public ShortestPathRequest(final String start, final String end, final Integer age) {
        this.start = start;
        this.end = end;
        this.age = age;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public Integer getAge() {
        return age;
    }
}
