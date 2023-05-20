package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.domain.SectionFixture.LINE1;
import static subway.domain.SectionFixture.LINE2;
import static subway.domain.SectionFixture.SECTIONS1;
import static subway.domain.SectionFixture.SECTIONS2;
import static subway.domain.SectionFixture.SECTIONS3;
import static subway.domain.SectionFixture.SECTIONS4;
import static subway.domain.SectionFixture.STATION1;
import static subway.domain.SectionFixture.STATION10;
import static subway.domain.SectionFixture.STATION11;
import static subway.domain.SectionFixture.STATION12;
import static subway.domain.SectionFixture.STATION2;
import static subway.domain.SectionFixture.STATION3;
import static subway.domain.SectionFixture.STATION4;
import static subway.domain.SectionFixture.STATION5;
import static subway.domain.SectionFixture.STATION6;
import static subway.domain.SectionFixture.STATION7;
import static subway.domain.SectionFixture.STATION8;
import static subway.domain.SectionFixture.STATION9;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SubwayMapTest {

    static Stream<Arguments> getSections() {
        return Stream.of(
                Arguments.of(SECTIONS1),
                Arguments.of(SECTIONS2),
                Arguments.of(SECTIONS3)
        );
    }

    @DisplayName("노선의 경로 조회 테스트")
    @ParameterizedTest
    @MethodSource("getSections")
    void getStations(final List<Section> sections) {
        final SubwayMap subwayMap = SubwayMap.of(List.of(LINE1), sections);
        assertThat(subwayMap.getStations(1L).getStations()).containsExactly(
                STATION1,
                STATION2,
                STATION3,
                STATION4,
                STATION5,
                STATION6,
                STATION7
        );
    }

    @DisplayName("노선의 경로 조회 테스트")
    @Test
    void getAllStations() {
        final SubwayMap subwayMap = SubwayMap.of(List.of(LINE1, LINE2), SECTIONS4);
        assertThat(subwayMap.getStations(LINE1.getId()).getStations()).containsExactly(
                STATION1,
                STATION2,
                STATION3,
                STATION4,
                STATION5,
                STATION6,
                STATION7
        );
        assertThat(subwayMap.getStations(LINE2.getId()).getStations()).containsExactly(
                STATION8,
                STATION9,
                STATION10,
                STATION4,
                STATION11,
                STATION12
        );
    }
}
