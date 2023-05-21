package subway.adapter.in.web.route;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import subway.adapter.in.web.exception.ErrorResponse;
import subway.adapter.in.web.route.dto.FindRouteRequest;
import subway.application.port.in.route.FindRouteUseCase;
import subway.application.port.in.route.dto.command.FindRouteCommand;
import subway.application.port.in.route.dto.response.RouteQueryResponse;
import subway.fixture.StationFixture.삼성역;
import subway.fixture.StationFixture.역삼역;
import subway.fixture.StationFixture.잠실역;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@WebMvcTest(FindRouteController.class)
class FindRouteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FindRouteUseCase findRouteUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 최단_경로를_찾는다() throws Exception {
        // given
        RouteQueryResponse response = new RouteQueryResponse(List.of(역삼역.RESPONSE, 삼성역.RESPONSE, 잠실역.RESPONSE), 10,
                1000);
        // TODO(질문): eq(command) 쓰고싶은데.. 그러면 FindRouteCommand에 equals&hasCode 재정의 해야한다. DTO에 재정의해도 되나?
        given(findRouteUseCase.findRoute(any(FindRouteCommand.class)))
                .willReturn(response);

        // when
        MvcResult result = mockMvc.perform(get("/route")
                        .param("sourceStationId", Long.toString(1L))
                        .param("targetStationId", Long.toString(2L))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        RouteQueryResponse actualResponse = objectMapper.readValue(content, RouteQueryResponse.class);

        // then
        assertThat(actualResponse)
                .usingRecursiveComparison()
                .isEqualTo(response);
    }

    @Test
    void 출발역_id가_없을때_예외() throws Exception {
        // given
        FindRouteRequest request = new FindRouteRequest(null, 2L);

        // when
        MvcResult result = mockMvc.perform(get("/route")
                        .param("targetStationId", Long.toString(2L))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        ErrorResponse actualResponse = objectMapper.readValue(content, ErrorResponse.class);

        // then
        assertThat(actualResponse.getMessage())
                .contains("요청 값이 잘못되었습니다.");
    }

    @Test
    void 요청이_없을때_예외() throws Exception {
        // when
        MvcResult result = mockMvc.perform(get("/route"))
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        ErrorResponse actualResponse = objectMapper.readValue(content, ErrorResponse.class);

        // then
        assertThat(actualResponse.getMessage())
                .contains("요청 값이 잘못되었습니다.");
    }
}
