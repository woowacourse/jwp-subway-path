package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.line.LineRepository;
import subway.domain.section.SectionRepository;
import subway.dto.LineFindResponse;
import subway.dto.LineRequest;

import java.util.List;

import static fixtures.LineFixtures.*;
import static fixtures.SectionFixtures.*;
import static fixtures.StationFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @InjectMocks
    LineService lineService;

    @Mock
    LineRepository lineRepository;
    @Mock
    SectionRepository sectionRepository;

    @Test
    @DisplayName("노선을 저장한다.")
    void saveLineTest() {
        // given
        LineRequest request = REQUEST_LINE7;
        when(lineRepository.insert(LINE7_TO_INSERT)).thenReturn(LINE7);

        // when
        Long savedLineId = lineService.saveLine(request);

        // then
        assertThat(savedLineId).isEqualTo(LINE7_ID);
    }

    @Test
    @DisplayName("노선 id에 맞는 노선의 역 이름을 상행역에서 하행역 순서대로 반환한다.")
    void findStationNamesByLineIdTest() {
        // given
        Long lineId = LINE2_ID;
        when(lineRepository.findLineById(lineId))
                .thenReturn(LINE2);
        when(sectionRepository.findSectionsByLineId(lineId))
                .thenReturn(List.of(
                        SECTION_강변역_TO_건대역,
                        SECTION_대림역_TO_잠실역,
                        SECTION_잠실역_TO_강변역));
        LineFindResponse expectResponse = new LineFindResponse(
                LINE2_NAME,
                List.of(STATION_대림역_NAME, STATION_잠실역_NAME, STATION_강변역_NAME, STATION_건대역_NAME));

        // when
        LineFindResponse response = lineService.findStationNamesByLineId(lineId);

        // then
        assertThat(response).isEqualTo(expectResponse);
    }

    @Test
    @DisplayName("모든 노선에 대해 각 노선의 역 이름을 상행역에서 하행역 순서대로 반환한다.")
    void findAllLineStationNamesTest() {
        // given
        when(lineRepository.findAllLines())
                .thenReturn(List.of(LINE2, LINE7));
        when(sectionRepository.findSectionsByLineId(LINE2_ID))
                .thenReturn(List.of(
                        SECTION_강변역_TO_건대역,
                        SECTION_대림역_TO_잠실역,
                        SECTION_잠실역_TO_강변역));
        when(sectionRepository.findSectionsByLineId(LINE7_ID))
                .thenReturn(List.of(SECTION_온수역_TO_철산역));

        LineFindResponse response2 = new LineFindResponse(
                LINE2_NAME,
                List.of(STATION_대림역_NAME, STATION_잠실역_NAME, STATION_강변역_NAME, STATION_건대역_NAME));
        LineFindResponse response7 = new LineFindResponse(
                LINE7_NAME,
                List.of(STATION_온수역_NAME, STATION_철산역_NAME));
        List<LineFindResponse> expectResponse = List.of(response2, response7);

        // when
        List<LineFindResponse> response = lineService.findAllLineStationNames();

        // then
        assertThat(response).isEqualTo(expectResponse);
    }

    @Test
    @DisplayName("Line 정보를 수정한다.")
    void updateLineTest() {
        // given
        Long lineId = LINE2_ID;
        LineRequest updateRequest = REQUEST_NEW_LINE2;
        doNothing().when(lineRepository).update(NEW_LINE2);

        // when, then
        assertThatNoException().isThrownBy(() -> lineService.updateLine(lineId, updateRequest));
    }

    @Test
    @DisplayName("Line을 삭제한다.")
    void deleteLineByIdTest() {
        // given
        Long lineId = LINE2_ID;
        doNothing().when(lineRepository).deleteById(LINE2_ID);

        // when, then
        assertThatNoException().isThrownBy(() -> lineService.deleteLineById(lineId));
    }
}