package subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.controller.dto.LineCreateRequest;
import subway.controller.dto.LineResponse;
import subway.controller.dto.LinesResponse;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.exception.InvalidLineNameException;
import subway.repository.LineRepository;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @InjectMocks
    private LineService lineService;

    @Mock
    private LineRepository lineRepository;

    @Nested
    @DisplayName("노선 추가시 ")
    class CreateLine {

        @Test
        @DisplayName("유효한 정보라면 노선을 추가한다.")
        void createLine() {
            //given
            final Line line = new Line(1L, "2호선", "초록색");
            final LineCreateRequest request = new LineCreateRequest("2호선", "초록색");
            given(lineRepository.save(any(Line.class))).willReturn(line);

            //when
            final Long lineId = lineService.createLine(request);

            //then
            assertThat(lineId).isEqualTo(1L);
        }

        @Test
        @DisplayName("유효하지 않은 정보라면 예외를 던진다.")
        void createLineWithInvalidName() {
            //given
            final LineCreateRequest request = new LineCreateRequest("경의중앙선", "초록색");

            //when
            //then
            assertThatThrownBy(() -> lineService.createLine(request))
                    .isInstanceOf(InvalidLineNameException.class);
        }
    }

    @Nested
    @DisplayName("노선 조회시 ")
    class FindLineById {

        @Test
        @DisplayName("존재하는 노선이라면 노선 정보를 조회한다.")
        void findLineById() {
            //given
            final List<Section> sections = List.of(
                    new Section(new Station(1L, "잠실역"), new Station(2L, "잠실새내역"), 10),
                    new Section(new Station(2L, "잠실새내역"), Station.TERMINAL, 0)
            );
            final Line line = new Line(1L, "2호선", "초록색", sections);

            given(lineRepository.findById(1L)).willReturn(line);

            //when
            final LineResponse response = lineService.findLineById(1L);

            //then
            assertAll(
                    () -> assertThat(response.getId()).isEqualTo(1L),
                    () -> assertThat(response.getName()).isEqualTo("2호선"),
                    () -> assertThat(response.getColor()).isEqualTo("초록색"),
                    () -> assertThat(response.getStations()).hasSize(2),
                    () -> assertThat(response.getStations().get(0).getId()).isEqualTo(1L),
                    () -> assertThat(response.getStations().get(0).getName()).isEqualTo("잠실역"),
                    () -> assertThat(response.getStations().get(1).getId()).isEqualTo(2L),
                    () -> assertThat(response.getStations().get(1).getName()).isEqualTo("잠실새내역")
            );
        }
    }

    @Test
    @DisplayName("노선 목록을 조회한다.")
    void findLines() {
        //given
        final List<Section> sectionsOfLineTwo = List.of(
                new Section(new Station(1L, "잠실역"), new Station(2L, "잠실새내역"), 10),
                new Section(new Station(2L, "잠실새내역"), Station.TERMINAL, 0)
        );
        final List<Section> sectionsOfLineFour = List.of(
                new Section(new Station(3L, "이수역"), new Station(4L, "서울역"), 11),
                new Section(new Station(4L, "서울역"), Station.TERMINAL, 0)
        );
        final List<Line> lines = List.of(
                new Line(1L, "2호선", "초록색", sectionsOfLineTwo),
                new Line(2L, "4호선", "하늘색", sectionsOfLineFour)
        );
        given(lineRepository.findAll()).willReturn(lines);

        //when
        final LinesResponse response = lineService.findLines();

        //then
        assertAll(
                () -> assertThat(response.getLines()).hasSize(2),
                () -> assertThat(response.getLines().get(0).getId()).isEqualTo(1L),
                () -> assertThat(response.getLines().get(0).getName()).isEqualTo("2호선"),
                () -> assertThat(response.getLines().get(0).getColor()).isEqualTo("초록색"),
                () -> assertThat(response.getLines().get(0).getStations()).hasSize(2),
                () -> assertThat(response.getLines().get(0).getStations().get(0).getId()).isEqualTo(1L),
                () -> assertThat(response.getLines().get(0).getStations().get(0).getName()).isEqualTo("잠실역"),
                () -> assertThat(response.getLines().get(0).getStations().get(1).getId()).isEqualTo(2L),
                () -> assertThat(response.getLines().get(0).getStations().get(1).getName()).isEqualTo("잠실새내역"),
                () -> assertThat(response.getLines().get(1).getId()).isEqualTo(2L),
                () -> assertThat(response.getLines().get(1).getName()).isEqualTo("4호선"),
                () -> assertThat(response.getLines().get(1).getColor()).isEqualTo("하늘색"),
                () -> assertThat(response.getLines().get(1).getStations()).hasSize(2),
                () -> assertThat(response.getLines().get(1).getStations().get(0).getId()).isEqualTo(3L),
                () -> assertThat(response.getLines().get(1).getStations().get(0).getName()).isEqualTo("이수역"),
                () -> assertThat(response.getLines().get(1).getStations().get(1).getId()).isEqualTo(4L),
                () -> assertThat(response.getLines().get(1).getStations().get(1).getName()).isEqualTo("서울역")
        );
    }
}
