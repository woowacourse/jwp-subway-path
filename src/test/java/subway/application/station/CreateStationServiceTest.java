package subway.application.station;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import subway.domain.Station;
import subway.domain.repository.StationRepository;
import subway.ui.dto.request.StationCreateRequest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class CreateStationServiceTest {
    private StationRepository stationRepository;
    private CreateStationService createStationService;

    @BeforeEach
    void setUp() {
        stationRepository = Mockito.mock(StationRepository.class);
        createStationService = new CreateStationService(stationRepository);
    }

    @Test
    @DisplayName("역을 정상적으로 만든다.")
    void createStation() {
        given(stationRepository.findByName(any()))
                .willReturn(Optional.empty());

        assertThatNoException().isThrownBy(
                () -> createStationService.createStation(new StationCreateRequest("비버"))
        );
    }

    @Test
    @DisplayName("존재하는 역이면 예외처리")
    void createStationException() {
        given(stationRepository.findByName(any()))
                .willReturn(Optional.of(new Station("라빈")));

        assertThatThrownBy(
                () -> createStationService.createStation(new StationCreateRequest("라빈"))
        ).isInstanceOf(IllegalArgumentException.class);
    }
}