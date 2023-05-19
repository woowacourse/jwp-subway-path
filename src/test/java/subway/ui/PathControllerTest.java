package subway.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.PathService;
import subway.dto.PathResponse;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PathController.class)
class PathControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PathService pathService;

    @Test
    @DisplayName("GET /path?departure={departureId}&arrival={arrivalId}")
    void getShortestPath() throws Exception {
        // given
        final PathResponse pathResponse = new PathResponse(List.of("A", "B", "C"), 20, 1450);
        given(pathService.findShortestPath(anyLong(), anyLong())).willReturn(pathResponse);

        // when & then
        mockMvc.perform(get("/path?departure=1&arrival=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pathStations[0]", is("A")))
                .andExpect(jsonPath("$.pathStations[1]", is("B")))
                .andExpect(jsonPath("$.pathStations[2]", is("C")))
                .andExpect(jsonPath("$.distance", is(20)))
                .andExpect(jsonPath("$.fee", is(1450)));

        verify(pathService, times(1)).findShortestPath(anyLong(), anyLong());
    }
}
