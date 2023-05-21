package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.controller.dto.LineRequest;
import subway.controller.dto.SectionCreateRequest;
import subway.controller.dto.SectionDeleteRequest;
import subway.domain.line.Distance;
import subway.domain.line.Line;
import subway.domain.line.Section;
import subway.domain.line.Sections;
import subway.domain.line.Station;
import subway.exception.InvalidDistanceException;
import subway.exception.LineNameException;
import subway.exception.LineStationAdditionException;
import subway.exception.StationNotFoundException;
import subway.repository.LineRepository;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationService stationService;

    @InjectMocks
    private LineService lineService;

    @Test
    @DisplayName("새로운 노선을 저장한다.")
    void saveLine() {
        LineRequest lineRequest = new LineRequest("1호선");
        given(lineRepository.existsByName(any()))
                .willReturn(false);
        given(lineRepository.save(new Line(null, lineRequest.getName(), null)))
                .willReturn(new Line(1L, "1호선", new Sections(new LinkedList<>())));

        Line line = lineService.saveLine(lineRequest);

        assertAll(
                () -> assertThat(line.getId()).isNotNull(),
                () -> assertThat(line.getName()).isEqualTo(lineRequest.getName())
        );
    }

    @Test
    @DisplayName("새로운 노선을 저장할 때 노선 이름이 중복된 경우 예외가 발생한다.")
    void saveLineFail() {
        LineRequest lineRequest = new LineRequest("1호선");
        given(lineRepository.existsByName(any()))
                .willReturn(true);

        assertThatThrownBy(() -> lineService.saveLine(lineRequest))
                .isInstanceOf(LineNameException.class)
                .hasMessageContaining("동일한 이름을 가진 노선이 존재합니다.");
    }

    @Test
    @DisplayName("ID에 해당하는 노선 정보를 가져온다.")
    void findLineResponseById() {
        Line line = new Line(1L, "1호선", new Sections(new LinkedList<>()));
        given(lineRepository.findById(any()))
                .willReturn(line);

        Line findLine = lineService.findLineResponseById(1L);

        assertAll(
                () -> assertThat(findLine.getId()).isEqualTo(findLine.getId()),
                () -> assertThat(findLine.getName()).isEqualTo(findLine.getName())
        );
    }

    @Test
    @DisplayName("모든 노선 정보를 가져온다.")
    void findLineResponses() {
        Line line1 = new Line(1L, "1호선", new Sections(new LinkedList<>()));
        Line line2 = new Line(2L, "2호선", new Sections(new LinkedList<>()));
        given(lineRepository.findAll())
                .willReturn(List.of(line1, line2));

        List<Line> lines = lineService.findLineResponses();

        assertThat(lines).hasSize(2);
    }

    @Test
    @DisplayName("ID에 해당하는 노선을 삭제한다.")
    void deleteLineById() {
        willDoNothing().given(lineRepository).deleteById(any());

        assertDoesNotThrow(() -> lineService.deleteLineById(1L));
    }

    @Test
    @DisplayName("노선에 역을 최초로 생성한다.")
    void createSection() {
        given(lineRepository.findById(any())).willReturn(new Line(1L, "1호선", new Sections(new LinkedList<>())));
        given(stationService.findByName("잠실역")).willReturn(new Station(1L, "잠실역"));
        given(stationService.findByName("강남역")).willReturn(new Station(2L, "강남역"));

        assertDoesNotThrow(() -> lineService.createSection(1L, new SectionCreateRequest("잠실역", "강남역", 10)));
    }

    @Test
    @DisplayName("추가하려고 하는 역이 모두 노선에 없으면 예외가 발생한다.")
    void createSectionFail() {
        Station station1 = new Station(1L, "부산역");
        Station station2 = new Station(2L, "서면역");
        Sections sections = new Sections(
                new LinkedList<>(List.of(new Section(null, station1, station2, new Distance(10)))));

        given(lineRepository.findById(any())).willReturn(new Line(1L, "1호선", sections));
        given(stationService.findByName("잠실역")).willReturn(new Station(3L, "잠실역"));
        given(stationService.findByName("강남역")).willReturn(new Station(4L, "강남역"));

        assertThatThrownBy(() -> lineService.createSection(1L, new SectionCreateRequest("잠실역", "강남역", 5)))
                .isInstanceOf(LineStationAdditionException.class)
                .hasMessageContaining("해당 역을 추가할 수 없습니다.");
    }

    @Test
    @DisplayName("추가하려고 하는 역이 이미 노선에 존재하면 예외가 발생한다.")
    void createSectionFail2() {
        Station station1 = new Station(1L, "부산역");
        Station station2 = new Station(2L, "서면역");
        Sections sections = new Sections(
                new LinkedList<>(List.of(new Section(null, station1, station2, new Distance(10)))));

        given(lineRepository.findById(any())).willReturn(new Line(1L, "1호선", sections));
        given(stationService.findByName("부산역")).willReturn(new Station(1L, "부산역"));
        given(stationService.findByName("서면역")).willReturn(new Station(2L, "서면역"));

        assertThatThrownBy(() -> lineService.createSection(1L, new SectionCreateRequest("부산역", "서면역", 5)))
                .isInstanceOf(LineStationAdditionException.class)
                .hasMessageContaining("해당 역을 추가할 수 없습니다.");
    }

    @Test
    @DisplayName("노선의 역과 역 사이에 역을 추가한다.")
    void createSectionBetweenStations() {
        Station station1 = new Station(1L, "부산역");
        Station station2 = new Station(2L, "서면역");
        Station station3 = new Station(3L, "동래역");
        Sections sections = new Sections(
                new LinkedList<>(List.of(new Section(null, station1, station2, new Distance(10)),
                        new Section(null, station2, station3, new Distance(5)))));

        given(lineRepository.findById(any())).willReturn(new Line(1L, "1호선", sections));
        given(stationService.findByName("서면역")).willReturn(new Station(2L, "서면역"));
        given(stationService.findByName("해운대역")).willReturn(new Station(4L, "해운대역"));

        assertDoesNotThrow(() -> lineService.createSection(1L, new SectionCreateRequest("서면역", "해운대역", 3)));
    }

    @Test
    @DisplayName("노선의 역과 역 사이에 역을 추가할 때, 기존 거리보다 크거나 같은 경우 예외가 발생한다..")
    void createSectionBetweenStationsWithWrongDistance() {
        Station station1 = new Station(1L, "부산역");
        Station station2 = new Station(2L, "서면역");
        Station station3 = new Station(3L, "동래역");
        Sections sections = new Sections(
                new LinkedList<>(List.of(new Section(null, station1, station2, new Distance(10)),
                        new Section(null, station2, station3, new Distance(5)))));

        given(lineRepository.findById(any())).willReturn(new Line(1L, "1호선", sections));
        given(stationService.findByName("서면역")).willReturn(new Station(2L, "서면역"));
        given(stationService.findByName("해운대역")).willReturn(new Station(4L, "해운대역"));

        assertThatThrownBy(() -> lineService.createSection(1L, new SectionCreateRequest("서면역", "해운대역", 5)))
                .isInstanceOf(InvalidDistanceException.class)
                .hasMessageContaining("기존 거리보다 멀 수 없습니다.");
    }

    @Test
    @DisplayName("노선의 상행 종점 전에 역을 추가한다.")
    void createSectionEndOfUpLines() {
        Station station1 = new Station(1L, "부산역");
        Station station2 = new Station(2L, "서면역");
        Sections sections = new Sections(
                new LinkedList<>(List.of(new Section(null, station1, station2, new Distance(10)))));

        given(lineRepository.findById(any())).willReturn(new Line(1L, "1호선", sections));
        given(stationService.findByName("부산역")).willReturn(new Station(1L, "부산역"));
        given(stationService.findByName("해운대역")).willReturn(new Station(3L, "해운대역"));

        assertDoesNotThrow(() -> lineService.createSection(1L, new SectionCreateRequest("부산역", "해운대역", 5)));
    }

    @Test
    @DisplayName("노선의 하행 종점 후에 역을 추가한다.")
    void createSectionEndOfDownLine() {
        Station station1 = new Station(1L, "부산역");
        Station station2 = new Station(2L, "서면역");
        Sections sections = new Sections(
                new LinkedList<>(List.of(new Section(null, station1, station2, new Distance(10)))));

        given(lineRepository.findById(any())).willReturn(new Line(1L, "1호선", sections));
        given(stationService.findByName("서면역")).willReturn(new Station(2L, "서면역"));
        given(stationService.findByName("해운대역")).willReturn(new Station(3L, "해운대역"));

        assertDoesNotThrow(() -> lineService.createSection(1L, new SectionCreateRequest("서면역", "해운대역", 5)));
    }

    @Test
    @DisplayName("노선에 역을 삭제한다.")
    void deleteSection() {
        Station station1 = new Station(1L, "부산역");
        Station station2 = new Station(2L, "서면역");
        Sections sections = new Sections(
                new LinkedList<>(List.of(new Section(null, station1, station2, new Distance(10)))));

        given(lineRepository.findById(any())).willReturn(new Line(1L, "1호선", sections));
        given(stationService.findByName("서면역")).willReturn(new Station(2L, "서면역"));

        assertDoesNotThrow(() -> lineService.deleteSection(1L, new SectionDeleteRequest("서면역")));
    }

    @Test
    @DisplayName("노선에 역을 삭제할 때, 존재하지 않는 역인 경우 예외가 발생한다.")
    void deleteSectionFail() {
        Station station1 = new Station(1L, "부산역");
        Station station2 = new Station(2L, "서면역");
        Sections sections = new Sections(
                new LinkedList<>(List.of(new Section(null, station1, station2, new Distance(10)))));

        given(lineRepository.findById(any())).willReturn(new Line(1L, "1호선", sections));
        given(stationService.findByName("강남역")).willReturn(new Station(3L, "서면역"));

        assertThatThrownBy(() -> lineService.deleteSection(1L, new SectionDeleteRequest("강남역")))
                .isInstanceOf(StationNotFoundException.class)
                .hasMessageContaining("노선에 해당 역이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("노선의 역과 역 사이에 있는 역을 삭제한다.")
    void deleteSectionBetweenStations() {
        Station station1 = new Station(1L, "부산역");
        Station station2 = new Station(2L, "서면역");
        Station station3 = new Station(3L, "동래역");
        Sections sections = new Sections(
                new LinkedList<>(List.of(new Section(null, station1, station2, new Distance(10)),
                        new Section(null, station2, station3, new Distance(5)))));

        given(lineRepository.findById(any())).willReturn(new Line(1L, "1호선", sections));
        given(stationService.findByName("서면역")).willReturn(new Station(2L, "서면역"));

        assertDoesNotThrow(() -> lineService.deleteSection(1L, new SectionDeleteRequest("서면역")));
    }

}
