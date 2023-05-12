package subway.application.station;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.station.application.RemoveBetweenStationService;
import subway.line.domain.Line;
import subway.line.domain.LineColor;
import subway.line.domain.LineName;
import subway.line.application.port.output.LineRepository;
import subway.section.domain.Section;
import subway.section.application.port.output.SectionRepository;
import subway.section.domain.Sections;
import subway.station.domain.Station;
import subway.station.domain.StationDistance;
import subway.ui.dto.request.RemoveStationRequest;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class RemoveBetweenStationServiceTest {

    @Mock
    private LineRepository lineRepository;

    @Mock
    private SectionRepository sectionRepository;

    private RemoveBetweenStationService removeBetweenStationService;

    @BeforeEach
    void setUp() {
        removeBetweenStationService = new RemoveBetweenStationService(
                lineRepository,
                sectionRepository
        );
    }

    @Test
    void 노선_전방_역_제거_테스트() {
        //given
        final List<Section> sectionList = new ArrayList<>();
        sectionList.add(new Section(new Station("강남"), new Station("역삼"), new StationDistance(3)));
        sectionList.add(new Section(new Station("역삼"), new Station("선릉"), new StationDistance(2)));
        final Sections sections = new Sections(sectionList);
        final Line line = new Line(sections, new LineName("2호선"), new LineColor("청록색"));
        given(lineRepository.findById(1L)).willReturn(line);
        doNothing().when(sectionRepository).delete(any());

        final RemoveStationRequest request = new RemoveStationRequest("역삼");

        //when
        removeBetweenStationService.removeBetweenStation(1L, request);

        //then
        final Sections updateSections = line.getSections();
        assertThat(updateSections.getSections()).hasSize(1);
        assertThatThrownBy(() -> updateSections.peekBySecondStationUnique(new Station("역삼")))
                .isInstanceOf(IllegalStateException.class);
        assertThat(updateSections.peekByFirstStationUnique(new Station("강남")).getDistance())
                .isEqualTo(new StationDistance(5));
    }
}
