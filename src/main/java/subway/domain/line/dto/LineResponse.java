package subway.domain.line.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import subway.domain.line.domain.Line;
import subway.domain.lineDetail.dto.LineDetailResponse;
import subway.domain.lineDetail.entity.LineDetailEntity;
import subway.domain.station.dto.StationResponse;
import subway.domain.station.entity.StationEntity;

import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {

    @JsonProperty("lineDetail")
    private LineDetailResponse lineDetailResponse;
    @JsonProperty("stations")
    private List<StationResponse> stationResponses;

    private LineResponse() {
    }

    public LineResponse(LineDetailResponse lineDetailResponse, final List<StationResponse> stationResponses) {
        this.lineDetailResponse = lineDetailResponse;
        this.stationResponses = stationResponses;
    }

    public static LineResponse of(Line line) {
        LineDetailEntity lineDetail = line.getLineDetail();
        List<StationEntity> stations = line.getStations();

        LineDetailResponse lineDetailResponse = new LineDetailResponse(lineDetail.getId(),
                lineDetail.getName(),
                lineDetail.getColor());

        List<StationResponse> stationResponses = stations.stream().map(
                stationEntity -> new StationResponse(stationEntity.getId(), stationEntity.getName())
        ).collect(Collectors.toList());

        return new LineResponse(lineDetailResponse, stationResponses);
    }

    public LineDetailResponse getLineDetail() {
        return lineDetailResponse;
    }

    public List<StationResponse> getStations() {
        return stationResponses;
    }
}
