package subway.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import subway.dto.request.AddPathRequest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PathControllerTest extends ControllerTest {

    @DisplayName("노선의 경로에 역을 추가한다.")
    @Test
    void addPathTest() throws Exception {
        final AddPathRequest request = new AddPathRequest(1L, 2L, 10, "UP");
        final String json = objectMapper.writeValueAsString(request);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/lines/1/stations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk());
    }
}
