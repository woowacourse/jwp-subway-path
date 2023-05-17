package subway.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.dto.request.StationRequest;
import subway.dto.response.StationResponse;
import subway.service.StationService;

@WebMvcTest(StationController.class)
@DisplayName("StationController 테스트")
class StationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StationService stationService;

    @Test
    @DisplayName("createStation 요청 메세지 검증 기능 테스트")
    void validateCreateStationDtoProperty() throws Exception {
        StationRequest request = new StationRequest(null);
        StationResponse response = new StationResponse(1L, null);
        ObjectMapper objectMapper = new ObjectMapper();

        when(stationService.saveStation(any())).thenReturn(response);

        mockMvc.perform(post("/stations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message", containsString("must not be blank")));
    }

    @Test
    @DisplayName("updateStation 요청 메세지 검증 기능 테스트")
    void validateUpdateStationDtoProperty() throws Exception {
        StationRequest request = new StationRequest(null);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(put("/stations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message", containsString("must not be blank")));
    }
}
