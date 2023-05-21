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
class DistanceFarePolicyTest {

    private FarePolicy farePolicy = new DistanceFarePolicy();

    @Test
    void 입력받은_거리가_10키로_이하인_경우_추가운임이_발생하지_않는다() {
        // given
        final Path path = new Path(List.of(
                new SectionEdge(new Section("A", "B", 4), 100, 1),
                new SectionEdge(new Section("B", "C", 5), 100, 1)
        ));
        final Passenger passenger = new Passenger(20);

        // when
        final int result = farePolicy.calculate(path, passenger, 1250);

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(1250);
    }

    @Test
    void 입력받은_거리가_10키로를_초과하고_50키로_이하인_경우_추가운임이_5KM당_100원이_발생한다() {
        // given
        final Path path = new Path(List.of(
                new SectionEdge(new Section("A", "B", 20), 100, 1),
                new SectionEdge(new Section("B", "C", 7), 100, 1)
        ));
        final Passenger passenger = new Passenger(20);

        // when
        final int result = farePolicy.calculate(path, passenger, 1250);

        // then
        assertThat(result).isEqualTo(1650);
    }

    @Test
    void 입력받은_거리가_50키로를_초과하는_경우_추가운임이_8KM당_100원이_발생한다() {
        // given
        final Path path = new Path(List.of(
                new SectionEdge(new Section("A", "B", 60), 100, 1),
                new SectionEdge(new Section("B", "C", 1), 100, 1)
        ));
        final Passenger passenger = new Passenger(20);

        // when
        final int result = farePolicy.calculate(path, passenger, 1250);

        // then
        assertThat(result).isEqualTo(2250);
    }
}
