package subway.service.subway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.subway.Section;
import subway.domain.subway.Sections;
import subway.domain.subway.Station;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;
import subway.service.SubwayMapService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SubwayMapServiceMockTest {

    @InjectMocks
    private SubwayMapService subwayMapService;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private SectionRepository sectionRepository;

    @Test
    @DisplayName("지하철역의 역 정보를 순서대로 보여준다.")
    void show_ordered_station_map_success() {
        // given
        long lineNumber = 2;
        Sections sections = new Sections(
                List.of(
                        new Section(
                                new Station("잠실역"),
                                new Station("잠실새내역"),
                                3)
                )
        );
        given(sectionRepository.findSectionsByLineNumber(lineNumber)).willReturn(sections);
        given(stationRepository.findStationIdByStationName(any())).willReturn(anyLong());

        // when
        subwayMapService.showLineMap(2);

        // then
        assertThat(sections.getSections().size()).isEqualTo(1);
    }
}
