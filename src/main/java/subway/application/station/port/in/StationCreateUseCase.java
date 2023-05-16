package subway.application.station.port.in;

public interface StationCreateUseCase {

    StationInfoResponseDto create(StationCreateRequestDto requestDto);
}
