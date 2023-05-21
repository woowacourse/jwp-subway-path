package subway.business.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public class SubwayPathResponse {
    @Schema(description = "경로에 대한 요금")
    private final int fare;

    @Schema(description = "전체 경로")
    private final List<LinePathDto> linePathDtos;

    public SubwayPathResponse(int fare, List<LinePathDto> linePathDtos) {
        this.fare = fare;
        this.linePathDtos = linePathDtos;
    }

    public int getFare() {
        return fare;
    }

    public List<LinePathDto> getLinePathDtos() {
        return linePathDtos;
    }
}
