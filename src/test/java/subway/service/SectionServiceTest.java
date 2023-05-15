package subway.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dto.LineStationRequest;
import subway.dto.LineStationResponse;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.SectionWithStationNameEntity;
import subway.entity.StationEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static subway.fixture.StationFixture.*;

@ExtendWith(MockitoExtension.class)
class SectionServiceTest {
    @Mock
    private LineDao lineDao;
    @Mock
    private StationDao stationDao;
    @Mock
    private SectionDao sectionDao;
    @InjectMocks
    private SectionService sectionService;

    @DisplayName("역을 추가한다")
    @Test
    void add() {
        //given
        Long lineId = 1L;
        LineStationRequest lineStationRequest = new LineStationRequest(1L, 2L, 10L);
        LineEntity lineEntity = new LineEntity(lineId, "경의중앙선", "청록");
        StationEntity stationEntity1 = new StationEntity("이촌");
        StationEntity stationEntity2 = new StationEntity("서빙고");
        when(lineDao.findById(lineId)).thenReturn(lineEntity);
        when(stationDao.findById(lineStationRequest.getPreStationId())).thenReturn(stationEntity1);
        when(stationDao.findById(lineStationRequest.getStationId())).thenReturn(stationEntity2);
        when(sectionDao.findAllByLineId(lineId)).thenReturn(new ArrayList<>());

        //when
        sectionService.addStation(lineId, lineStationRequest);

        //then
        verify(sectionDao).save(any(SectionEntity.class));
    }

    @DisplayName("역을 삭제한다")
    @Test
    void delete() {
        //given
        Long lineId = 1L;
        Long stationId = 1L;
        LineEntity lineEntity = new LineEntity(lineId, "경의중앙선", "청록");
        when(lineDao.findById(lineId)).thenReturn(lineEntity);
        when(sectionDao.findAllByLineId(lineId)).thenReturn(
                new ArrayList<>(List.of(new SectionWithStationNameEntity(lineId, "이촌", "서빙고", 10L))));
        when(stationDao.findById(stationId)).thenReturn(new StationEntity("이촌"));

        //when
        sectionService.removeStation(lineId, stationId);

        //then
        verify(sectionDao).remove(stationId);
    }

    @DisplayName("노선별 역을 조회한다")
    @Test
    void showLine() {
        //given
        Long lineId = 1L;
        LineEntity lineEntity = new LineEntity(lineId, "경의중앙선", "청록");
        when(lineDao.findById(lineId)).thenReturn(lineEntity);
        when(sectionDao.findAllByLineId(lineId)).thenReturn(
                new ArrayList<>(
                        List.of(new SectionWithStationNameEntity(lineId, "이촌", "서빙고", 10L),
                                new SectionWithStationNameEntity(lineId, "용산", "이촌", 10L),
                                new SectionWithStationNameEntity(lineId, "효창공원역", "용산", 10L))));

        //when
        LineStationResponse response = sectionService.findByLineId(lineId);

        //then
        assertThat(response.getStations()).isEqualTo(List.of(STATION_4, STATION_1, STATION_2, STATION_3));

    }
}
