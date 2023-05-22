package subway.controller;

import static fixtures.StationFixtures.GANGNAM;
import static fixtures.StationFixtures.YANGJAE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.Charset;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import subway.controller.dto.request.PassengerRequest;
import subway.controller.dto.response.ShortestPathResponse;
import subway.domain.section.PathSection;
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
            final PassengerRequest request = new PassengerRequest(10, 1L, 2L);
            final ShortestPathResponse response = ShortestPathResponse.of(
                    List.of(
                            new PathSection(1L, GANGNAM, YANGJAE, 10, 1000)
                    ),
                    10,
                    100
            );

            given(subwayService.findShortestPath(any(PassengerRequest.class))).willReturn(response);

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
            final PassengerRequest request = new PassengerRequest(10, null, 2L);

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
            final PassengerRequest request = new PassengerRequest(10, 1L, null);

            mockMvc.perform(get("/subways/shortest-path")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("도착역 ID는 존재해야 합니다."));
        }

        @Test
        @DisplayName("탑승자 나이가 존재하지 않으면 400 상태를 반환한다.")
        void findShortestPathWithEmptyAge() throws Exception {
            final PassengerRequest request = new PassengerRequest(null, 1L, 2L);

            mockMvc.perform(get("/subways/shortest-path")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("탑승자 나이는 입력해야 합니다."));
        }

        @ParameterizedTest
        @ValueSource(ints = {Integer.MIN_VALUE, -1, 0})
        @DisplayName("탑승자 나이가 0보다 작거나 같으면 400 상태를 반환한다.")
        void findShortestPathWithInvalidAge(final int age) throws Exception {
            final PassengerRequest request = new PassengerRequest(age, 1L, 2L);

            mockMvc.perform(get("/subways/shortest-path")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("탑승자 나이는 0보다 커야합니다."));
        }
    }
}
