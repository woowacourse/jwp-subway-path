package subway.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import fixtures.StationFixtures;
import java.nio.charset.Charset;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import subway.controller.dto.request.ShortestPathFindRequest;
import subway.controller.dto.response.ShortestPathResponse;
import subway.controller.dto.response.StationResponse;
import subway.service.SubwayService;

@WebMvcTest(SubwayController.class)
class SubwayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SubwayService subwayService;

    @Nested
    @DisplayName("findShortestPath 메서드는 ")
    class FindShortestPath {

        @Test
        @DisplayName("유효한 요청이라면 최단 경로 정보를 반환한다.")
        void findShortestPath() throws Exception {
            final ShortestPathFindRequest request = new ShortestPathFindRequest(1L, 2L);
            final ShortestPathResponse response = new ShortestPathResponse(
                    List.of(
                            StationResponse.from(StationFixtures.GANGNAM),
                            StationResponse.from(StationFixtures.YANGJAE)
                    ),
                    10,
                    1250
            );

            given(subwayService.findShortestPath(any(ShortestPathFindRequest.class))).willReturn(response);

            final MvcResult mvcResult = mockMvc.perform(get("/subways/shortest-path")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            final String jsonResponse = mvcResult.getResponse().getContentAsString(Charset.forName("UTF-8"));
            final ShortestPathResponse result = objectMapper.readValue(jsonResponse, ShortestPathResponse.class);
            assertThat(result).usingRecursiveComparison().isEqualTo(response);
        }

        @Test
        @DisplayName("출발역 ID가 존재하지 않으면 400 상태를 반환한다.")
        void findShortestPathWithInvalidStartStation() throws Exception {
            final ShortestPathFindRequest request = new ShortestPathFindRequest(null, 2L);

            mockMvc.perform(get("/subways/shortest-path")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("출발역 ID는 존재해야 합니다."));
        }

        @Test
        @DisplayName("도착역 ID가 존재하지 않으면 400 상태를 반환한다.")
        void findShortestPathWithInvalidEndStation() throws Exception {
            final ShortestPathFindRequest request = new ShortestPathFindRequest(1L, null);

            mockMvc.perform(get("/subways/shortest-path")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("도착역 ID는 존재해야 합니다."));
        }
    }
}
