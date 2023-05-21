package subway.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.DeleteStationService;
import subway.application.SaveSectionService;
import subway.dto.StationRequest;

import static fixtures.LineFixtures.LINE2_ID;
import static fixtures.StationFixtures.REQUEST_잠실역_TO_건대역;
import static fixtures.StationFixtures.STATION_강변역_ID;
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
    private SaveSectionService saveSectionService;
    @MockBean
    private DeleteStationService deleteStationService;

    @Test
    @DisplayName("POST /stations uri로 요청하면 201을 반환한다.")
    void createStationTest() throws Exception {
        // given
        StationRequest request = REQUEST_잠실역_TO_건대역;
        Long response = LINE2_ID;
        when(saveSectionService.saveSection(request))
                .thenReturn(response);

        // when, then
        mockMvc.perform(post("/stations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/lines/" + response));
    }

    @Test
    @DisplayName("DELETE /stations/{id} uri로 요청하면 반환이 noContent이다.")
    void deleteStationTest() throws Exception {
        // given
        Long stationIdToDelete = STATION_강변역_ID;
        Long response = LINE2_ID;
        when(deleteStationService.deleteStationById(stationIdToDelete)).thenReturn(response);

        // when, then
        mockMvc.perform(delete("/stations/" + stationIdToDelete))
                .andExpect(status().isNoContent())
                .andExpect(header().string("Location", "/lines/" + response));
    }
}