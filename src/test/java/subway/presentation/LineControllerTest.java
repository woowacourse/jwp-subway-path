package subway.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.business.LineService;
import subway.exception.bad_request.DuplicatedLineNameException;
import subway.exception.not_found.LineNotFoundException;
import subway.exception.not_found.StationNotFoundException;
import subway.presentation.dto.request.LineRequest;
import subway.presentation.dto.request.StationRegisterInLineRequest;
import subway.presentation.dto.response.LineDetailResponse;
import subway.presentation.dto.response.StationResponse;
import subway.presentation.dto.request.converter.SubwayDirection;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LineController.class)
class LineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LineService lineService;

    @Nested
    @DisplayName("노선 추가 - POST /lines")
    class Create {

        @Test
        @DisplayName("성공")
        void success() throws Exception {
            // given
            final LineRequest requestDto = new LineRequest("신분당선", "bg-red-600", 10, "강남", "신논현");
            final String requestBody = objectMapper.writeValueAsString(requestDto);
            given(lineService.save(any(), any())).willReturn(1L);

            // when, then
            mockMvc.perform(post("/lines")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(header().string("Location", "/lines/1"))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("실패 - 중복된 노선 이름")
        void fail_duplicated_name() throws Exception {
            // given
            final LineRequest requestDto = new LineRequest("신분당선", "bg-red-600", 10, "강남", "신논현");
            final String requestBody = objectMapper.writeValueAsString(requestDto);
            given(lineService.save(any(), any())).willThrow(DuplicatedLineNameException.class);

            // when, then
            mockMvc.perform(post("/lines")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 역")
        void fail_station_not_found() throws Exception {
            // given
            final LineRequest requestDto = new LineRequest("신분당선", "bg-red-600", 10, "하이염", "신논현");
            final String requestBody = objectMapper.writeValueAsString(requestDto);
            given(lineService.save(any(), any())).willThrow(StationNotFoundException.class);

            // when, then
            mockMvc.perform(post("/lines")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("노선 목록 조회 - GET /lines")
    class ReadAll {

        @Test
        @DisplayName("성공")
        void success() throws Exception {
            // given
            final List<LineDetailResponse> lineDetailResponses = List.of(
                    new LineDetailResponse(1L, "신분당선", "bg-red-600", List.of(
                            new StationResponse(1L, "정자"), new StationResponse(2L, "판교")
                    )),
                    new LineDetailResponse(2L, "분당선", "bg-yellow-600", List.of(
                            new StationResponse(1L, "정자"), new StationResponse(3L, "수내")
                    ))
            );
            given(lineService.findAll()).willReturn(lineDetailResponses);

            // when, then
            final String responseBody =
                    "[" +
                            "{\"id\":1,\"name\":\"신분당선\",\"color\":\"bg-red-600\",\"stations\": [" +
                            "   {\"id\":1,\"name\":\"정자\"}," +
                            "   {\"id\":2,\"name\":\"판교\"}" +
                            "]}," +
                            "{\"id\":2,\"name\":\"분당선\",\"color\":\"bg-yellow-600\",\"stations\": [" +
                            "   {\"id\":1,\"name\":\"정자\"}," +
                            "   {\"id\":3,\"name\":\"수내\"}" +
                            "]}" +
                            "]";
            mockMvc.perform(get("/lines"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(responseBody));
        }
    }

    @Nested
    @DisplayName("노선 조회 - GET /lines/{id}")
    class Read {

        @Test
        @DisplayName("성공")
        void success() throws Exception {
            // given
            final long lineId = 1L;
            final LineDetailResponse lineDetailResponse =
                    new LineDetailResponse(lineId, "신분당선", "bg-red-600", List.of(
                            new StationResponse(1L, "정자"), new StationResponse(2L, "판교")
                    ));
            given(lineService.findById(lineId)).willReturn(lineDetailResponse);

            // when, then
            final String responseBody =
                    "{" +
                            "\"id\":1,\"name\":\"신분당선\",\"color\":\"bg-red-600\",\"stations\": [" +
                            "   {\"id\":1,\"name\":\"정자\"}," +
                            "   {\"id\":2,\"name\":\"판교\"}" +
                            "]}";
            mockMvc.perform(get("/lines/{id}", lineId))
                    .andExpect(status().isOk())
                    .andExpect(content().json(responseBody));
        }

        @Test
        @DisplayName("실패 - 잘못된 line id")
        void fail_invalid_line_id() throws Exception {
            // given
            final long lineId = 10L;

            // when
            when(lineService.findById(lineId)).thenThrow(LineNotFoundException.class);

            // then
            mockMvc.perform(get("/lines/{id}", lineId))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("노선에 역 추가 - PATCH /lines/{id}/register")
    class Register {

        @Test
        @DisplayName("성공 - 상행 끝점에 추가")
        void success_upper_end_point() throws Exception {
            // given
            final long lineId = 1L;
            final StationRegisterInLineRequest requestDto = new StationRegisterInLineRequest(SubwayDirection.UP, "잠실", "송파", 5);
            final String requestBody = objectMapper.writeValueAsString(requestDto);
            final LineDetailResponse lineDetailResponse = new LineDetailResponse(lineId, "2호선", "bg-green-600",
                    List.of(new StationResponse(1L, "잠실"), new StationResponse(5L, "송파"), new StationResponse(2L, "잠실새내"))
            );

            // when
            when(lineService.registerStation(eq(lineId), any())).thenReturn(lineDetailResponse);

            // then
            final String responseBody =
                    "{" +
                            "\"id\":1,\"name\":\"2호선\",\"color\":\"bg-green-600\",\"stations\": [" +
                            "   {\"id\":1,\"name\":\"잠실\"}," +
                            "   {\"id\":5,\"name\":\"송파\"}," +
                            "   {\"id\":2,\"name\":\"잠실새내\"}" +
                            "]}";
            mockMvc.perform(patch("/lines/{id}/register", lineId)
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isOk())
                    .andExpect(content().json(responseBody));
        }

        @Test
        @DisplayName("성공 - 하행 끝점에 추가")
        void success_down_end_point() throws Exception {
            // given
            final long lineId = 1L;
            final StationRegisterInLineRequest requestDto = new StationRegisterInLineRequest(SubwayDirection.DOWN, "잠실", "송파", 5);
            final String requestBody = objectMapper.writeValueAsString(requestDto);
            final LineDetailResponse lineDetailResponse = new LineDetailResponse(lineId, "2호선", "bg-green-600",
                    List.of(new StationResponse(1L, "잠실새내"), new StationResponse(5L, "송파"), new StationResponse(2L, "잠실"))
            );

            // when
            when(lineService.registerStation(eq(lineId), any())).thenReturn(lineDetailResponse);

            // then
            final String responseBody =
                    "{" +
                            "\"id\":1,\"name\":\"2호선\",\"color\":\"bg-green-600\",\"stations\": [" +
                            "   {\"id\":1,\"name\":\"잠실새내\"}," +
                            "   {\"id\":5,\"name\":\"송파\"}," +
                            "   {\"id\":2,\"name\":\"잠실\"}" +
                            "]}";
            mockMvc.perform(patch("/lines/{id}/register", lineId)
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isOk())
                    .andExpect(content().json(responseBody));
        }
    }

}
