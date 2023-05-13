package subway.domain.section;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.fixture.SectionFixture.잠실_선릉;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.station.Station;

class SectionTest {

    @ParameterizedTest(name = "구간의 출발지와 같으면 true, 다르면 false를 반환한다.")
    @CsvSource(value = {"잠실역:true", "선릉역:false"}, delimiter = ':')
    void equalToSource(final String name, final boolean expected) {
        assertThat(잠실_선릉.equalToSource(new Station(name)))
            .isSameAs(expected);
    }

    @ParameterizedTest(name = "구간의 도착지와 같으면 true, 다르면 false를 반환한다.")
    @CsvSource(value = {"선릉역:true", "잠실역:false"}, delimiter = ':')
    void equalToTarget(final String name, final boolean expected) {
        assertThat(잠실_선릉.equalToTarget(new Station(name)))
            .isSameAs(expected);
    }
}
