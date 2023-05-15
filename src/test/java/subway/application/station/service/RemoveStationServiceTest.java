package subway.application.station.service;

import static org.assertj.core.api.Assertions.assertThat;
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

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class RemoveStationServiceTest {


    @Mock
    private LineRepository lineRepository;

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private StationRepository stationRepository;

    private RemoveStationService removeStationService;
    private Line lineFixture;

    @BeforeEach
    void setUp() {
        removeStationService = new RemoveStationService(
                lineRepository,
                sectionRepository,
                stationRepository
        );

        lineFixture = generateLineFixture();
    }

    private Line generateLineFixture() {
        final List<Section> sectionList = new ArrayList<>();
        sectionList.add(new Section(new Station("강남"), new Station("역삼"), new StationDistance(3)));
        sectionList.add(new Section(new Station("역삼"), new Station("선릉"), new StationDistance(2)));
        final Sections sections = new Sections(sectionList);

        return new Line(sections, new LineName("2호선"), new LineColor("청록색"));
    }

    @Test
    void 상행_역_삭제_테스트() {
        //given
        given(lineRepository.findById(1L)).willReturn(lineFixture);
        doNothing().when(sectionRepository).delete(any());

        final Station remvoalStation = new Station("강남");
        given(stationRepository.findById(any())).willReturn(remvoalStation);

        //when
        removeStationService.removeStation(1L, 1L);

        //given
        assertThat(lineFixture.getSections().getSections()).hasSize(1);
        final Station frontStation = lineFixture.getFrontStation();
        assertThat(frontStation.getStationName()).isNotEqualTo("강남");
    }

    @Test
    void 하행_역_삭제_테스트() {
        //given
        given(lineRepository.findById(1L)).willReturn(lineFixture);
        doNothing().when(sectionRepository).delete(any());

        final Station remvoalStation = new Station("선릉");
        given(stationRepository.findById(any())).willReturn(remvoalStation);

        //when
        removeStationService.removeStation(1L, 1L);

        //given
        assertThat(lineFixture.getSections().getSections()).hasSize(1);
        final Station endStation = lineFixture.getEndStation();
        assertThat(endStation.getStationName()).isNotEqualTo("선릉");
    }

    @Test
    void 중간_역_삭제_테스트() {
        //given
        given(lineRepository.findById(1L)).willReturn(lineFixture);
        doNothing().when(sectionRepository).delete(any());

        final Station remvoalStation = new Station("역삼");
        given(stationRepository.findById(any())).willReturn(remvoalStation);

        //when
        removeStationService.removeStation(1L, 1L);

        //given
        final Sections sections = lineFixture.getSections();
        assertThat(sections.getSections()).hasSize(1);
    }
}
