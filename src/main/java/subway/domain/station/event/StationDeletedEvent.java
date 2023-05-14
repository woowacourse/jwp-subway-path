package subway.domain.station.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
public class StationDeletedEvent {

    private final long id;
}
