package subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.domain.section.Distance;
import subway.domain.station.Station;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings({"NonAsciiCharacters", "SpellCheckingInspection"})
class PathSectionsTest {

    PathSections pathSections;

    @BeforeEach
    void setUp() {
        final Station firstStation = Station.of(1L, "1역");
        final Station secondStation = Station.of(2L, "2역");
        final Station thirdStation = Station.of(3L, "3역");
        final Distance distance = Distance.from(5);

        final PathSection firstPathSection = PathSection.of(firstStation, secondStation, distance);
        final PathSection secondPathSection = PathSection.of(secondStation, thirdStation, distance);
        pathSections = PathSections.from(List.of(firstPathSection, secondPathSection));
    }

    @Test
    void from_메소드는_비어_있는_pathSection_collection을_전달하면_예외가_발생한다() {
        assertThatThrownBy(() -> PathSections.from(Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("출발 역에서 도착 역까지 이동할 수 있는 경로가 없습니다.");
    }

    @Test
    void calculateSectionDistance_메소드는_현재_경로의_모든_거리의_합을_계산해_반환한다() {
        final int actual = pathSections.calculateSectionDistance();

        assertThat(actual).isEqualTo(10);
    }
}
