package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.domain.core.Section;
import subway.domain.path.Path;
import subway.domain.path.SectionEdge;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SubwayFarePolicyTest {

    private FarePolicy farePolicy = new SubwayFarePolicy(List.of(
            new BaseFarePolicy(),
            new DistanceFarePolicy(),
            new AgeDiscountFarePolicy()
    ));

    @Test
    void 최종_운임을_계산한다() {
        // given
        final Path path = new Path(List.of(
                new SectionEdge(new Section("A", "B", 5), 300, 1),
                new SectionEdge(new Section("B", "C", 10), 300, 1),
                new SectionEdge(new Section("C", "T", 10), 500, 2),
                new SectionEdge(new Section("T", "D", 10), 300, 1)
        ));
        final Passenger passenger = new Passenger(17);

        // when
        final int result = farePolicy.calculate(path, passenger, 0);

        // then
        assertThat(result).isEqualTo(1520);
    }
}
