package subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.DuplicatedStationNameException;
import subway.repository.SimpleStationRepository;
import subway.repository.StationRepository;

class StationServiceTest {

    private StationService stationService;
    private StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        stationRepository = new SimpleStationRepository();
        stationService = new StationService(stationRepository);
    }

    @Test
    @DisplayName("역을 추가한다.")
    void createStation() {
        //give
        final String name = "잠실";

        //when
        final Long createdId = stationService.create(name);

        //then
        assertThat(createdId).isEqualTo(1L);
    }

    @Test
    @DisplayName("중복된 이름의 역을 추가한다.")
    void createStationWithDuplicatedName() {
        //given
        final String name = "잠실";
        stationService.create(name);
        //when
        //then
        assertThatThrownBy(() -> stationService.create(name))
                .isInstanceOf(DuplicatedStationNameException.class);
    }

}