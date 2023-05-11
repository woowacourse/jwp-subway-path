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
import subway.application.station.service.AttachFrontStationService;
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
class AttachFrontStationServiceTest {

    @Mock
    private LineRepository lineRepository;

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private StationRepository stationRepository;

    private AttachFrontStationService attachFrontStationService;

    @BeforeEach
    void setUp() {
        attachFrontStationService = new AttachFrontStationService(
                lineRepository,
                sectionRepository,
                stationRepository
        );
    }

    @Test
    void 노선_전반에_역_추가_테스트() {
        //given
        final List<Section> sectionList = new ArrayList<>();
        sectionList.add(new Section(new Station("역삼"), new Station("선릉"), new StationDistance(2)));
        sectionList.add(new Section(new Station("강남"), new Station("역삼"), new StationDistance(3)));
        final Sections sections = new Sections(sectionList);
        final Line line = new Line(sections, new LineName("2호선"), new LineColor("청록색"));
        given(lineRepository.findById(1L)).willReturn(line);
        given(sectionRepository.save(any(), anyLong())).willReturn(1L);
        given(stationRepository.save(any())).willReturn(1L);

        final AttachStationRequest request = new AttachStationRequest("강남", "서초", 5);

        //when
        attachFrontStationService.attachFrontStation(1L, request);

        final Sections updateSections = line.getSections();
        assertThat(updateSections.getSections()).hasSize(3);
        assertThat(updateSections.peekByFirstStationUnique(new Station("서초"))).isNotNull();
    }
}
