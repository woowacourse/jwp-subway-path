package subway.section.adapter.output.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.line.adapter.output.persistence.LineDao;
import subway.line.adapter.output.persistence.LineEntity;
import subway.line.domain.Line;
import subway.line.domain.Subway;
import subway.section.domain.Section;
import subway.station.adapter.output.persistence.StationDao;
import subway.station.adapter.output.persistence.StationEntity;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;

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
        adapter = new SectionPersistenceAdapter(lineDao, sectionDao, stationDao);
    }
    
    @Test
    void 여러_구간_저장하기() {
        // given
        final Long lineId = lineDao.insert(new LineEntity("1호선", "파랑", 0L));
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
        final Long lineId = lineDao.insert(new LineEntity("1호선", "파랑", 0L));
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
        final Long lineId = lineDao.insert(new LineEntity("1호선", "파랑", 0L));
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
    
    @Test
    void lines로_구간_삭제하기() {
        // given
        final Long lineId1 = lineDao.insert(new LineEntity("1호선", "파랑", 0L));
        stationDao.insert(new StationEntity("잠실역"));
        stationDao.insert(new StationEntity("선릉역"));
        stationDao.insert(new StationEntity("청라역"));
        final Section section1 = new Section("잠실역", "선릉역", 5L);
        final Section section2 = new Section("선릉역", "청라역", 3L);
        adapter.saveAll(Set.of(section1, section2), lineId1);
        
        final Long lineId2 = lineDao.insert(new LineEntity("2호선", "초록", 0L));
        stationDao.insert(new StationEntity("신도림역"));
        stationDao.insert(new StationEntity("홍대입구역"));
        stationDao.insert(new StationEntity("사당역"));
        final Section section3 = new Section("신도림역", "홍대입구역", 5L);
        final Section section4 = new Section("홍대입구역", "사당역", 3L);
        adapter.saveAll(Set.of(section3, section4), lineId2);
        
        final Line line1 = new Line("1호선", "파랑", 0L);
        
        // expect
        assertThatNoException()
                .isThrownBy(() -> adapter.deleteSectionByLines(Set.of(line1)));
    }
    
    @Test
    void 해당하는_lines의_Sections들을_모두_저장() {
        // given
        lineDao.insert(new LineEntity("1호선", "파랑", 0L));
        stationDao.insert(new StationEntity("잠실역"));
        stationDao.insert(new StationEntity("선릉역"));
        stationDao.insert(new StationEntity("청라역"));
        final Section section1 = new Section("잠실역", "선릉역", 5L);
        final Section section2 = new Section("선릉역", "청라역", 3L);
        
        lineDao.insert(new LineEntity("2호선", "초록", 0L));
        stationDao.insert(new StationEntity("신도림역"));
        stationDao.insert(new StationEntity("홍대입구역"));
        stationDao.insert(new StationEntity("사당역"));
        final Section section3 = new Section("신도림역", "홍대입구역", 5L);
        final Section section4 = new Section("홍대입구역", "사당역", 3L);
        
        final Line line1 = new Line("1호선", "파랑", Set.of(section1, section2));
        final Line line2 = new Line("2호선", "초록", Set.of(section3, section4));
        
        // expect
        assertThatNoException()
                .isThrownBy(() -> adapter.saveAllSectionsOfLines(Set.of(line1, line2)));
    }
}
