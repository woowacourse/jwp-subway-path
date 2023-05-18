package subway.application;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.Station;
import subway.persistence.repository.StationRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StationServiceTest {

    @InjectMocks
    StationService stationService;

    @Mock
    StationRepository stationRepository;

    @Test
    void 역을_저장하다() {
        final Station station = Station.of(1L, "잠실역");

        when(stationRepository.insert(any())).thenReturn(station);

        final Station actual = stationService.saveStation("잠실역");

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isEqualTo(station.getId());
            softAssertions.assertThat(actual.getName()).isEqualTo(station.getName());
        });
    }

    @Test
    void 모든_역을_검색하다() {
        final Station station = Station.of(1L, "잠실역");
        when(stationRepository.findAll()).thenReturn(List.of(station));

        final List<Station> actual = stationService.findAll();

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(1);
            softAssertions.assertThat(actual.get(0).getId()).isEqualTo(station.getId());
            softAssertions.assertThat(actual.get(0).getName()).isEqualTo(station.getName());
        });
    }

    @Test
    void 아이디를_통해_역을_검색하다() {
        final Station station = Station.of(1L, "잠실역");
        when(stationRepository.findById(any())).thenReturn(station);

        final Station actual = stationService.findStationById(1L);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isEqualTo(station.getId());
            softAssertions.assertThat(actual.getName()).isEqualTo(station.getName());
        });
    }

    @Test
    void 아이디를_통해_역을_삭제하다() {
        doNothing().when(stationRepository).deleteById(any());

        assertDoesNotThrow(() -> stationService.deleteStationById(1L));
    }
}
