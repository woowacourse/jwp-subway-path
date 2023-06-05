package subway.station.application.port.in;

public interface StationCreateUseCase {

    StationInfoResponseDto create(StationCreateRequestDto requestDto);
}
