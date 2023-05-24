package subway.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class PathRequest {

    private static final int DEFAULT_AGE_VALUE = 19;
    @NotNull(message = "출발 역의 아이디를 입력해 주세요. 입력값 : ${validatedValue}")
    private final Long source;

    @NotNull(message = "도착 역의 아이디를 입력해 주세요. 입력값 : ${validatedValue}")
    private final Long target;

    @Min(value = 1, message = "나이는 최소 1살 이상이어야 합니다. 입력값 : ${validatedValue}")
    private final Integer age;

    public PathRequest(final Long source, final Long target, final Integer age) {
        this.source = source;
        this.target = target;
        this.age = age != null ? age : DEFAULT_AGE_VALUE;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public Integer getAge() {
        return age;
    }
}
