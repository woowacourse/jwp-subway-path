package subway.line.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.line.application.LineCommandService;
import subway.line.application.LineQueryService;
import subway.line.application.dto.RegisterLineRequest;
import subway.line.domain.Line;
import subway.line.domain.Section;
import subway.line.domain.Station;
import subway.line.domain.Stations;

@WebMvcTest(LineController.class)
class LineControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private LineCommandService lineCommandService;

  @MockBean
  private LineQueryService lineQueryService;

  @Test
  @DisplayName("showLines() : lineName이 주어지면 해당 line을 성공적으로 조회할 때 200 OK을 반환한다.")
  void test_showLines() throws Exception {
    //given
    final String lineName = "2호선";

    final List<Line> lines = createDefaultLines();

    when(lineQueryService.searchLines(any()))
        .thenReturn(lines);

    //when & then
    mockMvc.perform(get("/lines?lineName={lineName}", lineName))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("showLines() : lineName이 주어지지 않으면 모든 line을 성공적으로 조회할 때 200 OK을 반환한다.")
  void test_showLines_all() throws Exception {
    //given
    final List<Line> lines = createDefaultLines();

    when(lineQueryService.searchLines(any()))
        .thenReturn(lines);

    //when & then
    mockMvc.perform(get("/lines"))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("showLine() : id를 통해 해당 Line을 성공적으로 조회할 때 200 OK을 반환한다.")
  void test_showLine() throws Exception {
    //given
    final List<Line> lines = createDefaultLines();
    final long lineId = 1L;

    when(lineQueryService.searchByLineId(anyLong()))
        .thenReturn(lines.get(0));

    //when & then
    mockMvc.perform(get("/lines/{line-id}", lineId))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("registerLine() : Line을 성공적으로 저장할 때 201 Created를 반환한다.")
  void test_registerLine() throws Exception {
    //given
    final String currentStationName = "A";
    final String nextStationName = "B";
    final String lineName = "line";
    final int distance = 4;

    final RegisterLineRequest registerLineRequest =
        new RegisterLineRequest(currentStationName, nextStationName, lineName, distance);

    final String bodyData = objectMapper.writeValueAsString(registerLineRequest);

    doNothing().when(lineCommandService)
        .registerLine(any());

    when(lineQueryService.searchByLineName(any()))
        .thenReturn(createDefaultLines().get(0));

    //when & then
    mockMvc.perform(post("/lines")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyData))
        .andExpect(status().isCreated());
  }

  private List<Line> createDefaultLines() {
    final Stations stations1 = new Stations(new Station("A"), new Station("B"), 1);
    final Stations stations2 = new Stations(new Station("B"), new Station("C"), 2);
    final Stations stations3 = new Stations(new Station("C"), new Station("D"), 3);

    final Section section1 = new Section(stations1);
    final Section section2 = new Section(stations2);
    final Section section3 = new Section(stations3);

    final Line line1 = new Line(1L, "1호선", List.of(section1, section2, section3));

    final Stations stations4 = new Stations(new Station("B"), new Station("F"), 4);
    final Stations stations5 = new Stations(new Station("F"), new Station("G"), 11);
    final Stations stations6 = new Stations(new Station("G"), new Station("H"), 5);
    final Stations stations7 = new Stations(new Station("H"), new Station("D"), 4);

    final Section section4 = new Section(stations4);
    final Section section5 = new Section(stations5);
    final Section section6 = new Section(stations6);
    final Section section7 = new Section(stations7);

    final Line line2 = new Line(2L, "2호선",
        List.of(section4, section5, section6, section7));

    return List.of(line1, line2);
  }
}
