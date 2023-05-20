package subway.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Section {
    private final Line line;
    private final Station preStation;
    private final Station station;
    private final Distance distance;
}
