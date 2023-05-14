package subway.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import subway.domain.Station;

@JdbcTest(includeFilters = {
    @Filter(type = FilterType.ANNOTATION, value = Repository.class)
})
class StationRepositoryTest {

    @Autowired
    private StationRepository stationRepository;

    @Test
    @DisplayName("역을 저장한다.")
    void testSave() {
        //given
        final Station station = new Station(1L, "station");

        //when
        final Station savedStation = stationRepository.save(station);

        //then
        assertThat(stationRepository.findById(savedStation.getId())).isPresent();
    }

    @Test
    @DisplayName("이름으로 역을 조회한다.")
    void testFindByName() {
        //given
        final Station station = new Station(1L, "station");
        final Station savedStation = stationRepository.save(station);

        //when
        final Optional<Station> result = stationRepository.findByName(station.getName());

        //then
        assertThat(result).isPresent();
        final Station resultStation = result.get();
        assertThat(resultStation).isEqualTo(savedStation);
    }
}
