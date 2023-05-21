package subway.controller.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.controller.RouteController;
import subway.dto.route.PathRequest;
import subway.exception.ColorNotBlankException;
import subway.exception.LinesEmptyException;
import subway.service.SubwayMapService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RouteController.class)
public class RouteControllerUnitTest {

    @MockBean
    private SubwayMapService subwayMapService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("최단 경로를 조회한다.")
    void find_shortest_route() throws Exception {
        // given
        PathRequest req = new PathRequest("잠실역", "종합운동장역");

        // when & then
        mockMvc.perform(get("/routes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))
        ).andExpect(status().isOk());

        verify(subwayMapService).findShortestPath(any(PathRequest.class));
    }

    @Test
    @DisplayName("같은 역을 조회시 예외를 발생시킨다.")
    void throws_exception_when_search_same_station() throws Exception {
        // given
        PathRequest req = new PathRequest("잠실역", "잠실역");
        doAnswer(invocation -> {
            throw new ColorNotBlankException();
        }).when(subwayMapService).findShortestPath(any(PathRequest.class));

        // when & then
        mockMvc.perform(get("/routes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))
        ).andExpect(status().isBadRequest());
    }

    @DisplayName("최단 경로를 찾을 때, Lines가 null이면 예외를 발생시킨다.")
    @Test
    void throws_exception_when_find_shortest_route_but_lines_empty() throws Exception {
        //given
        PathRequest req = new PathRequest("잠실역", "선릉역");
        doAnswer(invocation -> {
            throw new LinesEmptyException();
        }).when(subwayMapService).findShortestPath(any(PathRequest.class));

        // when & then
        mockMvc.perform(get("/routes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))
        ).andExpect(status().isBadRequest());
    }
}
