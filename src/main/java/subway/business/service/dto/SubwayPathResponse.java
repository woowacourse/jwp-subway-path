package subway.business.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import subway.business.domain.subwaymap.Money;

public class SubwayPathResponse {
    @Schema(description = "경로에 대한 요금")
    private final String fare;

    @Schema(description = "전체 경로")
    private final List<LinePathDto> linePathDtos;

    public SubwayPathResponse(Money fare, List<LinePathDto> linePathDtos) {
        this.fare = fare.getMoney();
        this.linePathDtos = linePathDtos;
    }

    public String getFare() {
        return fare;
    }

    public List<LinePathDto> getLinePathDtos() {
        return linePathDtos;
    }
}
