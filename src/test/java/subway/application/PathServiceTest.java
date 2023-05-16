package subway.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.*;
import subway.dto.PathResponse;
import subway.dto.StationResponse;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    @InjectMocks
    private PathService pathService;
    @Mock
    private LineDao lineDao;
    @Mock
    private StationDao stationDao;
    @Mock
    private SectionDao sectionDao;

    @Test
    @DisplayName("경로를 탐색한다.")
    void findPath() {
        List<LineEntity> lines = List.of(new LineEntity("0호선"));
        List<StationEntity> stationEntities = List.of(new StationEntity(1L, "인천역"), new StationEntity(2L, "서울역"));
        List<SectionEntity> sectionEntities = List.of(new SectionEntity(1L, 1L, 2L, 5));
        List<StationResponse> stationResponses = List.of(new StationResponse(1L, "인천역"), new StationResponse(2L, "서울역"));
        when(lineDao.findAll()).thenReturn(lines);
        when(stationDao.findAll()).thenReturn(stationEntities);
        when(sectionDao.findByLineId(any())).thenReturn(sectionEntities);
        when(stationDao.findById(1L)).thenReturn(Optional.of(new StationEntity("인천역")));
        when(stationDao.findById(2L)).thenReturn(Optional.of(new StationEntity("서울역")));

        Assertions.assertThat(pathService.findPath(1L, 2L)).usingRecursiveComparison().isEqualTo(new PathResponse(stationResponses, 5, null));
    }
}
