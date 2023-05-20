package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.InvalidException;

class SectionTest {
    @Test
    @DisplayName("생성 테스트")
    void create() {
        // given
        Station upStation = new Station(1L, "잠실역");
        Station downStation = new Station(2L, "잠실새내역");

        // expected
        assertDoesNotThrow(() -> new Section(upStation, downStation, 10));
    }

    @ParameterizedTest(name = "{displayName}[{index}] = ''{0}''")
    @ValueSource(ints = {0, -10})
    @DisplayName("거리가 양수가 아닐 때 예외가 발생한다.")
    void createNotPositiveDistance(int distance) {
        // given
        Station upStation = new Station(1L, "잠실역");
        Station downStation = new Station(2L, "잠실새내역");

        // expected
        assertThatThrownBy(() -> new Section(upStation, downStation, distance))
                .isInstanceOf(InvalidException.class);
    }

    @Test
    @DisplayName("시작역과 도착역이 같을 때 예외가 발생한다.")
    void createDuplicateStations() {
        // given
        Station upStation = new Station(1L, "잠실역");

        // expected
        assertThatThrownBy(() -> new Section(upStation, upStation, 10))
                .isInstanceOf(InvalidException.class);
    }

    @Test
    @DisplayName("역이 구간 사이에 추가될 때 기존 구간의 거리보다 크거나 같으면 예외가 발생한다.")
    void validateDistance() {
        // given
        Station upStation = new Station(1L, "잠실역");
        Station downStation = new Station(2L, "잠실새내역");
        Section originalSection = new Section(upStation, downStation, 10);

        // expected
        assertThatThrownBy(() -> originalSection.validateDistance(10))
                .isInstanceOf(InvalidException.class);
    }

    @Test
    @DisplayName("새로운 구간이 뒤에 추가될 때 나누어지는 구간을 리턴한다.")
    void getDividedSectionBack() {
        // given
        Station upStation = new Station(1L, "잠실역");
        Station downStation = new Station(2L, "잠실새내역");
        Section originalSection = new Section(upStation, downStation, 10);

        Station newUpStation = new Station(3L, "봉천역");
        Section newSection = new Section(newUpStation, downStation, 4);

        // when
        Section dividedSection = originalSection.getDividedSection(newSection);

        // expected
        assertAll(
                () -> assertThat(dividedSection.getDistance()).isEqualTo(6),
                () -> assertThat(dividedSection.getUpStation()).isEqualTo(upStation),
                () -> assertThat(dividedSection.getDownStation()).isEqualTo(newUpStation)
        );
    }

    @Test
    @DisplayName("새로운 구간이 앞에 추가될 때 나누어지는 구간을 리턴한다.")
    void getDividedSectionFront() {
        // given
        Station upStation = new Station(1L, "잠실역");
        Station downStation = new Station(2L, "잠실새내역");
        Section originalSection = new Section(upStation, downStation, 10);

        Station newDownStation = new Station(3L, "봉천역");
        Section newSection = new Section(upStation, newDownStation, 4);

        // when
        Section dividedSection = originalSection.getDividedSection(newSection);

        // expected
        assertAll(
                () -> assertThat(dividedSection.getDistance()).isEqualTo(6),
                () -> assertThat(dividedSection.getUpStation()).isEqualTo(newDownStation),
                () -> assertThat(dividedSection.getDownStation()).isEqualTo(downStation)
        );
    }
}
