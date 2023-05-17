package subway.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.service.LineService;
import subway.presentation.controller.LineController;
import subway.presentation.dto.StationEnrollRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(LineController.class)
class LineControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LineService lineService;

    @Test
    @DisplayName("/subway/{lineId}로 POST 요청을 보낼 수 있다")
    void enrollStation() throws Exception {
        // given
        StationEnrollRequest stationEnrollRequest = new StationEnrollRequest(1L, 2L, 1);
        String body = objectMapper.writeValueAsString(stationEnrollRequest);
        doNothing().when(lineService).enrollStation(any(), any());

        // when
        mockMvc.perform(post("/subway/{lineId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))

                // then
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("/subway/{lineId}로 DELETE 요청을 보낼 수 있다")
    void deleteStation() throws Exception {
        // given
        doNothing().when(lineService).deleteStation(any(), any());
        Integer lineId = 1;

        // when
        mockMvc.perform(delete("/subway/{lineId}/{stationId}", lineId, 2))

                // then
                .andExpect(status().isNoContent())
                .andExpect(header().string("Location", "/line/" + lineId));
    }
}
