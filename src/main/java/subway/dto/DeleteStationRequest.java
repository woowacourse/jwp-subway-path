package subway.dto;

import javax.validation.constraints.NotNull;

public class DeleteStationRequest {

    @NotNull(message = "삭제하려는 역 이름을 입력해주세요")
    private String stationName;

    public DeleteStationRequest(String stationName) {
        this.stationName = stationName;
    }

    public DeleteStationRequest() {
    }

    public String getStationName() {
        return stationName;
    }
}
