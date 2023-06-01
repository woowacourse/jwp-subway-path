package subway.persistence.dao;

import org.junit.jupiter.api.Test;
import subway.domain.Station;
import subway.exception.StationNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StationDaoTest extends DaoTest {

    @Test
    void 역을_조회한다() {
        final Station station = stationDao.findById(1L);

        assertThat(station).isEqualTo(new Station(1L, "수원"));
    }

    @Test
    void 역을_추가한다() {
        final Station insert = stationDao.insert(new Station("추가"));
        final Long id = insert.getId();
        final Station station = stationDao.findById(id);

        assertThat(insert).isEqualTo(station);
    }

    @Test
    void 역을_수정한다() {
        stationDao.update(new Station(1L, "수정"));

        final Station station = stationDao.findById(1L);

        assertThat(station).isEqualTo(new Station(1L, "수정"));
    }

    @Test
    void 모든_역을_조회한다() {
        final List<Station> all = stationDao.findAll();

        assertThat(all).hasSize(5);
    }

    @Test
    void 역을_삭제한다() {
        stationDao.deleteById(1L);

        final List<Station> all = stationDao.findAll();

        assertThat(all).hasSize(4);
    }

    @Test
    void 없는_역을_삭제한다() {
        stationDao.deleteById(1L);
        assertThatThrownBy(() -> stationDao.deleteById(1L))
                .isInstanceOf(StationNotFoundException.class)
                .hasMessageContaining("존재하지 않는 역입니다.");
    }

}
