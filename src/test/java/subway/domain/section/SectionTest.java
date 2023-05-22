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
        assertThat(잠실_선릉.equalToSource(Station.create(name)))
            .isSameAs(expected);
    }

    @ParameterizedTest(name = "구간의 도착지와 같으면 true, 다르면 false를 반환한다.")
    @CsvSource(value = {"선릉역:true", "잠실역:false"}, delimiter = ':')
    void equalToTarget(final String name, final boolean expected) {
        assertThat(잠실_선릉.equalToTarget(Station.create(name)))
            .isSameAs(expected);
    }

    @ParameterizedTest(name = "두 구간이 동일한 구간이면 true, 아니면 false를 반환한다.")
    @CsvSource(value = {"잠실역:선릉역:true", "잠실역:강남역:false", "강남역:선릉역:false"}, delimiter = ':')
    void isSameSection(final String sourceName, final String targetName, final boolean expected) {
        // given
        final Station sourceStation = Station.create(sourceName);
        final Station targetStation = Station.create(targetName);
        final Section section = new Section(sourceStation, targetStation, SectionDistance.zero());

        // expected
        assertThat(잠실_선릉.isSameSection(section))
            .isSameAs(expected);
    }
}
