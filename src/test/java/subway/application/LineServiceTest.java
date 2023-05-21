package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineStationResponse;
import subway.repository.LineRepository;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @Mock
    private LineRepository lineRepository;
    private LineService lineService;

    @BeforeEach
    void setUp() {
        lineService = new LineService(lineRepository);
    }

    @DisplayName("Line이 정상적으로 저장되고 lineId 값을 반환한다.")
    @Test
    void save() {
        // given
        given(lineRepository.save(any(Line.class))).willReturn(1L);
        LineRequest lineRequest = new LineRequest("2호선", "bg-green-600");

        // when, then
        assertThat(lineService.save(lineRequest)).isEqualTo(1L);
    }

    @DisplayName("모든 노선의 정보와 각 노선에 있는 구간을 조회하여 순서대로 반환한다.")
    @Test
    void findAll() {
        // given
        Station station1 = new Station(1L, "용산역");
        Station station2 = new Station(2L, "죽전역");
        Section section = new Section(station1, station2, 3);
        Line line1 = new Line(1L, "1호선", "bg-red-500", new Sections(Collections.emptyList()));
        Line line2 = new Line(2L, "2호선", "bg-green-600", new Sections(List.of(section)));
        given(lineRepository.findAll()).willReturn(List.of(line1, line2));

        // when
        List<LineStationResponse> lineStationResponses = lineService.findAll();
        LineStationResponse lineStationResponse1 = lineStationResponses.get(0);
        LineStationResponse lineStationResponse2 = lineStationResponses.get(1);

        // then
        assertThat(lineStationResponse1.getLineResponse()).usingRecursiveComparison().isEqualTo(line1);
        assertThat(lineStationResponse1.getStationResponses().size()).isEqualTo(0);
        assertThat(lineStationResponse2.getLineResponse()).usingRecursiveComparison().isEqualTo(line2);
        assertThat(lineStationResponse2.getStationResponses().get(0)).usingRecursiveComparison().isEqualTo(station1);
        assertThat(lineStationResponse2.getStationResponses().get(1)).usingRecursiveComparison().isEqualTo(station2);
    }

    @DisplayName("특정 노선의 정보와 노선에 있는 구간을 조회하여 순서대로 반환한다.")
    @Test
    void findById() {
        // given
        Station station1 = new Station(1L, "반월당역");
        Station station2 = new Station(2L, "신천역");
        Station station3 = new Station(3L, "동대구역");
        Section section1 = new Section(station1, station2, 3);
        Section section2 = new Section(station2, station3, 4);
        Line line = new Line(1L, "1호선", "bg-red-500", new Sections(List.of(section1, section2)));
        given(lineRepository.findById(anyLong())).willReturn(line);

        // when
        LineStationResponse lineStationResponse = lineService.findById(1L);

        // then
        assertThat(lineStationResponse.getLineResponse()).usingRecursiveComparison().isEqualTo(line);
        assertThat(lineStationResponse.getStationResponses().get(0)).usingRecursiveComparison().isEqualTo(station1);
        assertThat(lineStationResponse.getStationResponses().get(1)).usingRecursiveComparison().isEqualTo(station2);
        assertThat(lineStationResponse.getStationResponses().get(2)).usingRecursiveComparison().isEqualTo(station3);
    }

    @DisplayName("노선 정보를 업데이트 한다.")
    @Test
    void update() {
        // given
        given(lineRepository.save(any(Line.class))).willReturn(1L);
        LineRequest lineRequest = new LineRequest("1호선", "bg-red-500");
        lineService.save(lineRequest);
        willDoNothing().given(lineRepository).update(anyLong(), any(Line.class));

        // when, then
        LineRequest lineUpdateRequest = new LineRequest("2호선", "bg-green-600");
        assertDoesNotThrow(() -> lineService.update(1L, lineUpdateRequest));
    }

    @DisplayName("노선 정보를 삭제한다.")
    @Test
    void deleteById() {
        // given
        given(lineRepository.save(any(Line.class))).willReturn(1L);
        LineRequest lineRequest = new LineRequest("1호선", "bg-blue-500");
        lineService.save(lineRequest);
        willDoNothing().given(lineRepository).deleteById(anyLong());

        // when, then
        assertDoesNotThrow(() -> lineService.deleteById(1L));
    }
}
