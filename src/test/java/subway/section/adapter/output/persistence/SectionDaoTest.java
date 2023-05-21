package subway.section.adapter.output.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.line.adapter.output.persistence.LineDao;
import subway.line.adapter.output.persistence.LineEntity;
import subway.station.adapter.output.persistence.StationDao;
import subway.station.adapter.output.persistence.StationEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class SectionDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private SectionDao sectionDao;
    private LineDao lineDao;
    private StationDao stationDao;
    
    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(jdbcTemplate);
        lineDao = new LineDao(jdbcTemplate);
        stationDao = new StationDao(jdbcTemplate);
    }
    
    @Test
    void 구간_저장하기() {
        // given
        final Long stationId1 = stationDao.insert(new StationEntity("잠실역"));
        final Long stationId2 = stationDao.insert(new StationEntity("선릉역"));
        final Long lineId = lineDao.insert(new LineEntity("1호선", "파랑", 0L));
        final SectionEntity entity = new SectionEntity(stationId1, stationId2, 3L, lineId);
        
        // when
        final Long sectionId = sectionDao.insert(entity);
        
        // then
        assertThat(sectionId).isPositive();
    }
    
    @Test
    void 모든_구간_가져오기() {
        // given
        final Long stationId1 = stationDao.insert(new StationEntity("잠실역"));
        final Long stationId2 = stationDao.insert(new StationEntity("선릉역"));
        final Long lineId = lineDao.insert(new LineEntity("1호선", "파랑", 0L));
        final SectionEntity entity = new SectionEntity(stationId1, stationId2, 3L, lineId);
        final Long sectionId = sectionDao.insert(entity);
        
        // when
        final List<SectionEntity> sectionEntities = sectionDao.findAll();
        
        // then
        assertThat(sectionEntities).contains(new SectionEntity(sectionId, stationId1, stationId2, 3L, lineId));
    }
    
    @Test
    void 노선_id로_구간들_가져오기() {
        // given
        final Long lineId = lineDao.insert(new LineEntity("1호선", "파랑", 0L));
        final Long stationId1 = stationDao.insert(new StationEntity("잠실역"));
        final Long stationId2 = stationDao.insert(new StationEntity("선릉역"));
        final SectionEntity entity1 = new SectionEntity(stationId1, stationId2, 3L, lineId);
        
        final Long stationId3 = stationDao.insert(new StationEntity("가정역"));
        final Long stationId4 = stationDao.insert(new StationEntity("청라역"));
        final SectionEntity entity2 = new SectionEntity(stationId3, stationId4, 3L, lineId);
        final Long id1 = sectionDao.insert(entity1);
        final Long id2 = sectionDao.insert(entity2);
        
        // when
        final List<SectionEntity> entities = sectionDao.findByLineId(lineId);
        
        // then
        final SectionEntity expectEntity1 = new SectionEntity(id1, stationId1, stationId2, 3L, lineId);
        final SectionEntity expectEntity2 = new SectionEntity(id2, stationId3, stationId4, 3L, lineId);
        assertThat(entities).contains(expectEntity1, expectEntity2);
    }
    
    @Test
    void 노선_id로_구간들_삭제하기() {
        // given
        final Long lineId = lineDao.insert(new LineEntity("1호선", "파랑", 0L));
        final Long stationId1 = stationDao.insert(new StationEntity("잠실역"));
        final Long stationId2 = stationDao.insert(new StationEntity("선릉역"));
        final SectionEntity entity1 = new SectionEntity(stationId1, stationId2, 3L, lineId);
        
        final Long stationId3 = stationDao.insert(new StationEntity("가정역"));
        final Long stationId4 = stationDao.insert(new StationEntity("청라역"));
        final SectionEntity entity2 = new SectionEntity(stationId3, stationId4, 3L, lineId);
        sectionDao.insert(entity1);
        sectionDao.insert(entity2);
        
        // when
        sectionDao.deleteByLineId(lineId);
        
        // then
        assertThat(sectionDao.findAll()).isEmpty();
    }
}
