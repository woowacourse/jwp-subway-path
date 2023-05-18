package subway.application.port.in.line;

import subway.application.dto.StationResponse;

import java.util.List;

public interface FindLineUseCase {
    List<StationResponse> findAllByLine(final Long lineId);

    List<List<StationResponse>> findAllLine();
}
