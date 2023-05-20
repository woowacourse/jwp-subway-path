package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.LineDao;
import subway.domain.section.SectionRepository;
import subway.dto.LineFindResponse;

import java.util.List;

import static fixtures.LineFixtures.*;
import static fixtures.SectionFixtures.*;
import static fixtures.StationFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @InjectMocks
    LineService lineService;

    @Mock
    LineDao lineDao;
    @Mock
    SectionRepository sectionRepository;

    @Test
    @DisplayName("노선 id에 맞는 노선의 역 이름을 상행역에서 하행역 순서대로 반환한다.")
    void findStationNamesByLineIdTest() {
        // given
        Long lineId = LINE2_ID;
        when(lineDao.findById(lineId))
                .thenReturn(LINE2_FIND_ENTITY);
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
    @DisplayName("노선 id에 맞는 노선의 역 이름을 상행역에서 하행역 순서대로 반환한다.")
    void findAllLineStationNamesTest() {
        // given
        when(lineDao.findAll())
                .thenReturn(List.of(LINE2_FIND_ENTITY, LINE7_FIND_ENTITY));
        when(lineDao.findById(LINE2_ID))
                .thenReturn(LINE2_FIND_ENTITY);
        when(lineDao.findById(LINE7_ID))
                .thenReturn(LINE7_FIND_ENTITY);
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
}