package subway.domain.path.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import subway.domain.path.domain.LinePath;
import subway.domain.line.dto.LineResponse;
import subway.domain.line.entity.LineEntity;
import subway.domain.station.dto.StationResponse;
import subway.domain.station.entity.StationEntity;

import java.util.List;
import java.util.stream.Collectors;

public class LinePathResponse {

    @JsonProperty("lineDetail")
    private LineResponse lineResponse;
    @JsonProperty("stations")
    private List<StationResponse> stationResponses;

    private LinePathResponse() {
    }

    public LinePathResponse(LineResponse lineResponse, final List<StationResponse> stationResponses) {
        this.lineResponse = lineResponse;
        this.stationResponses = stationResponses;
    }

    public static LinePathResponse of(LinePath linePath) {
        LineEntity lineDetail = linePath.getLineDetail();
        List<StationEntity> stations = linePath.getStations();

        LineResponse lineResponse = new LineResponse(lineDetail.getId(),
                lineDetail.getName(),
                lineDetail.getColor());

        List<StationResponse> stationResponses = stations.stream().map(
                stationEntity -> new StationResponse(stationEntity.getId(), stationEntity.getName())
        ).collect(Collectors.toList());

        return new LinePathResponse(lineResponse, stationResponses);
    }

    public LineResponse getLineDetail() {
        return lineResponse;
    }

    public List<StationResponse> getStations() {
        return stationResponses;
    }
}
