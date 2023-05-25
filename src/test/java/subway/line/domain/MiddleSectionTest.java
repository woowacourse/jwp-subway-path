package subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.line.exception.InvalidDistanceException;
import subway.line.exception.InvalidStationNameException;
import subway.station.domain.Station;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static subway.utils.StationFixture.*;

class MiddleSectionTest {

    @Test
    @DisplayName("MiddleSection은 DummyTerminalStation을 인자로 받으면 예외를 발생시킨다")
    void MiddleSectionFail1() {
        assertThatThrownBy(() -> new MiddleSection(DummyTerminalStation.getInstance(), JAMSIL_NARU_STATION, 5))
                .isInstanceOf(InvalidStationNameException.class);
    }

    @ParameterizedTest(name = "구간의 거리가 양의정수가 아니면 예외를 던진다")
    @ValueSource(ints = {0, -1})
    void MiddleSectionFail2(int invalidDistance) {
        assertThatThrownBy(() -> new MiddleSection(JAMSIL_STATION, JAMSIL_NARU_STATION, invalidDistance))
                .isInstanceOf(InvalidDistanceException.class);
    }

    @Test
    @DisplayName("구간의 거리가 양의정수이면 정상 생성된다")
    void MiddleSectionSuccess() {
        assertThatNoException().isThrownBy(() -> new MiddleSection(JAMSIL_STATION, JAMSIL_NARU_STATION, 1));
    }

    @Test
    @DisplayName("upstream과 downstream이 일치한다면 true를 반환한다")
    void isCorrespondingSectionTrue() {
        MiddleSection newMiddleSection = new MiddleSection(JAMSIL_STATION, JAMSIL_NARU_STATION, 3);

        assertThat(newMiddleSection.isCorrespondingSection(JAMSIL_STATION.getId(), JAMSIL_NARU_STATION.getId())).isTrue();
    }

    @Test
    @DisplayName("upstream과 downstream이 순서가 다르면 false를 반환한다")
    void isCorrespondingSectionFalse() {
        MiddleSection newMiddleSection = new MiddleSection(JAMSIL_STATION, JAMSIL_NARU_STATION, 3);

        assertThat(newMiddleSection.isCorrespondingSection(JAMSIL_NARU_STATION.getId(), JAMSIL_STATION.getId())).isFalse();
    }

    @Test
    @DisplayName("Station을 중간에 추가해서 새로운 Section들을 반환한다")
    void insertInTheMiddle() {
        MiddleSection middleSection = new MiddleSection(JAMSIL_STATION, JAMSIL_NARU_STATION, 5);
        Station newStation = new Station("건대입구");

        MiddleSection expectedFirstMiddleSection = new MiddleSection(JAMSIL_STATION, newStation, 2);
        MiddleSection expectedSecondMiddleSection = new MiddleSection(newStation, JAMSIL_NARU_STATION, 3);

        assertThat(middleSection.insertInTheMiddle(newStation, 2))
                .hasSize(2)
                .containsSequence(expectedFirstMiddleSection, expectedSecondMiddleSection);
    }

    @Test
    @DisplayName("기존 section 거리 이상의 거리를 넣을 수 없다")
    void insertInTheMiddleFail1() {
        MiddleSection middleSection = new MiddleSection(JAMSIL_STATION, JAMSIL_NARU_STATION, 5);
        Station newStation = new Station("건대입구");

        assertThatThrownBy(() -> middleSection.insertInTheMiddle(newStation, 5))
                .isInstanceOf(InvalidDistanceException.class);
    }

    @Test
    @DisplayName("DummyTerminalStation을 추가할 수 없다")
    void insertInTheMiddleFail2() {
        MiddleSection middleSection = new MiddleSection(JAMSIL_STATION, JAMSIL_NARU_STATION, 5);
        Station dummyStation = DummyTerminalStation.getInstance();

        assertThatThrownBy(() -> middleSection.insertInTheMiddle(dummyStation, 5))
                .isInstanceOf(InvalidStationNameException.class);
    }

    @Test
    @DisplayName("Section을 병합할 수 있다")
    void merge() {
        MiddleSection middleSection1 = new MiddleSection(SULLEUNG_STATION, JAMSIL_STATION, 5);
        MiddleSection middleSection2 = new MiddleSection(JAMSIL_STATION, JAMSIL_NARU_STATION, 5);

        MiddleSection mergedMiddleSection1 = middleSection1.merge(middleSection2);
        MiddleSection mergedMiddleSection2 = middleSection2.merge(middleSection1);

        assertSoftly(softly -> {
            softly.assertThat(mergedMiddleSection1.getDistance()).isEqualTo(10);
            softly.assertThat(mergedMiddleSection2.getDistance()).isEqualTo(10);
        });
    }

    @Test
    @DisplayName("Section병합시 하나의 Downstream과 다른 하나의 Upstream이 같지 않은 경우 예외를 던진다")
    void mergeFail1() {
        MiddleSection middleSection1 = new MiddleSection(SULLEUNG_STATION, JAMSIL_STATION, 5);
        MiddleSection middleSection2 = new MiddleSection(JAMSIL_NARU_STATION, JAMSIL_STATION, 5);

        assertSoftly(softly -> {
            softly.assertThatThrownBy(() -> middleSection1.merge(middleSection2))
                  .isInstanceOf(IllegalArgumentException.class);
            softly.assertThatThrownBy(() -> middleSection2.merge(middleSection1))
                  .isInstanceOf(IllegalArgumentException.class);
        });
    }
}
