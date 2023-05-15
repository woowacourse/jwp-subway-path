package subway.station.adapter.output.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.station.domain.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class StationDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private StationDao stationDao;
    
    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate);
    }
    
    @Test
    void 역_정보_저장하기() {
        // given
        final StationEntity entity = new StationEntity(null, "잠실역");
        
        // when
        final Long stationId = stationDao.insert(entity);
        
        // then
        assertThat(stationId).isPositive();
    }
    
    @Test
    void 모든_역_정보_가져오기() {
        // given
        final String stationName1 = "잠실역";
        final String stationName2 = "선릉역";
        final StationEntity entity1 = new StationEntity(null, stationName1);
        final StationEntity entity2 = new StationEntity(null, stationName2);
        final Long id1 = stationDao.insert(entity1);
        final Long id2 = stationDao.insert(entity2);
        
        // when
        final List<StationEntity> entities = stationDao.findAll();
        
        // then
        assertThat(entities).contains(new StationEntity(id1, stationName1), new StationEntity(id2, stationName2));
    }
    
    @Test
    void 아이디로_역_정보_가져오기() {
        // given
        final String stationName = "잠실역";
        final StationEntity entity = new StationEntity(null, stationName);
        final Long stationId = stationDao.insert(entity);
        
        // when
        final StationEntity result = stationDao.findById(stationId);
        
        // then
        assertThat(result).isEqualTo(new StationEntity(stationId, stationName));
    }
    
    @Test
    void 이름으로_역_정보_가져오기() {
        // given
        final String stationName = "잠실역";
        final StationEntity entity = new StationEntity(null, stationName);
        final Long stationId = stationDao.insert(entity);
        
        // when
        final StationEntity result = stationDao.findByName(new Station(stationName));
        
        // then
        assertThat(result).isEqualTo(new StationEntity(stationId, stationName));
    }
}
