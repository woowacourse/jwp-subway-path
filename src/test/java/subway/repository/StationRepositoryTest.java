package subway.repository;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dao.StationDao;
import subway.domain.station.Station;
@JdbcTest
class StationRepositoryTest {

    StationRepository stationRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        stationRepository = new DbStationRepository(new StationDao(jdbcTemplate));
    }

    @Test
    @DisplayName("역을 추가한다.")
    void create() {
        //given
        final Station station = new Station("잠실");
        //when
        Long id = stationRepository.create(station);
        //then
        assertThat(id).isNotNull();
    }


    @Test
    @DisplayName("이름으로 역을 찾는다.")
    void findByName() {
        //given
        final Station station = new Station("잠실");
        stationRepository.create(station);

        final String name = "잠실";

        //when
        Station result = stationRepository.findByName(name).get();
        //then
        assertThat(result.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("id로 역을 찾는다.")
    void findById() {
        //given
        final Station station = new Station("잠실");
        Long id = stationRepository.create(station);

        final String name = "잠실";

        //when
        Station result = stationRepository.findById(id).get();
        //then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isEqualTo(id);
            softly.assertThat(result.getName()).isEqualTo(name);
        });
    }

    @Test
    @DisplayName("id 순으로 역을 찾는다.")
    void findByIdList() {
        //given
        Long id1 = stationRepository.create(new Station("잠실"));
        Long id2 = stationRepository.create(new Station("성수"));
        Long id3 = stationRepository.create(new Station("건대"));

        //when
        List<Station> stations = stationRepository.findById(List.of(id1, id2, id3));

        //then
        assertSoftly(softly -> {
            softly.assertThat(stations.get(0).getName()).isEqualTo("잠실");
            softly.assertThat(stations.get(1).getName()).isEqualTo("성수");
            softly.assertThat(stations.get(2).getName()).isEqualTo("건대");
        });
    }
}
