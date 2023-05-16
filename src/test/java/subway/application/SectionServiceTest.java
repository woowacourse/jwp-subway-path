package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.SectionDao;
import subway.dao.SectionEntity;
import subway.dao.StationDao;
import subway.dao.StationEntity;
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

    @Test
    @DisplayName("Section을 저장한다.")
    void saveSection() {
        SectionRequest sectionRequest = new SectionRequest("서울역", "강릉역", 5);
        List<SectionEntity> sectionEntities = List.of(new SectionEntity(1L, 1L, 2L, 5));
        List<StationEntity> stationEntities = List.of(new StationEntity(1L, "인천역"), new StationEntity(2L, "서울역"));

        when(sectionDao.findByLineId(any())).thenReturn(sectionEntities);
        when(stationDao.findAll()).thenReturn(stationEntities);
        doNothing().when(sectionDao).deleteAllById(any());
        doNothing().when(sectionDao).insertAll(any());

        sectionService.saveSection(1L, sectionRequest);

        verify(sectionDao, times(1)).deleteAllById(any());
        verify(sectionDao, times(1)).insertAll(anyList());
    }

    @Test
    @DisplayName("Station을 삭제한다.")
    void deleteSection() {
        List<SectionEntity> sectionEntities = List.of(new SectionEntity(1L, 1L, 2L, 5));
        List<StationEntity> stationEntities = List.of(new StationEntity(1L, "인천역"), new StationEntity(2L, "서울역"));
        when(sectionDao.findByLineId(any())).thenReturn(sectionEntities);
        when(stationDao.findAll()).thenReturn(stationEntities);
        when(stationDao.findById(any())).thenReturn(Optional.of(new StationEntity("서울역")));
        doNothing().when(sectionDao).deleteAllById(any());
        doNothing().when(sectionDao).insertAll(any());

        sectionService.deleteSection(1L, 1L);

        verify(sectionDao, times(1)).deleteAllById(any());
        verify(sectionDao, times(1)).insertAll(anyList());
    }
}
