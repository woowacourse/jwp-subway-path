package subway.domain.subwaymap;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.domain.section.SectionFixture.LINE1;
import static subway.domain.section.SectionFixture.LINE2;
import static subway.domain.section.SectionFixture.SECTIONS1;
import static subway.domain.section.SectionFixture.SECTIONS2;
import static subway.domain.section.SectionFixture.SECTIONS3;
import static subway.domain.section.SectionFixture.SECTIONS4;
import static subway.domain.section.SectionFixture.STATION1;
import static subway.domain.section.SectionFixture.STATION10;
import static subway.domain.section.SectionFixture.STATION11;
import static subway.domain.section.SectionFixture.STATION12;
import static subway.domain.section.SectionFixture.STATION2;
import static subway.domain.section.SectionFixture.STATION3;
import static subway.domain.section.SectionFixture.STATION4;
import static subway.domain.section.SectionFixture.STATION5;
import static subway.domain.section.SectionFixture.STATION6;
import static subway.domain.section.SectionFixture.STATION7;
import static subway.domain.section.SectionFixture.STATION8;
import static subway.domain.section.SectionFixture.STATION9;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import subway.domain.section.Section;

class StationMapTest {

    static Stream<Arguments> getSections() {
        return Stream.of(
                Arguments.of(SECTIONS1),
                Arguments.of(SECTIONS2),
                Arguments.of(SECTIONS3)
        );
    }

    @DisplayName("id에 해당하는 노선의 모든 역 조회")
    @ParameterizedTest
    @MethodSource("getSections")
    void getStations(final List<Section> sections) {
        final StationMap stationMap = StationMap.of(List.of(LINE1), sections);
        assertThat(stationMap.getStations(1L).getStations()).containsExactly(
                STATION1,
                STATION2,
                STATION3,
                STATION4,
                STATION5,
                STATION6,
                STATION7
        );
    }

    @DisplayName("모든 id에 대해 노선의 모든 역 조회")
    @Test
    void getAllStations() {
        final StationMap stationMap = StationMap.of(List.of(LINE1, LINE2), SECTIONS4);
        Assertions.assertAll(
                () -> assertThat(stationMap.getStations(LINE1.getId()).getStations()).containsExactly(
                        STATION1,
                        STATION2,
                        STATION3,
                        STATION4,
                        STATION5,
                        STATION6,
                        STATION7
                ),
                () -> assertThat(stationMap.getStations(LINE2.getId()).getStations()).containsExactly(
                        STATION8,
                        STATION9,
                        STATION10,
                        STATION4,
                        STATION11,
                        STATION12
                )
        );
    }
}
