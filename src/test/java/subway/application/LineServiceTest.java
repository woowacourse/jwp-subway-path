package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.*;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationResponse;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {
    @InjectMocks
    private LineService lineService;
    @Mock
    private LineDao lineDao;
    @Mock
    private StationDao stationDao;
    @Mock
    private SectionDao sectionDao;

    @Test
    @DisplayName("Line을 저장한다.")
    void saveLine() {
        List<LineEntity> lines = List.of(new LineEntity("0호선"));
        LineRequest lineRequest = new LineRequest("1호선", "인천역", "서울역", 5);
        LineResponse lineResponse = new LineResponse(1L, "1호선",
                List.of(new StationResponse(1L, "인천역"), new StationResponse(2L, "서울역")));
        List<StationEntity> stationEntities = List.of(new StationEntity(1L, "인천역"), new StationEntity(2L, "서울역"));
        when(lineDao.findAll()).thenReturn(lines);
        when(lineDao.insert(any())).thenReturn(1L);
        doNothing().when(sectionDao).insertAll(any());
        when(stationDao.findAll()).thenReturn(stationEntities);

        assertThat(lineService.saveLine(lineRequest)).usingRecursiveComparison().isEqualTo(lineResponse);
        verify(lineDao, times(1)).findAll();
        verify(stationDao, times(4)).findAll();
        verify(lineDao, times(1)).insert(any());
    }

    @Test
    @DisplayName("모든 Line을 조회한다.")
    void findAllLines() {
        List<LineEntity> lineEntities = List.of(new LineEntity(1L, "1호선"));
        List<SectionEntity> sectionEntities = List.of(new SectionEntity(1L, 1L, 2L, 5));
        List<StationEntity> stationEntities = List.of(new StationEntity(1L, "인천역"), new StationEntity(2L, "서울역"));
        List<LineResponse> lineResponses = List.of(new LineResponse(1L, "1호선",
                List.of(new StationResponse(1L, "인천역"), new StationResponse(2L, "서울역"))));

        when(lineDao.findAll()).thenReturn(lineEntities);
        when(sectionDao.findByLineId(any())).thenReturn(sectionEntities);
        when(stationDao.findAll()).thenReturn(stationEntities);

        assertThat(lineService.findAllLines()).usingRecursiveComparison().isEqualTo(lineResponses);
        verify(lineDao, times(1)).findAll();
        verify(sectionDao, times(1)).findByLineId(any());
        verify(stationDao, times(2)).findAll();
    }

    @Test
    @DisplayName("lineId로 Line을 조회한다.")
    void findLineResponseById() {
        LineEntity findEntity = new LineEntity(1L, "1호선");
        List<SectionEntity> sectionEntities = List.of(new SectionEntity(1L, 1L, 2L, 5));
        List<StationEntity> stationEntities = List.of(new StationEntity(1L, "인천역"), new StationEntity(2L, "서울역"));
        LineResponse lineResponse = new LineResponse(1L, "1호선",
                List.of(new StationResponse(1L, "인천역"), new StationResponse(2L, "서울역")));
        when(lineDao.findById(any())).thenReturn(Optional.of(findEntity));
        when(sectionDao.findByLineId(any())).thenReturn(sectionEntities);
        when(stationDao.findAll()).thenReturn(stationEntities);

        assertThat(lineService.findLineResponseById(1L)).usingRecursiveComparison().isEqualTo(lineResponse);
        verify(lineDao, times(1)).findById(any());
        verify(sectionDao, times(1)).findByLineId(any());
        verify(stationDao, times(2)).findAll();
    }
}
