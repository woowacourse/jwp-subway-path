package subway.application.strategy.insert;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.application.strategy.StrategyFixture;
import subway.dao.SectionDao;
import subway.domain.Distance;
import subway.domain.Sections;
import subway.domain.Station;
import subway.repository.SectionRepository;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
class InsertStrategyTest extends StrategyFixture {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;
    private InsertMiddlePoint insertMiddlePoint;
    private Sections sections;

    @BeforeEach
    void init() {
        final SectionRepository sectionRepository = new SectionRepository(new SectionDao(jdbcTemplate, dataSource));
        final InsertUpPointStrategy insertUpPointStrategy = new InsertUpPointStrategy(sectionRepository);
        final InsertDownPointStrategy insertDownPointStrategy = new InsertDownPointStrategy(sectionRepository);
        final List<InsertStrategy> strategies = List.of(insertUpPointStrategy, insertDownPointStrategy);

        insertMiddlePoint = new InsertMiddlePoint(strategies);

        sections = sectionRepository.findAllByLineId(이호선);
    }

    @Test
    void 중간에_추가하는데_상행역_기준인_경우() {
        // given
        final Station 하행역 = stationDao.insert(new Station("하행역"));
        final InsertSection insertSection = new InsertSection(잠실역, 하행역, Distance.from(3));

        // when
        final Long id = insertMiddlePoint.insert(sections, insertSection);

        // then
        assertThat(id).isNotNull();
    }

    @ParameterizedTest(name = "중간에 추가하는데 상행역 기준이면서 거리가 기존 보다 클 때 - 거리 : {0}")
    @ValueSource(ints = {10, 11})
    void 중간에_추가하는데_상행역_기준이면서_거리가_기존_보다_클_때(final int newDistance) {
        // given
        final Station 하행역 = stationDao.insert(new Station("하행역"));
        final InsertSection insertSection = new InsertSection(잠실역, 하행역, Distance.from(newDistance));

        // when, then
        assertThatThrownBy(() -> insertMiddlePoint.insert(sections, insertSection))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 중간에_추가하는데_하행역_기준인_경우() {
        // given
        final Station 상행역 = stationDao.insert(new Station("상행역"));
        final InsertSection insertSection = new InsertSection(상행역, 잠실새내역, Distance.from(3));

        // when
        final Long id = insertMiddlePoint.insert(sections, insertSection);

        // then
        assertThat(id).isNotNull();
    }

    @ParameterizedTest(name = "중간에 추가하는데 하행역 기준이면서 기존 거리보다 클 때 - 거리 : {0}")
    @ValueSource(ints = {10, 11})
    void 중간에_추가하는데_하행역_기준이면서_기존_거리보다_클_때(final int distance) {
        // given
        final Station 상행역 = stationDao.insert(new Station("상행역"));
        final InsertSection insertSection = new InsertSection(상행역, 잠실새내역, Distance.from(distance));

        // when, then
        assertThatThrownBy(() -> insertMiddlePoint.insert(sections, insertSection))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
