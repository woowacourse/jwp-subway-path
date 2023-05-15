package subway.station.adapter.output.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

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
}
