package subway.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.LineService;
import subway.dto.LineRequest;

import static fixtures.LineFixtures.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
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

    @Test
    @DisplayName("POST /lines 노선 정보를 등록한다.")
    void createLineTest() throws Exception {
        // given
        LineRequest request = REQUEST_LINE7;
        Long response = LINE7_ID;
        when(lineService.saveLine(REQUEST_LINE7)).thenReturn(response);

        // when, then
        mockMvc.perform(post("/lines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/lines/" + response));
    }

    @Test
    @DisplayName("GET /lines/{id} uri로 요청하면 id에 해당하는 노선의 정보를 반환한다.")
    void findLineByIdTest() throws Exception {
        // given
        Long lineId = 1L;
        when(lineService.findStationNamesByLineId(lineId)).thenReturn(LINE2_노선도);

        // when, then
        mockMvc.perform(get("/lines/" + lineId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lineName").value(is(LINE2_NAME)))
                .andExpect(jsonPath("$.stationNames").value(is(LINE2_노선도.getStationNames())));
    }

    @Test
    @DisplayName("GET /lines uri로 요청하면 모든 노선의 정보를 반환한다.")
    void findAllLinesTest() throws Exception {
        // given
        when(lineService.findAllLineStationNames())
                .thenReturn(ALL_노선도);

        // when, then
        mockMvc.perform(get("/lines"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lineName").value(is(LINE2_NAME)))
                .andExpect(jsonPath("$[0].stationNames").value(is(LINE2_노선도.getStationNames())))
                .andExpect(jsonPath("$[1].lineName").value(is(LINE7_NAME)))
                .andExpect(jsonPath("$[1].stationNames").value(is(LINE7_노선도.getStationNames())));
    }

    @Test
    @DisplayName("PUT /lines 노선 정보를 수정한다.")
    void updateLineTest() throws Exception {
        // given
        Long targetLineId = LINE2_ID;
        LineRequest request = REQUEST_NEW_LINE2;
        doNothing().when(lineService).updateLine(LINE2_ID, REQUEST_NEW_LINE2);

        // when, then
        mockMvc.perform(put("/lines/" + targetLineId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}