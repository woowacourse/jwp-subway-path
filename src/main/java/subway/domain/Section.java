package subway.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Section {
    private final Line line;
    private final Station preStation;
    private final Station station;
    private final Long distance;
}
