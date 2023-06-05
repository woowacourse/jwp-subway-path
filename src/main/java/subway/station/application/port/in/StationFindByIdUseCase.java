package subway.station.application.port.in;

public interface StationFindByIdUseCase {

    StationInfoResponseDto findStationInfoById(long id);
}
