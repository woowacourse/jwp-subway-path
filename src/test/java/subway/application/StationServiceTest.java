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
import static subway.fixtures.domain.StationFixture.JAMSIL;

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
        // given
        when(stationRepository.insert(any())).thenReturn(JAMSIL);

        // when
        final Station actual = stationService.saveStation(JAMSIL.getName());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isEqualTo(JAMSIL.getId());
            softAssertions.assertThat(actual.getName()).isEqualTo(JAMSIL.getName());
        });
    }

    @Test
    void 모든_역을_검색하다() {
        // given
        when(stationRepository.findAll()).thenReturn(List.of(JAMSIL));

        // when
        final List<Station> actualStations = stationService.findAll();
        final Station actualStation = actualStations.get(0);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actualStations).hasSize(1);
            softAssertions.assertThat(actualStation.getId()).isEqualTo(JAMSIL.getId());
            softAssertions.assertThat(actualStation.getName()).isEqualTo(JAMSIL.getName());
        });
    }

    @Test
    void 아이디를_통해_역을_검색하다() {
        // given
        when(stationRepository.findById(any())).thenReturn(JAMSIL);

        // when
        final Station actual = stationService.findStationById(JAMSIL.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isEqualTo(JAMSIL.getId());
            softAssertions.assertThat(actual.getName()).isEqualTo(JAMSIL.getName());
        });
    }

    @Test
    void 아이디를_통해_역을_삭제하다() {
        // given
        doNothing().when(stationRepository).deleteById(any());

        // when, then
        assertDoesNotThrow(() -> stationService.deleteStationById(JAMSIL.getId()));
    }
}
