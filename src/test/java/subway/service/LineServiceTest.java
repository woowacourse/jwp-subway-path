package subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.controller.dto.request.LineCreateRequest;
import subway.controller.dto.request.SectionCreateRequest;
import subway.controller.dto.response.LineResponse;
import subway.controller.dto.response.LinesResponse;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.exception.InvalidDistanceException;
import subway.exception.InvalidLineNameException;
import subway.exception.InvalidSectionException;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @InjectMocks
    private LineService lineService;

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationRepository stationRepository;

    @Test
    @DisplayName("노선 목록을 조회한다.")
    void findLines() {
        final List<Section> sectionsOfLineTwo = List.of(
                new Section(new Station(1L, "잠실역"), new Station(2L, "잠실새내역"), 10),
                new Section(new Station(2L, "잠실새내역"), Station.TERMINAL, 0)
        );
        final List<Section> sectionsOfLineFour = List.of(
                new Section(new Station(3L, "이수역"), new Station(4L, "서울역"), 11),
                new Section(new Station(4L, "서울역"), Station.TERMINAL, 0)
        );
        final List<Line> lines = List.of(
                new Line(1L, "2호선", "초록색", 500, sectionsOfLineTwo),
                new Line(2L, "4호선", "하늘색", 500, sectionsOfLineFour)
        );
        given(lineRepository.findAll()).willReturn(lines);

        final LinesResponse response = lineService.findLines();

        final List<LineResponse> lineResponses = lines.stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
        assertThat(response).usingRecursiveComparison().isEqualTo(new LinesResponse(lineResponses));
    }

    @Nested
    @DisplayName("노선 추가시 ")
    class CreateLine {

        @Test
        @DisplayName("유효한 정보라면 노선을 추가한다.")
        void createLine() {
            final Line line = new Line(1L, "2호선", "초록색", 500);
            final LineCreateRequest request = new LineCreateRequest("2호선", "초록색", 500);
            given(lineRepository.save(any(Line.class))).willReturn(line);

            final Long lineId = lineService.createLine(request);

            assertThat(lineId).isEqualTo(1L);
        }

        @Test
        @DisplayName("유효하지 않은 정보라면 예외를 던진다.")
        void createLineWithInvalidName() {
            final LineCreateRequest request = new LineCreateRequest("경의중앙선", "초록색", 500);

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
            final List<Section> sections = List.of(
                    new Section(new Station(1L, "잠실역"), new Station(2L, "잠실새내역"), 10),
                    new Section(new Station(2L, "잠실새내역"), Station.TERMINAL, 0)
            );
            final Line line = new Line(1L, "2호선", "초록색", 500, sections);
            given(lineRepository.findById(1L)).willReturn(line);

            final LineResponse response = lineService.findLineById(1L);

            assertThat(response).usingRecursiveComparison().isEqualTo(LineResponse.from(line));
        }
    }

    @Nested
    @DisplayName("노선에 역 등록 시")
    class CreateSection {

        @Test
        @DisplayName("유효한 정보라면 노선에 역을 등록한다.")
        void createSection() {
            final SectionCreateRequest request = new SectionCreateRequest(1L, 3L, 2);
            final List<Section> sections = List.of(
                    new Section(new Station(1L, "잠실역"), new Station(2L, "잠실새내역"), 10),
                    new Section(new Station(2L, "잠실새내역"), Station.TERMINAL, 0)
            );
            final Line line = new Line(1L, "2호선", "초록색", 500, new ArrayList<>(sections));
            given(lineRepository.findById(1L)).willReturn(line);
            given(stationRepository.findById(1L)).willReturn(new Station(1L, "잠실역"));
            given(stationRepository.findById(3L)).willReturn(new Station(3L, "종합운동장역"));
            willDoNothing().given(lineRepository).update(any(Line.class));

            lineService.createSection(1L, request);

            assertThat(line.getSections()).hasSize(2);
        }

        @Test
        @DisplayName("유효하지 않은 정보라면 예외를 던진다.")
        void createSectionWithInvalidDistance() {
            final SectionCreateRequest request = new SectionCreateRequest(1L, 3L, 10);
            final List<Section> sections = List.of(
                    new Section(new Station(1L, "잠실역"), new Station(2L, "잠실새내역"), 10),
                    new Section(new Station(2L, "잠실새내역"), Station.TERMINAL, 0)
            );
            final Line line = new Line(1L, "2호선", "초록색", 500, new ArrayList<>(sections));
            given(lineRepository.findById(1L)).willReturn(line);
            given(stationRepository.findById(1L)).willReturn(new Station(1L, "잠실역"));
            given(stationRepository.findById(3L)).willReturn(new Station(3L, "종합운동장역"));

            assertThatThrownBy(() -> lineService.createSection(1L, request))
                    .isInstanceOf(InvalidDistanceException.class);
        }
    }

    @Nested
    @DisplayName("노선에서 역 삭제시 ")
    class DeleteStation {

        @Test
        @DisplayName("유효한 정보라면 역을 삭제한다.")
        void deleteStation() {
            final List<Section> sections = List.of(
                    new Section(new Station(1L, "잠실역"), new Station(2L, "잠실새내역"), 10),
                    new Section(new Station(2L, "잠실새내역"), Station.TERMINAL, 0)
            );
            final Line line = new Line(1L, "2호선", "초록색", 500, new ArrayList<>(sections));
            given(lineRepository.findById(1L)).willReturn(line);
            given(stationRepository.findById(1L)).willReturn(new Station(1L, "잠실역"));
            willDoNothing().given(lineRepository).update(any(Line.class));

            lineService.deleteStation(1L, 1L);

            assertThat(line.getSections()).isEmpty();
        }

        @Test
        @DisplayName("유효하지 않은 정보라면 예외를 던진다.")
        void deleteStationWithInvalidStationId() {
            final List<Section> sections = List.of(
                    new Section(new Station(1L, "잠실역"), new Station(2L, "잠실새내역"), 10),
                    new Section(new Station(2L, "잠실새내역"), Station.TERMINAL, 0)
            );
            final Line line = new Line(1L, "2호선", "초록색", 500, new ArrayList<>(sections));
            given(lineRepository.findById(1L)).willReturn(line);
            given(stationRepository.findById(3L)).willReturn(new Station(3L, "종합운동장역"));

            assertThatThrownBy(() -> lineService.deleteStation(1L, 3L))
                    .isInstanceOf(InvalidSectionException.class);
        }
    }
}
