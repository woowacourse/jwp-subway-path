package subway.application;

import static fixtures.GeneralSectionFixtures.*;
import static fixtures.LineFixtures.*;
import static fixtures.StationFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.line.Line;
import subway.domain.station.Station;
import subway.dto.LineFindResponse;
import subway.repository.GeneralSectionRepository;
import subway.repository.LineRepository;


@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @InjectMocks
    private LineService lineService;

    @Mock
    private LineRepository lineRepository;

    @Mock
    private GeneralSectionRepository generalSectionRepository;

    @Test
    @DisplayName("노선 ID로 해당 노선의 역 이름들을 순서대로 반환한다. (CASE : A-C-E)")
    void findOrderedStationNamesByLineId() {
        // given
        Line line2 = INITIAL_Line2.FIND_LINE;
        Long initLineId = INITIAL_Line2.ID;
        Station stationC = INITIAL_STATION_C.FIND_STATION;
        Station dummyStationB = STATION_E.createDummyStation(-1L, line2);

        when(lineRepository.findLineById(initLineId)).thenReturn(line2);
        when(generalSectionRepository.findAllSectionByLineId(initLineId))
                .thenReturn(List.of(
                        INITIAL_GENERAL_SECTION_A_TO_C.FIND_SECTION,
                        GENERAL_SECTION_C_TO_E.createDummy(stationC, dummyStationB, line2))
                );

        LineFindResponse expectedResponse = LINE_FIND_RESPONSE_LINE2_A_TO_C_AND_C_TO_E.RESPONSE;

        // when
        LineFindResponse generatedLineFindResponse = lineService.findOrderedStationNamesByLineId(initLineId);

        // then
        assertThat(generatedLineFindResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("모든 노선의 역 이름들을 노선별로 순서대로 반환한다. (CASE : Line 2 -> A-C-E / Line 7 -> B-D)")
    void findAllLineOrderedStationNames() {
        // given
        Line line2 = INITIAL_Line2.FIND_LINE;
        Line line7 = INITIAL_Line7.FIND_LINE;
        Long line2Id = INITIAL_Line2.ID;
        Long line7Id = INITIAL_Line7.ID;
        Station line2StationC = INITIAL_STATION_C.FIND_STATION;
        Station dummyLine2StationE = STATION_E.createDummyStation(-1L, line2);

        when(lineRepository.findAllLine()).thenReturn(List.of(line2, line7));
        when(generalSectionRepository.findAllSectionByLineId(line2Id))
                .thenReturn(List.of(
                        INITIAL_GENERAL_SECTION_A_TO_C.FIND_SECTION,
                        GENERAL_SECTION_C_TO_E.createDummy(line2StationC, dummyLine2StationE, line2))
                );

        Station dummyLine7StationB = STATION_B.createDummyStation(-1L, line7);
        Station dummyLine7StationD = STATION_D.createDummyStation(-1L, line7);
        when(generalSectionRepository.findAllSectionByLineId(line7Id))
                .thenReturn(List.of(
                        GENERAL_SECTION_B_TO_D.createDummy(dummyLine7StationB, dummyLine7StationD, line7))
                );

        List<LineFindResponse> expectedResponse = ALL_LINE_FIND_RESPONSE_LINE2_AND_LINE7.RESPONSE;

        // when
        List<LineFindResponse> generatedAllLineFindResponse = lineService.findAllLineOrderedStationNames();

        // then
        assertThat(generatedAllLineFindResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
    }
}
