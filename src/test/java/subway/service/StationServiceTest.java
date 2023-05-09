package subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.Station;
import subway.exception.DuplicatedStationNameException;
import subway.repository.StationRepository;

@ExtendWith(MockitoExtension.class)
class StationServiceTest {

    @InjectMocks
    private StationService stationService;
    @Mock
    private StationRepository stationRepository;

    @Test
    @DisplayName("역을 추가한다.")
    void createStation() {
        //given
        BDDMockito.given(stationRepository.findByName(Mockito.any(String.class)))
                .willReturn(Optional.empty());
        BDDMockito.given(stationRepository.create(Mockito.any(Station.class)))
                .willReturn(1L);
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
        BDDMockito.given(stationRepository.findByName(Mockito.any(String.class)))
                .willReturn(Optional.of(new Station(name)));

        //when
        //then
        assertThatThrownBy(() -> stationService.create(name))
                .isInstanceOf(DuplicatedStationNameException.class);
    }

}