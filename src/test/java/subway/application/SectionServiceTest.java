package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.*;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.SectionRequest;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SectionServiceTest {
    @InjectMocks
    private SectionService sectionService;
    @Mock
    private SectionDao sectionDao;
    @Mock
    private StationDao stationDao;
    @Mock
    private LineDao lineDao;

    @Test
    @DisplayName("Section을 저장한다.")
    void saveSection() {
        SectionRequest sectionRequest = new SectionRequest("인천역", "서울역", 5);
        Station 인천역 = new Station(1L, "인천역");
        Station 서울역 = new Station(2L, "서울역");
        List<Section> sections = List.of(new Section(1L, 인천역, 서울역, new Distance(5)));
        List<Station> stations = List.of(new Station(1L, "인천역"), new Station(2L, "서울역"));

        when(sectionDao.findByLineId(any())).thenReturn(sections);
        when(stationDao.findAll()).thenReturn(stations);
        when(stationDao.findByName("인천역")).thenReturn(인천역);
        when(stationDao.findByName("서울역")).thenReturn(서울역);
        doNothing().when(sectionDao).deleteAllById(any());
        doNothing().when(sectionDao).insertAll(any(),any());
        when(lineDao.findById(any())).thenReturn(Optional.of(new Line(1L,"1호선")));
        sectionService.saveSection(1L, sectionRequest);

        verify(sectionDao, times(1)).deleteAllById(any());
        verify(sectionDao, times(1)).insertAll(any(),anyList());
    }

    @Test
    @DisplayName("Station을 삭제한다.")
    void deleteSection() {
        Station 인천역 = new Station(1L, "인천역");
        Station 서울역 = new Station(2L, "서울역");
        List<Section> sections = List.of(new Section(1L, 인천역, 서울역, new Distance(5)));

        when(sectionDao.findByLineId(any())).thenReturn(sections);
        when(stationDao.findById(any())).thenReturn(Optional.of(new Station("서울역")));
        doNothing().when(sectionDao).deleteAllById(any());
        doNothing().when(sectionDao).insertAll(any(),any());
        when(lineDao.findById(any())).thenReturn(Optional.of(new Line(1L,"1호선")));

        sectionService.deleteSection(1L, 1L);

        verify(sectionDao, times(1)).deleteAllById(any());
        verify(sectionDao, times(1)).insertAll(any(),anyList());
    }
}
