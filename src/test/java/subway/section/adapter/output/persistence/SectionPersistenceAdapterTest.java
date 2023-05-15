package subway.section.adapter.output.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.line.adapter.output.persistence.LineDao;
import subway.line.adapter.output.persistence.LineEntity;
import subway.section.domain.Section;
import subway.station.adapter.output.persistence.StationDao;
import subway.station.adapter.output.persistence.StationEntity;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class SectionPersistenceAdapterTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private SectionPersistenceAdapter adapter;
    private LineDao lineDao;
    private SectionDao sectionDao;
    private StationDao stationDao;
    
    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate);
        sectionDao = new SectionDao(jdbcTemplate);
        stationDao = new StationDao(jdbcTemplate);
        adapter = new SectionPersistenceAdapter(stationDao, sectionDao);
    }
    
    @Test
    void 여러_구간_저장하기() {
        // given
        final Long lineId = lineDao.insert(new LineEntity("1호선", "파랑"));
        stationDao.insert(new StationEntity("잠실역"));
        stationDao.insert(new StationEntity("선릉역"));
        stationDao.insert(new StationEntity("청라역"));
        final Section section1 = new Section("잠실역", "선릉역", 5L);
        final Section section2 = new Section("선릉역", "청라역", 3L);
        
        // expect
        assertThatNoException()
                .isThrownBy(() -> adapter.saveAll(Set.of(section1, section2), lineId));
    }
    
    @Test
    void 구간_저장하기() {
        // given
        final Long lineId = lineDao.insert(new LineEntity("1호선", "파랑"));
        stationDao.insert(new StationEntity("잠실역"));
        stationDao.insert(new StationEntity("선릉역"));
        
        // when
        final Section section = new Section("잠실역", "선릉역", 5L);
        final Long id = adapter.save(section, lineId);
        
        // then
        assertThat(id).isPositive();
    }
    
    @Test
    void lineId로_구간_삭제하기() {
        // given
        final Long lineId = lineDao.insert(new LineEntity("1호선", "파랑"));
        stationDao.insert(new StationEntity("잠실역"));
        stationDao.insert(new StationEntity("선릉역"));
        stationDao.insert(new StationEntity("청라역"));
        final Section section1 = new Section("잠실역", "선릉역", 5L);
        final Section section2 = new Section("선릉역", "청라역", 3L);
        adapter.saveAll(Set.of(section1, section2), lineId);
        
        // expect
        assertThatNoException()
                .isThrownBy(() -> adapter.deleteSectionByLineId(lineId));
    }
}
