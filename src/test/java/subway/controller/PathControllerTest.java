package subway.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static subway.integration.common.JsonMapper.toJson;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import subway.dto.request.ShortestPathRequest;
import subway.dto.response.SectionQueryResponse;
import subway.dto.response.ShortestPathResponse;
import subway.service.PathService;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("PathController 은(는)")
@WebMvcTest(PathController.class)
class PathControllerTest {

    private static final String API_URL = "/path-shorted";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PathService pathService;

    @Test
    void 정상적으로_최단_경로를_조회한다() throws Exception {
        // given
        final ShortestPathResponse responseDto = new ShortestPathResponse(
                List.of(
                        new SectionQueryResponse("종합운동장역", "잠실새내역", 5),
                        new SectionQueryResponse("잠실새내역", "잠실역", 6)
                ),
                10,
                1350
        );
        final ShortestPathRequest request = new ShortestPathRequest("역삼역", "잠실역", 20);
        given(pathService.findShortestPath(any()))
                .willReturn(responseDto);

        // when & then
        mockMvc.perform(get(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sectionQueryResponses[0].upStationName").value("종합운동장역"))
                .andExpect(jsonPath("$.sectionQueryResponses[0].downStationName").value("잠실새내역"))
                .andExpect(jsonPath("$.sectionQueryResponses[0].distance").value(5))
                .andExpect(jsonPath("$.sectionQueryResponses[1].upStationName").value("잠실새내역"))
                .andExpect(jsonPath("$.sectionQueryResponses[1].downStationName").value("잠실역"))
                .andExpect(jsonPath("$.sectionQueryResponses[1].distance").value(6))
                .andExpect(jsonPath("$.sectionQueryResponses.size()").value(2))
                .andExpect(jsonPath("$.totalDistance").value(10))
                .andExpect(jsonPath("$.fee").value(1350))
                .andDo(print());
    }


    @ParameterizedTest
    @NullAndEmptySource
    void 시작역이_null이거나_공백이면_예외(final String nullAndEmpty) throws Exception {
        // given
        final ShortestPathRequest request = new ShortestPathRequest(nullAndEmpty, "사당역", 20);

        // when
        final MockHttpServletResponse response = 최단_거리_조회_요청(request);

        // then
        assertThat(response.getStatus()).isEqualTo(UNPROCESSABLE_ENTITY.value());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 도착이_null이거나_공백이면_예외(final String nullAndEmpty) throws Exception {
        // given
        final ShortestPathRequest request = new ShortestPathRequest("사당역", nullAndEmpty, 20);

        // when
        final MockHttpServletResponse response = 최단_거리_조회_요청(request);

        // then
        assertThat(response.getStatus()).isEqualTo(UNPROCESSABLE_ENTITY.value());
    }

    @Test
    void 나이가_null이면_예외() throws Exception {
        // given
        final ShortestPathRequest request = new ShortestPathRequest("사당역", "잠실역", null);

        // when
        final MockHttpServletResponse response = 최단_거리_조회_요청(request);

        // then
        assertThat(response.getStatus()).isEqualTo(UNPROCESSABLE_ENTITY.value());
    }

    private MockHttpServletResponse 최단_거리_조회_요청(final ShortestPathRequest request) throws Exception {
        return mockMvc.perform(get(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andDo(print())
                .andReturn()
                .getResponse();
    }
}
