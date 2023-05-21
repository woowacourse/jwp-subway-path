package subway.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ShortestPathRequest {

    @NotBlank(message = "시작역을 입력해주세요")
    private String startStation;

    @NotBlank(message = "도착역을 입력해주세요")
    private String endStation;

    @NotNull(message = "나이를 입력해주세요")
    private Integer age;

    public ShortestPathRequest() {
    }

    public ShortestPathRequest(String startStation, String endStation, Integer age) {
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

    public Integer getAge() {
        return age;
    }
}
