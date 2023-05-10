package subway.repository;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.Station;

class SimpleStationRepositoryTest {

    StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        stationRepository = new SimpleStationRepository();
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
}