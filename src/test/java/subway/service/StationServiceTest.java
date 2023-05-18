package subway.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.station.Station;
import subway.exception.DuplicatedStationNameException;
import subway.repository.StationRepository;
import subway.service.dto.StationDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class StationServiceTest {

    @Autowired
    private StationService stationService;

    @Autowired
    private StationRepository stationRepository;


    @Test
    @DisplayName("역을 추가한다.")
    void createStation() {
        //give
        final String name = "잠실";

        //when
        final Long createdId = stationService.create(name);

        //then
        assertThat(createdId).isNotNull();
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

    @Test
    @DisplayName("id로 순으로 역을 조회한다.")
    void findById() {
        //given
        Long id1 = stationRepository.create(new Station("잠실"));
        Long id2 = stationRepository.create(new Station("성수"));
        Long id3 = stationRepository.create(new Station("건대"));

        //when
        List<StationDto> stations = stationService.findById(List.of(id1, id2, id3));

        //then
        assertSoftly(softly -> {
            softly.assertThat(stations.get(0).getName()).isEqualTo("잠실");
            softly.assertThat(stations.get(1).getName()).isEqualTo("성수");
            softly.assertThat(stations.get(2).getName()).isEqualTo("건대");
        });
    }
}
