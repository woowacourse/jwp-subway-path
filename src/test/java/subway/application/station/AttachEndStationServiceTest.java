package subway.application.station;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.station.application.AttachEndStationService;
import subway.line.domain.Line;
import subway.line.domain.LineColor;
import subway.line.domain.LineName;
import subway.line.application.port.output.LineRepository;
import subway.section.domain.Section;
import subway.section.application.port.output.SectionRepository;
import subway.section.domain.Sections;
import subway.station.domain.Station;
import subway.station.domain.StationDistance;
import subway.station.application.port.output.StationRepository;
import subway.ui.dto.request.AttachStationRequest;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class AttachEndStationServiceTest {

    @Mock
    private LineRepository lineRepository;

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private StationRepository stationRepository;

    private AttachEndStationService attachEndStationService;

    @BeforeEach
    void setUp() {
        attachEndStationService = new AttachEndStationService(
                lineRepository,
                sectionRepository,
                stationRepository
        );
    }

    @Test
    void 노선_맨뒤에_역_추가_테스트() {
        //given
        final List<Section> sectionList = new ArrayList<>();
        sectionList.add(new Section(new Station("역삼"), new Station("선릉"), new StationDistance(2)));
        sectionList.add(new Section(new Station("강남"), new Station("역삼"), new StationDistance(3)));
        final Sections sections = new Sections(sectionList);
        final Line line = new Line(sections, new LineName("2호선"), new LineColor("청록색"));
        given(lineRepository.findById(1L)).willReturn(line);
        given(sectionRepository.save(any(), anyLong())).willReturn(1L);
        given(stationRepository.save(any())).willReturn(1L);

        final AttachStationRequest request = new AttachStationRequest("선릉", "삼성", 5);

        //when
        attachEndStationService.attachEndStation(1L, request);

        final Sections updateSections = line.getSections();
        assertThat(updateSections.getSections()).hasSize(3);
        assertThat(updateSections.peekBySecondStationUnique(new Station("삼성"))).isNotNull();
    }
}
