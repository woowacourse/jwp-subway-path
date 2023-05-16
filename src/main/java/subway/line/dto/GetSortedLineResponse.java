package subway.line.dto;

import lombok.*;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class GetSortedLineResponse {
    private final List<String> sortedStations;
}
