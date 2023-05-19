package subway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.controller.dto.request.StationRequest;
import subway.controller.dto.response.StationResponse;
import subway.exception.StationNotFoundException;
import subway.service.StationService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StationController.class)
public class StationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StationService stationService;

    @Nested
    @DisplayName("역 추가 - POST /stations")
    class Create {

        @Test
        @DisplayName("성공")
        void success() throws Exception {
            // given
            final long id = 1L;
            final String name = "혜화";
            final StationRequest request = new StationRequest(name);
            final String requestBody = objectMapper.writeValueAsString(request);
            final StationResponse response = new StationResponse(id, name);
            given(stationService.save(any())).willReturn(response);

            // when, then
            mockMvc.perform(post("/stations")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(header().string("Location", "/stations/1"))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 id")
        void fail_id_not_found() throws Exception {
            // given
            final long id = 1L;
            final String name = "혜화";
            final StationRequest request = new StationRequest(name);
            final String requestBody = objectMapper.writeValueAsString(request);
            given(stationService.save(any())).willThrow(StationNotFoundException.class);

            // when, then
            mockMvc.perform(post("/stations")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("실패 - 잘못된 request body")
        void fail_invalid_request() throws Exception {
            // given
            final StationRequest request = new StationRequest(null);
            final String requestBody = objectMapper.writeValueAsString(request);
            given(stationService.save(any())).willThrow(DataIntegrityViolationException.class);

            // when, then
            mockMvc.perform(post("/stations")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("역 전체 조회 - GET /stations")
    class ReadAll {

        @Test
        @DisplayName("성공")
        void success() throws Exception {
            // given
            final List<StationResponse> responses = List.of(
                    new StationResponse(1L, "잠실"),
                    new StationResponse(2L, "잠실새내"),
                    new StationResponse(3L, "종합운동장"));
            given(stationService.findAll()).willReturn(responses);

            // when, then
            final String responseBody =
                    "[" +
                            "{\"id\":1,\"name\":\"잠실\"}," +
                            "{\"id\":2,\"name\":\"잠실새내\"}," +
                            "{\"id\":3,\"name\":\"종합운동장\"}" +
                            "]";
            mockMvc.perform(get("/stations"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(responseBody));
        }
    }

    @Nested
    @DisplayName("역 조회 - GET /stations/{id}")
    class Read {

        @Test
        @DisplayName("성공")
        void success() throws Exception {
            // given
            final long id = 1L;
            final StationResponse response = new StationResponse(1L, "잠실");
            given(stationService.findById(id)).willReturn(response);

            // when, then
            final String responseBody = "{\"id\":1,\"name\":\"잠실\"}";
            mockMvc.perform(get("/stations/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(content().json(responseBody));
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 id")
        void fail_id_not_found() throws Exception {
            // given
            final long id = 10L;
            given(stationService.findById(id)).willThrow(StationNotFoundException.class);

            // when, then
            mockMvc.perform(get("/stations/{id}", id))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("역 수정 - PUT /stations/{id}")
    class Update {

        @Test
        @DisplayName("성공")
        void success() throws Exception {
            // given
            final long id = 1L;
            final StationRequest request = new StationRequest("잠실");
            final String requestBody = objectMapper.writeValueAsString(request);

            // when, then
            mockMvc.perform(put("/stations/{id}", id)
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 id")
        void fail_id_not_found() throws Exception {
            // given
            final long id = 10L;
            final StationRequest request = new StationRequest("잠실");
            final String requestBody = objectMapper.writeValueAsString(request);
            doThrow(StationNotFoundException.class)
                    .when(stationService)
                    .update(eq(id), isA(StationRequest.class));

            // when, then
            mockMvc.perform(put("/stations/{id}", id)
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("역 삭제 - DELETE /stations/{id}")
    class Delete {

        @Test
        @DisplayName("성공")
        void success() throws Exception {
            // given
            final long id = 1L;

            // when, then
            mockMvc.perform(delete("/stations/{id}", id))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 id")
        void fail_id_not_found() throws Exception {
            // given
            final long id = 10L;
            doThrow(StationNotFoundException.class)
                    .when(stationService)
                    .delete(eq(id));

            // when, then
            mockMvc.perform(delete("/stations/{id}", id))
                    .andExpect(status().isNotFound());
        }
    }
}
