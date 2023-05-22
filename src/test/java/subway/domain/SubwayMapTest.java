package subway.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.domain.line.SubwayMap;
import subway.domain.path.SectionEdge;
import subway.domain.station.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.common.fixture.DomainFixture.디노;
import static subway.common.fixture.DomainFixture.디노_로운;
import static subway.common.fixture.DomainFixture.디노_조앤;
import static subway.common.fixture.DomainFixture.로운;
import static subway.common.fixture.DomainFixture.이호선_초록색_침착맨_디노_로운;
import static subway.common.fixture.DomainFixture.일호선_남색_후추_디노_조앤;
import static subway.common.fixture.DomainFixture.조앤;
import static subway.common.fixture.DomainFixture.침착맨;
import static subway.common.fixture.DomainFixture.침착맨_디노;
import static subway.common.fixture.DomainFixture.후추;
import static subway.common.fixture.DomainFixture.후추_디노;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SubwayMapTest {

    @Test
    void 모든_역을_반환한다() {
        //given
        final SubwayMap subwayMap = new SubwayMap(List.of(일호선_남색_후추_디노_조앤, 이호선_초록색_침착맨_디노_로운));

        //when
        final List<Station> stations = subwayMap.getStations();

        //then
        assertThat(stations).containsExactly(후추, 디노, 조앤, 침착맨, 로운);
    }

    @Test
    void 모든_구간을_반환한다() {
        //given
        final SubwayMap subwayMap = new SubwayMap(List.of(일호선_남색_후추_디노_조앤, 이호선_초록색_침착맨_디노_로운));

        //when
        final List<SectionEdge> sectionEdges = subwayMap.getSectionEdge();

        //then
        assertThat(sectionEdges).containsExactly(
                new SectionEdge(1L, 후추_디노),
                new SectionEdge(1L, 디노_조앤),
                new SectionEdge(2L, 침착맨_디노),
                new SectionEdge(2L, 디노_로운)
        );
    }
}
