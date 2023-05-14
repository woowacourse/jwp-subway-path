package subway.domain.station.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StationDeletedEvent {

    private final long id;
}
