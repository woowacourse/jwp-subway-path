package subway.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
@AllArgsConstructor
public class Section {

    private Long id;
    private Long lineId;
    private Station upStation;
    private Station downStation;
    private Distance distance;
}
