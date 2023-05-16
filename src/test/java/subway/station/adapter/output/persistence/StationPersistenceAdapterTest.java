package subway.station.adapter.output.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.station.domain.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class StationPersistenceAdapterTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private StationPersistenceAdapter adapter;
    
    @BeforeEach
    void setUp() {
        final StationDao stationDao = new StationDao(jdbcTemplate);
        adapter = new StationPersistenceAdapter(stationDao);
    }
    
    @Test
    void 역들을_저장하기() {
        // given
        final Station station1 = new Station("잠실역");
        final Station station2 = new Station("선릉역");
        
        // expect
        assertThatNoException()
                .isThrownBy(() -> adapter.saveAll(List.of(station1, station2)));
    }
    
    @Test
    void id로_역을_가져오기() {
        // given
        final String stationName = "잠실역";
        final Station station = new Station(stationName);
        final Long id = adapter.saveStation(station);
        
        // when
        final Station stationById = adapter.getStationById(id);
        
        // then
        assertThat(stationById).isEqualTo(new Station(stationName));
    }
}
