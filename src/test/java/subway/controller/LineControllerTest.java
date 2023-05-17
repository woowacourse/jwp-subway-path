package subway.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import subway.controller.dto.LinesResponse;
import subway.controller.dto.SectionCreateRequest;
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
            final LineCreateRequest request = new LineCreateRequest("2호선", "초록색", 300);

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
            final LineCreateRequest request = new LineCreateRequest(" ", "초록색", 300);

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
            final LineCreateRequest request = new LineCreateRequest("2호선", " ", 300);

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
            final LineResponse response = new LineResponse(1L, "2호선", "초록색", 300, stations);

            given(lineService.findLineById(1L)).willReturn(response);

            mockMvc.perform(get("/lines/{id}", 1L))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.name").value("2호선"))
                    .andExpect(jsonPath("$.color").value("초록색"))
                    .andExpect(jsonPath("$.extraFare").value(300))
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

        final List<StationResponse> stationsOfLineTwo = List.of(
                new StationResponse(1L, "잠실역"),
                new StationResponse(2L, "잠실새내역")
        );

        final List<StationResponse> stationsOfLineFour = List.of(
                new StationResponse(3L, "이수역"),
                new StationResponse(4L, "서울역")
        );

        final List<LineResponse> lines = List.of(
                new LineResponse(1L, "2호선", "초록색", 300, stationsOfLineTwo),
                new LineResponse(2L, "4호선", "하늘색", 400, stationsOfLineFour));
        final LinesResponse response = new LinesResponse(lines);

        given(lineService.findLines()).willReturn(response);

        mockMvc.perform(get("/lines"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lines", hasSize(2)))
                .andExpect(jsonPath("$.lines[0].id").value(1))
                .andExpect(jsonPath("$.lines[0].name").value("2호선"))
                .andExpect(jsonPath("$.lines[0].color").value("초록색"))
                .andExpect(jsonPath("$.lines[0].extraFare").value(300))
                .andExpect(jsonPath("$.lines[0].stations", hasSize(2)))
                .andExpect(jsonPath("$.lines[0].stations[0].id").value(1))
                .andExpect(jsonPath("$.lines[0].stations[0].name").value("잠실역"))
                .andExpect(jsonPath("$.lines[0].stations[1].id").value(2))
                .andExpect(jsonPath("$.lines[0].stations[1].name").value("잠실새내역"))
                .andExpect(jsonPath("$.lines[1].id").value(2))
                .andExpect(jsonPath("$.lines[1].name").value("4호선"))
                .andExpect(jsonPath("$.lines[1].color").value("하늘색"))
                .andExpect(jsonPath("$.lines[1].extraFare").value(400))
                .andExpect(jsonPath("$.lines[1].stations", hasSize(2)))
                .andExpect(jsonPath("$.lines[1].stations[0].id").value(3))
                .andExpect(jsonPath("$.lines[1].stations[0].name").value("이수역"))
                .andExpect(jsonPath("$.lines[1].stations[1].id").value(4))
                .andExpect(jsonPath("$.lines[1].stations[1].name").value("서울역"));
    }

    @Nested
    @DisplayName("노선에 역을 등록할 시 ")
    class CreateSection {

        @Test
        @DisplayName("유효한 정보가 입력되면 노선에 역을 등록한다.")
        void createSection() throws Exception {
            final SectionCreateRequest request = new SectionCreateRequest(1L, 2L, 10);

            willDoNothing().given(lineService).createSection(1L, mock(SectionCreateRequest.class));

            mockMvc.perform(post("/lines/{id}/sections", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(header().string(HttpHeaders.LOCATION, containsString("/lines/1")));
        }

        @Test
        @DisplayName("상행 역 ID가 입력되지 않으면 400 상태를 반환한다.")
        void createSectionWithoutUpwardStationId() throws Exception {
            final SectionCreateRequest request = new SectionCreateRequest(null, 2L, 10);

            mockMvc.perform(post("/lines/{id}/sections", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("상행 역 ID는 존재해야 합니다."));
        }

        @Test
        @DisplayName("하행 역 ID가 입력되지 않으면 400 상태를 반환한다.")
        void createSectionWithoutDownwardStationId() throws Exception {
            final SectionCreateRequest request = new SectionCreateRequest(1L, null, 10);

            mockMvc.perform(post("/lines/{id}/sections", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("하행 역 ID는 존재해야 합니다."));
        }

        @Test
        @DisplayName("역 간의 거리가 입력되지 않으면 400 상태를 반환한다.")
        void createSectionWithoutDistance() throws Exception {
            final SectionCreateRequest request = new SectionCreateRequest(1L, 2L, null);

            mockMvc.perform(post("/lines/{id}/sections", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("역 간의 거리는 존재해야 합니다."));
        }

        @Test
        @DisplayName("역 간의 거리가 0이하이면 400 상태를 반환한다.")
        void createSectionWithNegativeDistance() throws Exception {
            final SectionCreateRequest request = new SectionCreateRequest(1L, 2L, -1);

            mockMvc.perform(post("/lines/{id}/sections", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("역 간의 거리는 0보다 커야합니다."));
        }
    }

    @Nested
    @DisplayName("노선에서 역 삭제 요청 시")
    class DeleteStation {

        @Test
        @DisplayName("유효한 요청이라면 역을 삭제한다.")
        void deleteStation() throws Exception {
            willDoNothing().given(lineService).deleteStation(any(Long.class), any(Long.class));

            mockMvc.perform(delete("/lines/{lineId}", 1L)
                            .queryParam("stationId", String.valueOf(1L)))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("역 아이디가 존재하지 않으면 400 상태를 반환한다.")
        void deleteStationWithoutStationId() throws Exception {
            mockMvc.perform(delete("/lines/{lineId}", 1L))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("역 아이디로 변환할 수 없는 타입이면 400 상태를 반환한다.")
        void deleteStationWithInvalidStationIDType() throws Exception {
            mockMvc.perform(delete("/lines/{lineId}", 1L)
                            .queryParam("stationId", "s"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }
}
