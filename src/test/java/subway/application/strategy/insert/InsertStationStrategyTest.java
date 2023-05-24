package subway.application.strategy.insert;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import subway.application.strategy.StrategyFixture;
import subway.dao.SectionStationDao;
import subway.domain.Distance;
import subway.domain.Station;
import subway.domain.section.SingleLineSections;
import subway.repository.SectionRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@Import({SectionRepository.class, SectionStationDao.class})
class InsertStationStrategyTest extends StrategyFixture {

    @Autowired
    private SectionRepository sectionRepository;
    private SectionInserter sectionInserter;
    private SingleLineSections sections;

    @BeforeEach
    void init() {
        final InsertTerminal insertTerminal = new InsertTerminal(sectionRepository);
        final InsertUpwardStation insertUpwardStation = new InsertUpwardStation(sectionRepository);
        final InsertDownwardStation insertDownwardStation = new InsertDownwardStation(sectionRepository);
        final List<InsertStationStrategy> strategies = List.of(insertTerminal, insertUpwardStation, insertDownwardStation);

        sectionInserter = new SectionInserter(strategies);

        sections = sectionRepository.findAllByLineId(이호선);
    }

    @Test
    void 중간에_추가하는데_상행역_기준인_경우() {
        // given
        final Station 하행역 = createStation("하행역");
        final InsertSection insertSection = new InsertSection(잠실역, 하행역, Distance.from(3), 1L);

        // when
        final Long id = sectionInserter.insert(sections, insertSection);

        // then
        assertThat(id).isNotNull();
    }

    @ParameterizedTest(name = "중간에 추가하는데 상행역 기준이면서 거리가 기존 보다 클 때 - 거리 : {0}")
    @ValueSource(ints = {10, 11})
    void 중간에_추가하는데_상행역_기준이면서_거리가_기존_보다_클_때(final int newDistance) {
        // given
        final Station 하행역 = createStation("하행역");
        final InsertSection insertSection = new InsertSection(잠실역, 하행역, Distance.from(newDistance), 1L);

        // when, then
        assertThatThrownBy(() -> sectionInserter.insert(sections, insertSection))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 중간에_추가하는데_하행역_기준인_경우() {
        // given
        final Station 상행역 = createStation("상행역");
        final InsertSection insertSection = new InsertSection(상행역, 잠실새내역, Distance.from(3), 1L);

        // when
        final Long id = sectionInserter.insert(sections, insertSection);

        // then
        assertThat(id).isNotNull();
    }

    @ParameterizedTest(name = "중간에 추가하는데 하행역 기준이면서 기존 거리보다 클 때 - 거리 : {0}")
    @ValueSource(ints = {10, 11})
    void 중간에_추가하는데_하행역_기준이면서_기존_거리보다_클_때(final int distance) {
        // given
        final Station 상행역 = createStation("상행역");
        final InsertSection insertSection = new InsertSection(상행역, 잠실새내역, Distance.from(distance), 1L);

        // when, then
        assertThatThrownBy(() -> sectionInserter.insert(sections, insertSection))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
