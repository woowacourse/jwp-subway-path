package subway.domain.line;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.station.StationName;

class LineWithSectionResTest {

    @ParameterizedTest(name = "입력받은 역이 출발역 혹은 도착역이면 true, 아니면 false를 반환한다. (아이디를 기준으로 확인)")
    @CsvSource(value = {"1:true", "2:true", "3:false"}, delimiter = ':')
    void isSourceOrTargetStation_id(final Long stationId, final boolean expected) {
        // given
        final LineWithSectionRes lineWithSectionRes = new LineWithSectionRes(1L, "이호선", "green", 0, 1L,
            "잠실역", 2L, "선릉역", 10);

        // when
        final boolean result = lineWithSectionRes.isSourceOrTargetStation(stationId);

        // then
        assertThat(result)
            .isSameAs(expected);
    }

    @ParameterizedTest(name = "입력받은 역이 출발역 혹은 도착역이면 true, 아니면 false를 반환한다. (이름을 기준으로 확인)")
    @CsvSource(value = {"잠실역:true", "선릉역:true", "강남역:false"}, delimiter = ':')
    void isSourceOrTargetStation_name(final String stationName, final boolean expected) {
        // given
        final LineWithSectionRes lineWithSectionRes = new LineWithSectionRes(1L, "이호선", "green", 0, 1L,
            "잠실역", 2L, "선릉역", 10);

        // when
        final boolean result = lineWithSectionRes.isSourceOrTargetStation(StationName.create(stationName));

        // then
        assertThat(result)
            .isSameAs(expected);
    }

    @Test
    @DisplayName("역 이름을 기준으로 해당 역의 아이디를 조회한다.")
    void getStationIdByStationName() {
        // given
        final LineWithSectionRes lineWithSectionRes = new LineWithSectionRes(1L, "이호선", "green", 0, 1L,
            "잠실역", 2L, "선릉역", 10);

        // when
        final Long stationId = lineWithSectionRes.getStationIdByStationName(StationName.create("잠실역"));

        // then
        assertThat(stationId)
            .isSameAs(1L);
    }

    @Test
    @DisplayName("역 아이디를 기준으로 해당 역의 이름을 조회한다.")
    void getStationNameByStationId() {
        // given
        final LineWithSectionRes lineWithSectionRes = new LineWithSectionRes(1L, "이호선", "green", 0, 1L,
            "잠실역", 2L, "선릉역", 10);

        // when
        final String stationName = lineWithSectionRes.getStationNameByStationId(1L);

        // then
        assertThat(stationName)
            .isEqualTo("잠실역");
    }
}
