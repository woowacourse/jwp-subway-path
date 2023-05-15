package subway.line.dto;

import lombok.*;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class SortedLineResponse {
    private final List<String> sortedStations;
}
