package subway.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.PathService;
import subway.dto.request.PathRequest;
import subway.dto.response.PathResponse;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PathController.class)
class PathControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PathService pathService;

    @DisplayName("최단 경로에 대한 역 목록과 가격을 반환해준다")
    @Test
    void findShortestPath() throws Exception {
        PathRequest pathRequest = new PathRequest(1L, 4L);
        PathResponse pathResponse = new PathResponse(
                List.of("서울대입구", "낙성대", "봉천", "사당"),
                14,
                1_500
        );
        given(pathService.findShortestPath(any(PathRequest.class)))
                .willReturn(pathResponse);

        mockMvc.perform(get("/path")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(pathRequest)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.stations[0]").value("서울대입구"))
               .andExpect(jsonPath("$.stations[1]").value("낙성대"))
               .andExpect(jsonPath("$.stations[2]").value("봉천"))
               .andExpect(jsonPath("$.stations[3]").value("사당"))
               .andExpect(jsonPath("$.distance").value(14))
               .andExpect(jsonPath("$.fare").value(1_500))
               .andDo(print());
    }
}
