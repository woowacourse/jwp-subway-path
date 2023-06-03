package subway.application;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dto.request.SectionRequest;
import subway.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.*;
import static subway.TestFeature.*;

@ExtendWith(MockitoExtension.class)
@Sql("classpath:initializeTestDb.sql")
class SectionServiceTest {

    @Mock
    private LineDao lineDao;

    @Mock
    private StationDao stationDao;

    @Mock
    private SectionDao sectionDao;

    @InjectMocks
    private SectionService sectionService;

    @DisplayName("빈 노선에 두 개의 역 추가")
    @Test
    void saveSectionEmptyLine() {
        // given
        Long lineId = 3L;
        SectionRequest sectionRequest = new SectionRequest(3L, 1L, 4);
        given(lineDao.isExistId(lineId)).willReturn(true);
        given(stationDao.isExistId(sectionRequest.getUpStationId())).willReturn(true);
        given(stationDao.isExistId(sectionRequest.getDownStationId())).willReturn(true);
        willDoNothing().given(sectionDao).insert(any());
        given(sectionDao.findByLineId(lineId)).willReturn(Optional.empty());

        // then
        assertDoesNotThrow(() -> sectionService.saveSection(lineId, sectionRequest));
    }

    @DisplayName("비지 않은 노선에 역 추가")
    @Test
    void saveSectionInDownStationNode() {
        // given
        Long lineId = 1L;
        SectionRequest sectionRequest = new SectionRequest(3L, 7L, 5);
        given(lineDao.isExistId(lineId)).willReturn(true);
        given(stationDao.isExistId(sectionRequest.getUpStationId())).willReturn(true);
        given(stationDao.isExistId(sectionRequest.getDownStationId())).willReturn(true);
        given(stationDao.findById(3L)).willReturn(Optional.of(STATION_ENTITY_낙성대역));
        given(stationDao.findById(7L)).willReturn(Optional.of(STATION_ENTITY_인천역));
        given(sectionDao.findByLineId(lineId)).willReturn(Optional.of(List.of(SECTION_ENTITY_인천_방배)));
        given(sectionDao.findSectionsByLineId(lineId)).willReturn(List.of(SECTION_STATION_MAPPER_인천_방배));
        willDoNothing().given(sectionDao).insertAll(any());
        willDoNothing().given(sectionDao).deleteAll(any());

        // when
        assertDoesNotThrow(() -> sectionService.saveSection(lineId, sectionRequest));
        verify(sectionDao, times(1)).insertAll(any());
        verify(sectionDao, times(1)).deleteAll(any());
    }

    @DisplayName("존재하지 않는 노선에 역을 추가하는 경우 예외를 반환")
    @Test
    void saveSectionExceptionLine() {
        // given
        Long lineId = 1000L;
        SectionRequest sectionRequest = new SectionRequest(2L, 3L, 3);
        given(lineDao.isExistId(lineId)).willReturn(false);

        // then
        assertThatThrownBy(() -> sectionService.saveSection(lineId, sectionRequest))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("존재하지 않는 역을 추가하는 경우 예외를 반환")
    @Test
    void saveSectionExceptionStation() {
        // given
        Long lineId = 1L;
        SectionRequest sectionRequest = new SectionRequest(2L, 3L, 3);
        given(lineDao.isExistId(lineId)).willReturn(true);
        given(stationDao.isExistId(sectionRequest.getUpStationId())).willReturn(false);

        // then
        assertThatThrownBy(() -> sectionService.saveSection(lineId, sectionRequest))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("역을 삭제할 수 있다")
    @Test
    void removeStationFromLine() {
        // given
        Long lineId = 1L;
        Long stationIdToRemove = 1L;
        given(lineDao.isExistId(lineId)).willReturn(true);
        given(stationDao.isExistId(stationIdToRemove)).willReturn(true);
        given(stationDao.findById(stationIdToRemove)).willReturn(Optional.of(STATION_ENTITY_서울대입구));
        given(sectionDao.findSectionsByLineId(lineId)).willReturn(List.of(
                SECTION_STATION_MAPPER_서울대입구_사당
        ));

        // then
        Assertions.assertDoesNotThrow(() -> sectionService.removeStationFromLine(lineId, stationIdToRemove));
        verify(sectionDao, times(1)).deleteAll(any());
        verify(sectionDao, times(1)).insertAll(any());
    }
}
