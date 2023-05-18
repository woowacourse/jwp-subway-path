package subway.route.application.dto;

import javax.validation.constraints.NotNull;

public class RouteReadDto {

    @NotNull(message = "출발역 id를 입력해주세요")
    private Long source;
    @NotNull(message = "도착역 id를 입력해주세요")
    private Long destination;
    @NotNull(message = "나이를 입력해주세요")
    private Integer age;

    public RouteReadDto(Long source, Long destination, Integer age) {
        this.source = source;
        this.destination = destination;
        this.age = age;
    }

    public Long getSource() {
        return source;
    }

    public Long getDestination() {
        return destination;
    }

    public Integer getAge() {
        return age;
    }
}
