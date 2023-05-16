package subway.line.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class GetAllSortedLineResponse {
    private final List<GetSortedLineResponse> allSortedLines;
}
