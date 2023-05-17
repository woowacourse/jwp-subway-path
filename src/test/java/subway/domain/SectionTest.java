package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.domain.SectionFixture.LINE1_SECTION_ST1_ST2;
import static subway.domain.SectionFixture.LINE1_SECTION_ST2_ST3;
import static subway.domain.SectionFixture.LINE1_SECTION_ST4_ST5;
import static subway.domain.StationFixture.FIXTURE_STATION_1;
import static subway.domain.StationFixture.FIXTURE_STATION_2;
import static subway.domain.StationFixture.FIXTURE_STATION_3;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.exception.IllegalDistanceArgumentException;
import subway.domain.exception.IllegalSectionArgumentException;

class SectionTest {

    @DisplayName("구간은 동일한 두 역으로 이루어질 수 없다")
    @Test
    void createFail() {
        assertThatThrownBy(() -> new Section(FIXTURE_STATION_1, FIXTURE_STATION_1, new Distance(1)))
                .isInstanceOf(IllegalSectionArgumentException.class)
                .hasMessageContaining("동일한 역 간 구간을 생성할 수 없습니다.");
    }

    @DisplayName("구간에서 전달받은 구간만큼 뺀 구간을 반환한다 (하행 방향 구간 전달)")
    @Test
    void subtractAddingByDownDirection() {
        Station adding = new Station(7L, "추가역");

        Optional<Section> result = LINE1_SECTION_ST1_ST2.subtract(
                new Section(FIXTURE_STATION_1, adding, new Distance(6)));

        assertThat(result.get())
                .isEqualTo(new Section(adding, FIXTURE_STATION_2, new Distance(4)));
    }

    @DisplayName("구간에서 전달받은 구간만큼 뺀 구간을 반환한다 (상행 방향 구간 전달)")
    @Test
    void subtractAddingByUpDirection() {
        Station adding = new Station(7L, "추가역");

        Optional<Section> result = LINE1_SECTION_ST1_ST2.subtract(
                new Section(adding, FIXTURE_STATION_2, new Distance(6)));

        assertThat(result.get())
                .isEqualTo(new Section(FIXTURE_STATION_1, adding, new Distance(4)));
    }

    @DisplayName("전달받은 구간이 계산하는 구간보다 거리가 크거나 같으면 예외가 발생한다")
    @Test
    void subtractFail() {
        Station adding = new Station(7L, "추가역");

        assertThatThrownBy(() -> LINE1_SECTION_ST1_ST2.subtract(
                new Section(FIXTURE_STATION_1, adding, new Distance(10))))
                .isInstanceOf(IllegalDistanceArgumentException.class)
                .hasMessageContaining("거리는 양의 정수여야 합니다.");
    }

    @DisplayName("전달받은 구간과 겹치는 영역이 없으면 빈 값을 반환한다")
    @Test
    void subtractAddingNoIntersection() {
        Station adding = new Station(7L, "추가역");

        Optional<Section> setOfDifference = LINE1_SECTION_ST1_ST2.subtract(
                new Section(FIXTURE_STATION_2, adding, new Distance(6)));

        assertThat(setOfDifference.isEmpty())
                .isTrue();
    }

    @DisplayName("구간과 전달받은 구간을 합친 구간을 반환한다")
    @Test
    void merge() {
        Optional<Section> result = LINE1_SECTION_ST1_ST2.merge(LINE1_SECTION_ST2_ST3);

        assertThat(result.get())
                .isEqualTo(new Section(FIXTURE_STATION_1, FIXTURE_STATION_3, new Distance(20)));
    }

    @DisplayName("합치기 위한 구간과 전달받은 구간에 상관 없이 합친 구간을 반환한다")
    @Test
    void mergeReverseOrders() {
        Optional<Section> result = LINE1_SECTION_ST2_ST3.merge(LINE1_SECTION_ST1_ST2);

        assertThat(result.get())
                .isEqualTo(new Section(FIXTURE_STATION_1, FIXTURE_STATION_3, new Distance(20)));
    }

    @DisplayName("구간과 전달받은 구간 사이에 교점이 없으면 빈 값을 반환한다")
    @Test
    void mergeNoIntersection() {
        Optional<Section> result = LINE1_SECTION_ST2_ST3.merge(LINE1_SECTION_ST4_ST5);

        assertThat(result.isEmpty())
                .isTrue();
    }
}
