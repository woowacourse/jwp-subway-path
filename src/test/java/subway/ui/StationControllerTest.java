package subway.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.StationService;
import subway.dto.StationRequest;
import subway.dto.StationSaveResponse;

import static fixtures.StationFixtures.SAVE_INITIAL_STATIONS_잠실_TO_건대_RESPONSE;
import static fixtures.StationFixtures.잠실_TO_건대_REQUEST;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StationController.class)
class StationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StationService stationService;

    @Test
    @DisplayName("POST /stations uri로 요청하면 201을 반환한다.")
    void createStationTest() throws Exception {
        // given
        StationRequest request = 잠실_TO_건대_REQUEST;
        StationSaveResponse response = SAVE_INITIAL_STATIONS_잠실_TO_건대_RESPONSE;
        when(stationService.saveStation(request))
                .thenReturn(response);

        // when, then
        mockMvc.perform(post("/stations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/lines/" + response.getLineId()));
    }

    @Test
    @DisplayName("DELETE /stations/{id} uri로 요청하면 반환이 noContent이다.")
    void deleteStationTest() throws Exception {
        // given
        Long stationIdToDelete = 1L;
        doNothing().when(stationService).deleteStationById(stationIdToDelete);

        // when, then
        mockMvc.perform(delete("/stations/" + stationIdToDelete))
                .andExpect(status().isNoContent());
    }
}