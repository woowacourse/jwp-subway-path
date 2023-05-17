package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.domain.core.Distance;
import subway.domain.core.Section;
import subway.domain.path.PathFindResult;
import subway.domain.path.SectionEdge;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class BaseFarePolicyTest {

    final FarePolicy baseFarePolicy = new BaseFarePolicy();

    @Test
    void 기본운임은_입력받은_운임에_1250원을_더한값을_반환한다() {
        // given
        final PathFindResult pathFindResult = new PathFindResult(new Distance(9), Collections.emptyList());
        final Passenger passenger = new Passenger();

        // when
        final int result = baseFarePolicy.calculate(pathFindResult, passenger, 0);

        // then
        assertThat(result).isEqualTo(1250);
    }

    @Test
    void 기본운임은_입력받은_운임에_1250원과_노선운임_중_가장높은_값을_더하여_반환한다() {
        // given
        final PathFindResult pathFindResult = new PathFindResult(new Distance(9), List.of(
                new SectionEdge(new Section("A", "B", 5), 100, 1),
                new SectionEdge(new Section("B", "C", 10), 100, 1),
                new SectionEdge(new Section("C", "T", 10), 300, 2),
                new SectionEdge(new Section("T", "D", 10), 100, 1)
        ));
        final Passenger passenger = new Passenger();

        // when
        final int result = baseFarePolicy.calculate(pathFindResult, passenger, 0);

        // then
        assertThat(result).isEqualTo(1550);
    }
}
