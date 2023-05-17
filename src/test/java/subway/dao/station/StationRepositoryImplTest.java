package subway.dao.station;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import subway.domain.station.Station;
import subway.domain.station.StationRepository;

@SuppressWarnings("NonAsciiCharacters")
@Import({StationDao.class, StationRepositoryImpl.class})
@JdbcTest
class StationRepositoryImplTest {

    @Autowired
    private StationDao stationDao;

    @Autowired
    private StationRepository stationRepository;

    @Test
    void 저장되어있지_않으면_저장한다() {
        //given
        final Station station = new Station("강남");

        //when
        final Station savedStation = stationRepository.saveIfNotExist(station);

        //then
        final List<StationEntity> all = stationDao.findAll();
        assertThat(all).hasSize(1);
        assertThat(savedStation.getStationId()).isNotNull();
    }

    @Test
    void 식별자로_역_조회_테스트() {
        //given
        final Station station = new Station("강남");
        final StationEntity stationEntity = new StationEntity(station.getStationName());
        final Long savedId = stationDao.insert(stationEntity).getStationId();

        //when
        final Optional<Station> findByIdStation = stationRepository.findById(savedId);

        //then
        assertThat(findByIdStation).isPresent();
        final Station actualStation = findByIdStation.get();
        assertThat(actualStation.getStationName()).isEqualTo("강남");
    }
}
