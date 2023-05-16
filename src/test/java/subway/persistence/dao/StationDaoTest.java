package subway.persistence.dao;

import org.junit.jupiter.api.Test;
import subway.domain.Station;

import static org.assertj.core.api.Assertions.assertThat;

class StationDaoTest extends DaoTest {

    @Test
    void 역을_조회한다() {
        final Station station = stationDao.findById(1L);

        assertThat(station).isEqualTo(new Station(1L, "수원"));
    }

}
