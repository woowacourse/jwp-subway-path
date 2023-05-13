package subway.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.section.dto.SectionStationDto;
import subway.section.persistence.SectionDao;
import subway.station.persistence.StationEntity;

@ExtendWith(MockitoExtension.class)
class SectionServiceTest {

    @Mock
    private SectionDao sectionDao;

    @InjectMocks
    private SectionService sectionService;

    @DisplayName("한 라인의 모든 구간을 상행에서 하행으로 정렬하여 반환한다.")
    @Test
    void findSortedSectionsByLineId() {
        //given
        final SectionStationDto 잠실나루_잠실_구간 = new SectionStationDto(1L, 1L, "잠실나루", 2L, "잠실", 4);
        final SectionStationDto 잠실_잠실새내_구간 = new SectionStationDto(1L, 2L, "잠실", 3L, "잠실새내", 5);
        when(sectionDao.findAllByLineId(any())).thenReturn(List.of(잠실_잠실새내_구간, 잠실나루_잠실_구간));

        //when
        final List<SectionStationDto> sortedSectionStationDtoByLineId =
            sectionService.findSortedSectionStationDtoByLineId(1L);

        //then
        assertThat(sortedSectionStationDtoByLineId).hasSize(2);
        assertThat(sortedSectionStationDtoByLineId.get(0).getUpStationName()).isEqualTo("잠실나루");
        assertThat(sortedSectionStationDtoByLineId.get(0).getDownStationName()).isEqualTo("잠실");
        assertThat(sortedSectionStationDtoByLineId.get(0).getDistance()).isEqualTo(4);
        assertThat(sortedSectionStationDtoByLineId.get(1).getUpStationName()).isEqualTo("잠실");
        assertThat(sortedSectionStationDtoByLineId.get(1).getDownStationName()).isEqualTo("잠실새내");
        assertThat(sortedSectionStationDtoByLineId.get(1).getDistance()).isEqualTo(5);
    }

    @DisplayName("한 라인의 모든 역을 상행에서 하행으로 정렬하여 반환한다.")
    @Test
    void findSortedStationEntityByLineId() {
        //given
        final SectionStationDto 잠실나루_잠실_구간 = new SectionStationDto(1L, 1L, "잠실나루", 2L, "잠실", 4);
        final SectionStationDto 잠실_잠실새내_구간 = new SectionStationDto(1L, 2L, "잠실", 3L, "잠실새내", 5);
        when(sectionDao.findAllByLineId(any())).thenReturn(List.of(잠실_잠실새내_구간, 잠실나루_잠실_구간));

        //when
        final List<StationEntity> sortedStationEntityByLineId = sectionService.findSortedStationEntityByLineId(1L);

        //then
        assertThat(sortedStationEntityByLineId).hasSize(3);

        assertThat(sortedStationEntityByLineId.get(0))
            .usingRecursiveComparison()
            .isEqualTo(new StationEntity(1L, "잠실나루"));
        assertThat(sortedStationEntityByLineId.get(1))
            .usingRecursiveComparison()
            .isEqualTo(new StationEntity(2L, "잠실"));
        assertThat(sortedStationEntityByLineId.get(2))
            .usingRecursiveComparison()
            .isEqualTo(new StationEntity(3L, "잠실새내"));
    }
}
