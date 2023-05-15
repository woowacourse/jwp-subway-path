package subway.station.application.port.input;

public interface DeleteStationUseCase {
    void deleteStation(Long lineId, Long stationId);
}
