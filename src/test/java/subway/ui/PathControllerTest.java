package subway.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import subway.application.service.PathService;
import subway.dto.request.AddPathRequest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PathController.class)
class PathControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PathService pathService;

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
