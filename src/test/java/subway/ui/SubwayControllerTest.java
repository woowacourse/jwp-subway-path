package subway.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.SubwayService;
import subway.dto.StationEnrollRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(SubwayController.class)
class SubwayControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SubwayService subwayService;

    @Test
    @DisplayName("/subway/{lineId}로 POST 요청을 보낼 수 있다")
    void enrollStation() throws Exception {
        //given
        Integer lineId = 1;
        Long from = 1L;
        Long to = 2L;
        Integer distance = 1;

        String body = objectMapper.writeValueAsString(new StationEnrollRequest(from, to, distance));

        //when
        mockMvc.perform(post("/subway/{lineId}", lineId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))

                //then
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("/subway/{lineId}로 DELETE 요청을 보낼 수 있다")
    void deleteStation() throws Exception {
        //given
        Integer lineId = 1;
        Integer stationId = 2;

        //when
        mockMvc.perform(delete("/subway/{lineId}/{stationId}", lineId, stationId))

                //then
                .andExpect(status().isNoContent())
                .andExpect(header().string("Location", "/line/" + lineId));
    }
}
