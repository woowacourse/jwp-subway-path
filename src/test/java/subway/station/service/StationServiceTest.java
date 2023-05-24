package subway.station.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.station.domain.Station;
import subway.station.domain.StationRepository;
import subway.station.dto.StationCreateDto;

@ExtendWith(MockitoExtension.class)
class StationServiceTest {

  @Mock
  private StationRepository stationRepository;

  @InjectMocks
  private StationService stationService;

  @Test
  void create() {
    //given
    final String stationName = "잠실역";
    final long expectedId = 1L;
    given(stationRepository.createStation(any())).willReturn(new Station(expectedId, stationName));

    //when
    final Long actualId = stationService.create(new StationCreateDto(stationName));

    //then
    Assertions.assertThat(actualId).isEqualTo(expectedId);
  }
}
