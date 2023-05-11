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
import subway.application.station.service.RemoveFrontStationService;
import subway.domain.line.Line;
import subway.domain.line.LineColor;
import subway.domain.line.LineName;
import subway.domain.line.LineRepository;
import subway.domain.section.Section;
import subway.domain.section.SectionRepository;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.station.StationDistance;
import subway.ui.dto.request.RemoveStationRequest;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class RemoveFrontStationServiceTest {

    @Mock
    private LineRepository lineRepository;

    @Mock
    private SectionRepository sectionRepository;

    private RemoveFrontStationService removeFrontStationService;

    @BeforeEach
    void setUp() {
        removeFrontStationService = new RemoveFrontStationService(
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

        final RemoveStationRequest request = new RemoveStationRequest("강남");

        //when
        removeFrontStationService.removeFrontStation(1L, request);

        //then
        final Sections updateSections = line.getSections();
        assertThat(updateSections.getSections()).hasSize(1);
        assertThatThrownBy(() -> updateSections.peekByFirstStationUnique(new Station("강남")))
                .isInstanceOf(IllegalStateException.class);
    }
}
