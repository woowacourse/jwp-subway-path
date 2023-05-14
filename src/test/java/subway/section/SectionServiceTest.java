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
import subway.station.dto.StationResponseDto;

@ExtendWith(MockitoExtension.class)
class SectionServiceTest {

    @Mock
    private SectionDao sectionDao;

    @InjectMocks
    private SectionService sectionService;

    @DisplayName("한 라인의 모든 역을 상행에서 하행으로 정렬하여 반환한다.")
    @Test
    void findSortedStationEntityByLineId() {
        //given
        final SectionStationDto 잠실새내_잠실_구간 = new SectionStationDto(1L, 1L, "잠실새내", 2L, "잠실", 4);
        final SectionStationDto 잠실_잠실나루_구간 = new SectionStationDto(1L, 2L, "잠실", 3L, "잠실나루", 5);
        when(sectionDao.findAllByLineId(any())).thenReturn(List.of(잠실_잠실나루_구간, 잠실새내_잠실_구간));

        //when
        final List<StationResponseDto> sortedStationResponses = sectionService.findSortedStations(1L);

        //then
        assertThat(sortedStationResponses).hasSize(3);

        assertThat(sortedStationResponses.get(0))
            .usingRecursiveComparison()
            .isEqualTo(new StationResponseDto(1L, "잠실새내"));
        assertThat(sortedStationResponses.get(1))
            .usingRecursiveComparison()
            .isEqualTo(new StationResponseDto(2L, "잠실"));
        assertThat(sortedStationResponses.get(2))
            .usingRecursiveComparison()
            .isEqualTo(new StationResponseDto(3L, "잠실나루"));
    }
}
