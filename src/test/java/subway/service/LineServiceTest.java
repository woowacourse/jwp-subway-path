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
            final Line line = new Line(1L, "2호선", "초록색");
            final LineCreateRequest request = new LineCreateRequest("2호선", "초록색");
            given(lineRepository.save(any(Line.class))).willReturn(line);

            final Long lineId = lineService.createLine(request);

            assertThat(lineId).isEqualTo(1L);
        }

        @Test
        @DisplayName("유효하지 않은 정보라면 예외를 던진다.")
        void createLineWithInvalidName() {
            final LineCreateRequest request = new LineCreateRequest("경의중앙선", "초록색");

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
            final Line line = new Line(1L, "2호선", "초록색", sections);

            given(lineRepository.findById(1L)).willReturn(line);

            final LineResponse response = lineService.findLineById(1L);

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
}
