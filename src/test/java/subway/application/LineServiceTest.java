package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.Fixture;
import subway.domain.LineRepository;
import subway.dto.SectionRequest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @InjectMocks
    private LineService lineService;
    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationService stationService;

    @Test
    @DisplayName("노선에 첫 역 삽입 기능")
    void saveInitialSection() {
        // given
        final SectionRequest sectionRequest = new SectionRequest(1L, 2L, 10);
        given(lineRepository.findLineById(1L)).willReturn(Fixture.line);
        given(stationService.findStationById(1L)).willReturn(Fixture.stationA);
        given(stationService.findStationById(2L)).willReturn(Fixture.stationB);
        given(lineRepository.saveSection(Fixture.line, Fixture.sectionAB)).willReturn(1L);
        doNothing().when(lineRepository).updateUpEndpoint(any());

        // when & then
        assertDoesNotThrow(() -> lineService.saveInitialSection(1L, sectionRequest));
        verify(lineRepository, times(1)).saveSection(any(), any());
        verify(lineRepository, times(1)).updateUpEndpoint(any());
    }
}
