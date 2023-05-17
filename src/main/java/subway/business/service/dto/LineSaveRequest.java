package subway.business.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class LineSaveRequest {
    @Schema(description = "등록할 노선의 이름")
    private final String name;

    @Schema(description = "초기 지정될 상행 종점역 이름")
    private final String upwardTerminus;

    @Schema(description = "초기 지정될 하행 종점역 이름")
    private final String downwardTerminus;

    @Schema(description = "초기 지정될 종점역 간 거리")
    private final int distance;

    public LineSaveRequest(String name, String upwardTerminus, String downwardTerminus, int distance) {
        this.name = name;
        this.upwardTerminus = upwardTerminus;
        this.downwardTerminus = downwardTerminus;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public String getUpwardTerminus() {
        return upwardTerminus;
    }

    public String getDownwardTerminus() {
        return downwardTerminus;
    }

    public int getDistance() {
        return distance;
    }
}
