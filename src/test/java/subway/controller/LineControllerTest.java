package subway.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.controller.dto.LineCreateRequest;
import subway.controller.dto.LineResponse;
import subway.controller.dto.LineStationsResponse;
import subway.controller.dto.LinesResponse;
import subway.controller.dto.StationResponse;
import subway.service.LineService;

@WebMvcTest(LineController.class)
class LineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LineService lineService;

    @Nested
    @DisplayName("노선 생성 요청시 ")
    class CreateLine {

        @Test
        @DisplayName("유효한 노선 정보라면 새로운 노선을 추가한다.")
        void createLine() throws Exception {
            final LineCreateRequest request = new LineCreateRequest("2호선", "초록색");

            given(lineService.createLine(any(LineCreateRequest.class))).willReturn(1L);

            mockMvc.perform(post("/lines")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(header().string(HttpHeaders.LOCATION, containsString("/lines/1")));
        }

        @Test
        @DisplayName("이름이 공백이라면 400 상태를 반환한다.")
        void createLineWithInvalidName() throws Exception {
            final LineCreateRequest request = new LineCreateRequest(" ", "초록색");

            mockMvc.perform(post("/lines")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("노선 이름은 공백일 수 없습니다."));
        }

        @Test
        @DisplayName("색이 공백이라면 400 상태를 반환한다.")
        void createLineWithInvalidColor() throws Exception {
            final LineCreateRequest request = new LineCreateRequest("2호선", " ");

            mockMvc.perform(post("/lines")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("노선 색깔은 공백일 수 없습니다."));
        }
    }

    @Nested
    @DisplayName("노선 조회 시 ")
    class FindLine {

        @Test
        @DisplayName("존재하는 노선이라면 노선 정보를 조회한다.")
        void findLine() throws Exception {
            final List<StationResponse> stations = List.of(
                    new StationResponse(1L, "잠실역"),
                    new StationResponse(2L, "잠실새내역")
            );
            final LineStationsResponse response = new LineStationsResponse(1L, "2호선", "초록색", stations);

            given(lineService.findLineById(1L)).willReturn(response);

            mockMvc.perform(get("/lines/{id}", 1L))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.name").value("2호선"))
                    .andExpect(jsonPath("$.color").value("초록색"))
                    .andExpect(jsonPath("$.stations", hasSize(2)))
                    .andExpect(jsonPath("$.stations[0].id").value(1))
                    .andExpect(jsonPath("$.stations[0].name").value("잠실역"))
                    .andExpect(jsonPath("$.stations[1].id").value("2"))
                    .andExpect(jsonPath("$.stations[1].name").value("잠실새내역"));
        }

        @Test
        @DisplayName("ID로 변환할 수 없는 타입이라면 400 상태를 반환한다.")
        void findLineWithInvalidIDType() throws Exception {
            mockMvc.perform(get("/lines/{id}", "l"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    @Test
    @DisplayName("노선 목록을 조회한다.")
    void findLines() throws Exception {
        final List<LineResponse> lines = List.of(
                new LineResponse(1L, "2호선", "초록색"),
                new LineResponse(2L, "4호선", "하늘색"));
        final LinesResponse response = new LinesResponse(lines);

        given(lineService.findLines()).willReturn(response);

        mockMvc.perform(get("/lines"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lines", hasSize(2)))
                .andExpect(jsonPath("$.lines[0].id").value(1))
                .andExpect(jsonPath("$.lines[0].name").value("2호선"))
                .andExpect(jsonPath("$.lines[0].color").value("초록색"))
                .andExpect(jsonPath("$.lines[1].id").value(2))
                .andExpect(jsonPath("$.lines[1].name").value("4호선"))
                .andExpect(jsonPath("$.lines[1].color").value("하늘색"));
    }
}
