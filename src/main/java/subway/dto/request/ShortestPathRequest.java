package subway.dto.request;

import javax.validation.constraints.NotBlank;

public class ShortestPathRequest {

    @NotBlank(message = "시작역을 입력해주세요")
    private String startStation;

    @NotBlank(message = "도착역을 입력해주세요")
    private String endStation;

    public ShortestPathRequest() {
    }

    public ShortestPathRequest(String startStation, String endStation) {
        this.startStation = startStation;
        this.endStation = endStation;
    }

    public String getStartStation() {
        return startStation;
    }

    public String getEndStation() {
        return endStation;
    }
}
