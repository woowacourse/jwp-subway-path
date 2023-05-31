package subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.line.exception.DuplicateStationInLineException;
import subway.line.exception.InvalidAdditionalFareException;
import subway.line.exception.SectionNotFoundException;
import subway.station.domain.Station;
import subway.station.exception.NameLengthException;
import subway.station.exception.StationNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static subway.line.domain.SectionFixture.*;
import static subway.utils.LineFixture.LINE_NUMBER_TWO;
import static subway.utils.StationFixture.*;

class LineTest {

    @ParameterizedTest(name = "이름이 2이상 15이하인 경우 정상 생성된다")
    @ValueSource(strings = {"잠실", "서울대입구서울15자이름입니다"})
    void LineSuccess(String validLineName) {
        assertThatNoException().isThrownBy(
                () -> new Line(validLineName, 0, List.of(JAMSIL_TO_JAMSILNARU))
        );
    }

    @ParameterizedTest(name = "이름이 2이상 15이하가 아니면 예외를 던진다")
    @ValueSource(strings = {"가", "서울대입구서울대16자이름입니다"})
    void LineFail1(String invalidLineName) {
        assertThatThrownBy(() -> new Line(invalidLineName, 0, List.of(JAMSIL_TO_JAMSILNARU)))
                .isInstanceOf(NameLengthException.class);
    }

    @Test
    @DisplayName("추가 요금이 음수이면 예외를 던진다")
    void LineFail2() {
        assertThatThrownBy(() -> new Line(LINE_NUMBER_TWO.getName(), -1, List.of(JAMSIL_TO_JAMSILNARU)))
                .isInstanceOf(InvalidAdditionalFareException.class);
    }

    /**
     * LINE_NUMBER_TWO
     * 선릉 --(거리: 5)--> 잠실 --(거리: 5)--> 잠실나루
     */

    @Test
    @DisplayName("구간 사이에 역을 추가할 수 있다")
    void addStationSuccess1() {
        final Line line = new Line(LINE_NUMBER_TWO);
        final Station stationToAdd = GANGNAM_STATION;

        line.addStation(stationToAdd, SULLEUNG_STATION.getId(), JAMSIL_STATION.getId(), 3);

        assertThat(line.getSections()).containsExactly(
                new MiddleSection(SULLEUNG_STATION, stationToAdd, 3),
                new MiddleSection(stationToAdd, JAMSIL_STATION, DISTANCE - 3),
                JAMSIL_TO_JAMSILNARU
        );
    }

    @Test
    @DisplayName("상행 종점에 역을 추가할 수 있다")
    void addStationSuccess2() {
        Line lineNumberTwo = new Line(LINE_NUMBER_TWO);
        Station stationToAdd = GANGNAM_STATION;

        lineNumberTwo.addStation(stationToAdd, DummyTerminalStation.STATION_ID, SULLEUNG_STATION.getId(), 2);

        assertThat(lineNumberTwo.getSections()).containsExactly(
                new MiddleSection(stationToAdd, SULLEUNG_STATION, 2),
                SULLEUNG_TO_JAMSIL,
                JAMSIL_TO_JAMSILNARU
        );
    }

    @Test
    @DisplayName("하행 종점에 역을 추가할 수 있다")
    void addStationSuccess3() {
        Line lineNumberTwo = new Line(LINE_NUMBER_TWO);
        Station stationToAdd = GANGNAM_STATION;

        lineNumberTwo.addStation(stationToAdd, JAMSIL_NARU_STATION.getId(), DummyTerminalStation.STATION_ID, 2);

        assertThat(lineNumberTwo.getSections()).containsExactly(
                SULLEUNG_TO_JAMSIL,
                JAMSIL_TO_JAMSILNARU,
                new MiddleSection(JAMSIL_NARU_STATION, stationToAdd, 2)
        );
    }

    @Test
    @DisplayName("추가하려는 Station이 이미 존재하는 경우 예외를 던진다")
    void addStationFail1() {
        assertThatThrownBy(() -> LINE_NUMBER_TWO.addStation(JAMSIL_STATION, DummyTerminalStation.STATION_ID, SULLEUNG_STATION.getId(), 2))
                .isInstanceOf(DuplicateStationInLineException.class);
    }

    @Test
    @DisplayName("Upstream과 Downstream이 Section으로 등록되지 않은 경우 예외를 던진다")
    void addStationFail2() {
        Station newStation = new Station("에밀");

        assertSoftly(softly -> {
            assertThatThrownBy(() -> LINE_NUMBER_TWO.addStation(newStation, SULLEUNG_STATION.getId(), JAMSIL_NARU_STATION.getId(), 3))
                    .isInstanceOf(SectionNotFoundException.class);
            assertThatThrownBy(() -> LINE_NUMBER_TWO.addStation(newStation, JAMSIL_STATION.getId(), DummyTerminalStation.STATION_ID, 3))
                    .isInstanceOf(SectionNotFoundException.class);
        });
    }

    @Test
    @DisplayName("노선에서 역을 삭제할 수 있다")
    void deleteStationSuccess1() {
        Line lineNumberTwo = new Line(LINE_NUMBER_TWO);

        lineNumberTwo.deleteStation(JAMSIL_STATION);

        assertThat(lineNumberTwo.getSections()).containsExactly(
                new MiddleSection(SULLEUNG_STATION, JAMSIL_NARU_STATION, 10)
        );
    }

    @Test
    @DisplayName("상행 종점을 삭제할 수 있다")
    void deleteStationSuccess2() {
        Line lineNumberTwo = new Line(LINE_NUMBER_TWO);

        lineNumberTwo.deleteStation(SULLEUNG_STATION);

        assertThat(lineNumberTwo.getSections()).containsExactly(
                new MiddleSection(JAMSIL_STATION, JAMSIL_NARU_STATION, 5)
        );
    }

    @Test
    @DisplayName("하행 종점을 삭제할 수 있다")
    void deleteStationSuccess3() {
        Line lineNumberTwo = new Line(LINE_NUMBER_TWO);

        lineNumberTwo.deleteStation(JAMSIL_NARU_STATION);

        assertThat(lineNumberTwo.getSections()).containsExactly(
                new MiddleSection(SULLEUNG_STATION, JAMSIL_STATION, 5)
        );
    }

    @Test
    @DisplayName("삭제하려는 Station이 없는 경우 예외를 던진다")
    void deleteStationFail1() {
        assertThatThrownBy(() -> LINE_NUMBER_TWO.deleteStation(new Station("메리")))
                .isInstanceOf(StationNotFoundException.class);
    }
}
