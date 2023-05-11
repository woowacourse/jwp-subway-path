package subway.application.station;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.application.station.service.AttachBetweenStationService;
import subway.domain.line.Line;
import subway.domain.line.LineColor;
import subway.domain.line.LineName;
import subway.domain.line.LineRepository;
import subway.domain.section.Section;
import subway.domain.section.SectionRepository;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.station.StationDistance;
import subway.domain.station.StationRepository;
import subway.ui.dto.request.AttachStationRequest;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class AttachBetweenStationServiceTest {

    @Mock
    private LineRepository lineRepository;

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private StationRepository stationRepository;

    private AttachBetweenStationService attachBetweenStationService;

    @BeforeEach
    void setUp() {
        attachBetweenStationService = new AttachBetweenStationService(
                lineRepository,
                sectionRepository,
                stationRepository
        );
    }

    @Test
    void 노선내_역_사이에_역_추가_테스트() {
        //given
        final List<Section> sectionList = new ArrayList<>();
        sectionList.add(new Section(new Station("강남"), new Station("역삼"), new StationDistance(3)));
        sectionList.add(new Section(new Station("역삼"), new Station("선릉"), new StationDistance(2)));
        final Sections sections = new Sections(sectionList);
        final Line line = new Line(sections, new LineName("2호선"), new LineColor("청록색"));
        given(lineRepository.findById(1L)).willReturn(line);
        given(sectionRepository.save(any(), anyLong())).willReturn(1L);
        doNothing().when(sectionRepository).delete(any());
        given(stationRepository.save(any())).willReturn(1L);

        final AttachStationRequest request = new AttachStationRequest("역삼", "서초", 1);

        //when
        attachBetweenStationService.attachBetweenStation(1L, request);

        final Sections updateSections = line.getSections();
        assertThat(updateSections.getSections()).hasSize(3);
        assertThat(updateSections.peekByFirstStationUnique(new Station("역삼"))).isNotNull();
        assertThat(updateSections.peekByFirstStationUnique(new Station("서초"))).isNotNull();
    }
}
