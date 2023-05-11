package subway.dto;

import java.util.List;

public class StationAddRequests {

    private static final int VALID_SIZE = 2;

    private final List<StationAddRequest> stationAddRequests;

    private StationAddRequests() {
        this.stationAddRequests = null;
    }

    public StationAddRequests(List<StationAddRequest> stationAddRequests) {
        this.stationAddRequests = stationAddRequests;
    }

    private void validateSize(final List<StationAddRequest> stationAddRequests) {
        if (stationAddRequests.size() != VALID_SIZE) {
            throw new IllegalArgumentException(
                "노선에 역을 1개 추가할 때 " + VALID_SIZE + "개의 역의 정보를 입력해주세요.");
        }
    }
}
