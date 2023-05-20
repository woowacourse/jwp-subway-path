package subway.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Sections;
import subway.dto.LineStationRequest;
import subway.dto.LineStationResponse;
import subway.persistence.repository.SectionRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static subway.fixture.LineFixture.LINE_1;
import static subway.fixture.SectionFixture.*;
import static subway.fixture.StationFixture.*;

@ExtendWith(MockitoExtension.class)
class SectionServiceTest {
    @Mock
    private SectionRepository sectionRepository;
    @InjectMocks
    private SectionService sectionService;

    @DisplayName("역을 추가한다")
    @Test
    void add() {
        //given
        Long lineId = 1L;
        LineStationRequest lineStationRequest = new LineStationRequest(2L, 3L, 10L);
        when(sectionRepository.toSection(lineId, lineStationRequest)).thenReturn(new Section(LINE_1, STATION_2, STATION_3, new Distance(10)));
        when(sectionRepository.getCurrentLineSections(lineId)).thenReturn(new Sections(new ArrayList<>(List.of(SECTION_1))));

        //when
        sectionService.addStation(lineId, lineStationRequest);

        //then
        verify(sectionRepository).saveSection(lineId, lineStationRequest);
    }

    @DisplayName("역을 삭제한다")
    @Test
    void delete() {
        //given
        Long lineId = 1L;
        Long stationId = 1L;
        when(sectionRepository.getCurrentLineSections(lineId)).thenReturn(new Sections(new ArrayList<>(List.of(SECTION_1))));
        when(sectionRepository.findStationById(stationId)).thenReturn(STATION_1);

        //when
        sectionService.removeStation(lineId, stationId);

        //then
        verify(sectionRepository).removeSection(stationId);
    }

    @DisplayName("노선별 역을 조회한다")
    @Test
    void showLine() {
        //given
        Long lineId = 1L;
        when(sectionRepository.getCurrentLineSections(lineId)).thenReturn(new Sections(new ArrayList<>(List.of(SECTION_2, SECTION_3, SECTION_1))));

        //when
        LineStationResponse response = sectionService.findByLineId(lineId);

        //then
        assertThat(response.getStations()).isEqualTo(List.of(STATION_1, STATION_2, STATION_3, STATION_4));
    }
}
