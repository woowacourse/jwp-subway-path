package subway.application.station.port.in;

public interface StationFindByIdUseCase {

    StationInfoResponseDto findStationInfoById(long id);
}
