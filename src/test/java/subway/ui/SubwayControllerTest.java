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
    @DisplayName("/subway/lines/{lineId}로 POST 요청을 보낼 수 있다")
    void enrollStation() throws Exception {
        //given
        final Integer lineId = 1;
        final Long from = 1L;
        final Long to = 2L;
        final Integer distance = 1;

        final String body = objectMapper.writeValueAsString(new StationEnrollRequest(from, to, distance));

        //when
        mockMvc.perform(post("/subway/lines/{lineId}", lineId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))

                //then
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("/subway/lines/{lineId/stations/{stationId}로 DELETE 요청을 보낼 수 있다")
    void deleteStation() throws Exception {
        //given
        final Integer lineId = 1;
        final Integer stationId = 2;

        //when
        mockMvc.perform(delete("/subway/lines/{lineId}/stations/{stationId}", lineId, stationId))

                //then
                .andExpect(status().isNoContent())
                .andExpect(header().string("Location", "/line/" + lineId));
    }
}
