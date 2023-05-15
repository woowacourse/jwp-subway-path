package subway.section.adapter.output.persistence;

import org.assertj.core.api.Assertions;
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

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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
        final Long lineId = lineDao.insert(new LineEntity("1호선", "파랑"));
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
        final Long lineId = lineDao.insert(new LineEntity("1호선", "파랑"));
        final SectionEntity entity = new SectionEntity(stationId1, stationId2, 3L, lineId);
        final Long sectionId = sectionDao.insert(entity);
        
        // when
        final List<SectionEntity> sectionEntities = sectionDao.findAll();
        
        // then
        assertThat(sectionEntities).contains(new SectionEntity(sectionId, stationId1, stationId2, 3L, lineId));
    }
}
