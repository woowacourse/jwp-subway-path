package subway.application.station.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import subway.domain.station.StationName;
import subway.domain.station.StationRepository;
import subway.ui.dto.request.AddStationRequest;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class AddStationServiceTest {

    @Mock
    private LineRepository lineRepository;

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private StationRepository stationRepository;

    private AddStationService addStationService;
    private Line lineFixture;

    @BeforeEach
    void setUp() {
        addStationService = new AddStationService(
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
    void 노선_상행에_역_추가() {
        //given
        final AddStationRequest request = new AddStationRequest(null, "강남", "교대", 3);
        given(lineRepository.findById(1L)).willReturn(lineFixture);
        given(sectionRepository.save(any(), any())).willReturn(1L);
        given(stationRepository.findByName(any())).willReturn(Optional.of(new Station("강남")));
        given(stationRepository.saveIfNotExist(any())).willReturn(new Station("교대"));

        //when
        addStationService.addStation(1L, request);

        //then
        final Station frontStation = lineFixture.getFrontStation();
        assertThat(frontStation.getStationName()).isEqualTo("교대");

        final Sections updateSections = lineFixture.getSections();
        assertThat(updateSections.getSections()).hasSize(3);
        assertThat(updateSections.peekByFirstStationUnique(frontStation).getDistance())
                .isEqualTo(new StationDistance(3));
    }

    @Test
    void 상행역으로_추가시_기준역이_상행역이_아닌경우_예외발생() {
        //given
        final AddStationRequest request = new AddStationRequest(null, "선릉", "교대", 3);
        given(stationRepository.findByName(any())).willReturn(Optional.of(new Station("선릉")));
        given(lineRepository.findById(1L)).willReturn(lineFixture);

        //when & then
        assertThatThrownBy(() -> addStationService.addStation(1L, request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("기준역이 상행 종점이 아닙니다.");
    }

    @Test
    void 노선_하행에_역_추가() {
        //given
        final AddStationRequest request = new AddStationRequest("선릉", null, "삼성", 1);
        given(lineRepository.findById(1L)).willReturn(lineFixture);
        given(sectionRepository.save(any(), any())).willReturn(1L);
        given(stationRepository.findByName(any())).willReturn(Optional.of(new Station("선릉")));
        given(stationRepository.saveIfNotExist(any())).willReturn(new Station("삼성"));

        //when
        addStationService.addStation(1L, request);

        //then
        final Station endStation = lineFixture.getEndStation();
        assertThat(endStation.getStationName()).isEqualTo("삼성");

        final Sections updateSections = lineFixture.getSections();
        assertThat(updateSections.getSections()).hasSize(3);
        assertThat(updateSections.peekBySecondStationUnique(endStation).getDistance())
                .isEqualTo(new StationDistance(1));
    }

    @Test
    void 하행역으로_추가시_기준역이_하행역이_아닌경우_예외발생() {
        //given
        final AddStationRequest request = new AddStationRequest("강남", null, "삼성", 3);
        given(lineRepository.findById(1L)).willReturn(lineFixture);
        given(stationRepository.findByName(any())).willReturn(Optional.of(new Station("강남")));

        //when & then
        assertThatThrownBy(() -> addStationService.addStation(1L, request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("기준역이 하행 종점이 아닙니다.");
    }

    @Test
    void 노선_사이에_역_추가() {
        //given
        final AddStationRequest request = new AddStationRequest("강남", "역삼", "삼성", 2);
        given(lineRepository.findById(1L)).willReturn(lineFixture);
        given(sectionRepository.save(any(), any())).willReturn(1L);
        given(stationRepository.saveIfNotExist(any())).willReturn(new Station(4L, new StationName("삼성")));

        //when
        addStationService.addStation(1L, request);

        //then
        final Sections updateSections = lineFixture.getSections();
        assertThat(updateSections.getSections()).hasSize(3);

        assertThat(updateSections.peekByFirstStationUnique(new Station("강남")).getDistance())
                .isEqualTo(new StationDistance(2));
        assertThat(updateSections.peekByFirstStationUnique(new Station("삼성")).getDistance())
                .isEqualTo(new StationDistance(1));
    }

    @Test
    void 연결되지_않은_구간에_역_추가시_예외발생() {
        //given
        final AddStationRequest request = new AddStationRequest("강남", "선릉", "삼성", 2);
        given(lineRepository.findById(1L)).willReturn(lineFixture);

        //when & then
        assertThatThrownBy(() -> addStationService.addStation(1L, request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("두 역은 구간으로 연결되어 있지 않습니다.");
    }
}
