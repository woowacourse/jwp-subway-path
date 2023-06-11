package subway.dto.shortestpath;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

public class ShortestPathRequest {

    @NotBlank(message = "출발역을 입력해 주세요.")
    private String startStation;

    @NotBlank(message = "도착역을 입력해 주세요.")
    private String endStation;

    @PositiveOrZero(message = "나이는 음수일 수 없습니다.")
    private int age;

    public ShortestPathRequest() {
    }

    public ShortestPathRequest(String startStation, String endStation, int age) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.age = age;
    }

    public String getStartStation() {
        return startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public int getAge() {
        return age;
    }
}
