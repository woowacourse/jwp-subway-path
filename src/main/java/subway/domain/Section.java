package subway.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Section {
    private Line line;
    private Station preStation;
    private Station station;
    private Long distance;
}
