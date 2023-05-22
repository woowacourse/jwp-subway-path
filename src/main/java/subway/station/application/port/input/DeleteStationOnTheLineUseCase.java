package subway.station.application.port.input;

public interface DeleteStationOnTheLineUseCase {
    void deleteStationOnTheLine(Long lineId, Long stationId);
}
