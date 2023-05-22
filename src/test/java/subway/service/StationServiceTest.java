package subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.controller.dto.request.StationCreateRequest;
import subway.controller.dto.response.StationResponse;
import subway.domain.station.Station;
import subway.exception.InvalidStationException;
import subway.exception.InvalidStationNameException;
import subway.repository.StationRepository;

@ExtendWith(MockitoExtension.class)
class StationServiceTest {

    @InjectMocks
    private StationService stationService;

    @Mock
    private StationRepository stationRepository;

    @Nested
    @DisplayName("역 생성 시 ")
    class CreateStation {

        @Test
        @DisplayName("정보가 유효하면 역을 생성한다.")
        void createStation() {
            final StationCreateRequest request = new StationCreateRequest("잠실역");
            final Station station = new Station(1L, "잠실역");
            given(stationRepository.save(any(Station.class))).willReturn(station);

            final Long stationId = stationService.createStation(request);

            assertThat(stationId).isEqualTo(1L);
        }

        @Test
        @DisplayName("역 이름이 유효하지 않으면 예외를 던진다.")
        void createStationWithInvalidName() {
            final StationCreateRequest request = new StationCreateRequest("잠실");

            assertThatThrownBy(() -> stationService.createStation(request))
                    .isInstanceOf(InvalidStationNameException.class);
        }
    }

    @Nested
    @DisplayName("역을 조회 시 ")
    class FindStationById {

        @Test
        @DisplayName("존재하는 역일시 역 정보를 반환한다.")
        void findStationById() {
            final Station station = new Station(1L, "잠실역");
            given(stationRepository.findById(1L)).willReturn(station);

            final StationResponse result = stationService.findStationById(1L);

            assertThat(result).usingRecursiveComparison().isEqualTo(StationResponse.from(station));
        }

        @Test
        @DisplayName("존재하지 않는 역일시 예외를 던진다.")
        void findStationByInvalidId() {
            final InvalidStationException exception = new InvalidStationException("존재하지 않는 역입니다.");
            willThrow(exception).given(stationRepository).findById(anyLong());

            assertThatThrownBy(() -> stationService.findStationById(1L))
                    .isInstanceOf(InvalidStationException.class)
                    .hasMessage("존재하지 않는 역입니다.");
        }
    }
}
