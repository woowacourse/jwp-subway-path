package subway.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Section {

    @EqualsAndHashCode.Include
    private Long id;
    private Long lineId;
    private Station upStation;
    private Station downStation;
    private Distance distance;
}
