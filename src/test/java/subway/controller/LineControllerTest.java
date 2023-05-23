package subway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.exception.DuplicatedStationNameException;
import subway.service.LineModifyService;
import subway.service.LineService;
import subway.exception.DuplicatedLineNameException;
import subway.exception.LineNotFoundException;
import subway.exception.StationNotFoundException;
import subway.controller.dto.request.LineRequest;
import subway.controller.dto.request.StationRegisterInLineRequest;
import subway.controller.dto.request.StationUnregisterInLineRequest;
import subway.controller.dto.response.LineResponse;
import subway.controller.dto.response.StationResponse;
import subway.controller.dto.request.SubwayDirection;

import java.util.List;
import java.util.Optional;

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

    @MockBean
    private LineModifyService lineModifyService;

    @Nested
    @DisplayName("노선 추가 - POST /lines")
    class Create {

        @Test
        @DisplayName("성공")
        void success() throws Exception {
            // given
            final LineRequest requestDto = new LineRequest("신분당선", "bg-red-600", 10, "강남", "신논현");
            final String requestBody = objectMapper.writeValueAsString(requestDto);
            given(lineService.create(any(), any())).willReturn(1L);

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
            given(lineService.create(any(), any())).willThrow(DuplicatedLineNameException.class);

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
            given(lineService.create(any(), any())).willThrow(StationNotFoundException.class);

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
            final List<LineResponse> lineResponses = List.of(
                    new LineResponse(1L, "신분당선", "bg-red-600", List.of(
                            new StationResponse(1L, "정자"), new StationResponse(2L, "판교")
                    )),
                    new LineResponse(2L, "분당선", "bg-yellow-600", List.of(
                            new StationResponse(1L, "정자"), new StationResponse(3L, "수내")
                    ))
            );
            given(lineService.findAll()).willReturn(lineResponses);

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
            final LineResponse lineResponse =
                    new LineResponse(lineId, "신분당선", "bg-red-600", List.of(
                            new StationResponse(1L, "정자"), new StationResponse(2L, "판교")
                    ));
            given(lineService.findById(lineId)).willReturn(lineResponse);

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
        @DisplayName("성공 - 상행 중간에 추가")
        void success_upper_mid() throws Exception {
            // given
            final long lineId = 1L;
            final StationRegisterInLineRequest requestDto = new StationRegisterInLineRequest(SubwayDirection.UP, 2L, 5L, 5);
            final String requestBody = objectMapper.writeValueAsString(requestDto);
            final LineResponse lineResponse = new LineResponse(lineId, "2호선", "bg-green-600",
                    List.of(new StationResponse(1L, "잠실"), new StationResponse(2L, "잠실새내"), new StationResponse(5L, "송파"))
            );

            // when
            when(lineModifyService.registerStation(eq(lineId), any())).thenReturn(lineResponse);

            // then
            final String responseBody =
                    "{" +
                            "\"id\":1,\"name\":\"2호선\",\"color\":\"bg-green-600\",\"stations\": [" +
                            "   {\"id\":1,\"name\":\"잠실\"}," +
                            "   {\"id\":2,\"name\":\"잠실새내\"}," +
                            "   {\"id\":5,\"name\":\"송파\"}" +
                            "]}";
            mockMvc.perform(patch("/lines/{id}/register", lineId)
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isOk())
                    .andExpect(content().json(responseBody));
        }

        @Test
        @DisplayName("성공 - 상행 끝점에 추가")
        void success_upper_end_point() throws Exception {
            // given
            final long lineId = 1L;
            final StationRegisterInLineRequest requestDto = new StationRegisterInLineRequest(SubwayDirection.UP, 1L, 5L, 5);
            final String requestBody = objectMapper.writeValueAsString(requestDto);
            final LineResponse lineResponse = new LineResponse(lineId, "2호선", "bg-green-600",
                    List.of(new StationResponse(1L, "잠실"), new StationResponse(5L, "송파"), new StationResponse(2L, "잠실새내"))
            );

            // when
            when(lineModifyService.registerStation(eq(lineId), any())).thenReturn(lineResponse);

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
        @DisplayName("성공 - 하행 중간에 추가")
        void success_down_mid() throws Exception {
            // given
            final long lineId = 1L;
            final StationRegisterInLineRequest requestDto = new StationRegisterInLineRequest(SubwayDirection.DOWN, 1L, 5L, 5);
            final String requestBody = objectMapper.writeValueAsString(requestDto);
            final LineResponse lineResponse = new LineResponse(lineId, "2호선", "bg-green-600",
                    List.of(new StationResponse(1L, "잠실새내"), new StationResponse(5L, "송파"), new StationResponse(2L, "잠실"))
            );

            // when
            when(lineModifyService.registerStation(eq(lineId), any())).thenReturn(lineResponse);

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

        @Test
        @DisplayName("성공 - 하행 끝점에 추가")
        void success_down_end_point() throws Exception {
            // given
            final long lineId = 1L;
            final StationRegisterInLineRequest requestDto = new StationRegisterInLineRequest(SubwayDirection.DOWN, 1L, 5L, 5);
            final String requestBody = objectMapper.writeValueAsString(requestDto);
            final LineResponse lineResponse = new LineResponse(lineId, "2호선", "bg-green-600",
                    List.of(new StationResponse(5L, "송파"), new StationResponse(1L, "잠실새내"), new StationResponse(2L, "잠실"))
            );

            // when
            when(lineModifyService.registerStation(eq(lineId), any())).thenReturn(lineResponse);

            // then
            final String responseBody =
                    "{" +
                            "\"id\":1,\"name\":\"2호선\",\"color\":\"bg-green-600\",\"stations\": [" +
                            "   {\"id\":5,\"name\":\"송파\"}," +
                            "   {\"id\":1,\"name\":\"잠실새내\"}," +
                            "   {\"id\":2,\"name\":\"잠실\"}" +
                            "]}";
            mockMvc.perform(patch("/lines/{id}/register", lineId)
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isOk())
                    .andExpect(content().json(responseBody));
        }

        @Test
        @DisplayName("실패 - 중복된 역이름")
        void fail_duplicated_station_name() throws Exception {
            // given
            final long lineId = 1L;
            final StationRegisterInLineRequest requestDto = new StationRegisterInLineRequest(SubwayDirection.DOWN, 2L, 1L, 5);
            final String requestBody = objectMapper.writeValueAsString(requestDto);

            // when
            when(lineModifyService.registerStation(eq(lineId), any())).thenThrow(DuplicatedStationNameException.class);

            // then
            mockMvc.perform(patch("/lines/{id}/register", lineId)
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 노선")
        void fail_duplicated_line() throws Exception {
            // given
            final long lineId = 2L;
            final StationRegisterInLineRequest requestDto = new StationRegisterInLineRequest(SubwayDirection.DOWN, 2L, 1L, 5);
            final String requestBody = objectMapper.writeValueAsString(requestDto);

            // when
            when(lineModifyService.registerStation(eq(lineId), any())).thenThrow(DuplicatedLineNameException.class);

            // then
            mockMvc.perform(patch("/lines/{id}/register", lineId)
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("노선에 역 제거 - PATCH /lines/{id}/unregister")
    class Unregister {

        @Test
        @DisplayName("성공 - 역 하나 제거")
        void success_delete_one_station() throws Exception {
            // given
            final long lineId = 1L;
            final StationUnregisterInLineRequest requestDto = new StationUnregisterInLineRequest(1L);
            final String requestBody = objectMapper.writeValueAsString(requestDto);
            final LineResponse lineResponse = new LineResponse(lineId, "2호선", "bg-green-600",
                    List.of(new StationResponse(2L, "잠실새내"), new StationResponse(3L, "종합운동장"))
            );

            // when
            when(lineModifyService.unregisterStation(eq(lineId), any())).thenReturn(Optional.of(lineResponse));

            // then
            final String responseBody =
                    "{" +
                            "\"id\":1,\"name\":\"2호선\",\"color\":\"bg-green-600\",\"stations\": [" +
                            "   {\"id\":2,\"name\":\"잠실새내\"}," +
                            "   {\"id\":3,\"name\":\"종합운동장\"}" +
                            "]}";
            mockMvc.perform(patch("/lines/{id}/unregister", lineId)
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isOk())
                    .andExpect(content().json(responseBody));
        }

        @Test
        @DisplayName("성공 - 노선도 제거되는 경우")
        void success_delete_with_line() throws Exception {
            // given
            final long lineId = 1L;
            final StationUnregisterInLineRequest requestDto = new StationUnregisterInLineRequest(1L);
            final String requestBody = objectMapper.writeValueAsString(requestDto);
            final Optional<LineResponse> lineDetailResponse = Optional.empty();

            // when
            when(lineModifyService.unregisterStation(eq(lineId), any())).thenReturn(lineDetailResponse);

            // then
            mockMvc.perform(patch("/lines/{id}/unregister", lineId)
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 노선")
        void fail_line_not_found() throws Exception {
            // given
            final long lineId = 11L;
            final StationUnregisterInLineRequest requestDto = new StationUnregisterInLineRequest(1L);
            final String requestBody = objectMapper.writeValueAsString(requestDto);

            // when
            when(lineModifyService.unregisterStation(eq(lineId), any())).thenThrow(LineNotFoundException.class);

            // then
            mockMvc.perform(patch("/lines/{id}/unregister", lineId)
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 역")
        void fail_station_not_found() throws Exception {
            // given
            final long lineId = 1L;
            final StationUnregisterInLineRequest requestDto = new StationUnregisterInLineRequest(11L);
            final String requestBody = objectMapper.writeValueAsString(requestDto);

            // when
            when(lineModifyService.unregisterStation(eq(lineId), any())).thenThrow(StationNotFoundException.class);

            // then
            mockMvc.perform(patch("/lines/{id}/unregister", lineId)
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isNotFound());
        }
    }
}
