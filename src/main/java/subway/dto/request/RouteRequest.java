package subway.dto.request;

import javax.validation.constraints.NotNull;

public class RouteRequest {

    @NotNull(message = "상행역은 비어있을 수 없습니다. 입력값 : ${validatedValue}")
    private Long sourceStation;

    @NotNull(message = "하행역은 비어있을 수 없습니다. 입력값 : ${validatedValue}")
    private Long targetStation;

    @NotNull(message = "나이는 비어있을 수 없습니다. 입력값 : ${validatedValue}")
    private Integer age;

    public RouteRequest() {
    }

    public RouteRequest(Long sourceStation, Long targetStation, Integer age) {
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.age = age;
    }

    public Long getSourceStation() {
        return sourceStation;
    }

    public Long getTargetStation() {
        return targetStation;
    }

    public Integer getAge() {
        return age;
    }
}
