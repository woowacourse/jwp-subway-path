package subway.controller;

import static org.hamcrest.Matchers.endsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static subway.integration.common.JsonMapper.toJson;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import subway.dto.request.LineCreateRequest;
import subway.dto.response.LineQueryResponse;
import subway.dto.response.SectionQueryResponse;
import subway.service.LineQueryService;
import subway.service.LineService;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("LineController 은(는)")
@WebMvcTest(LineController.class)
class LineControllerTest {

    private static final String API_URL = "/lines";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LineQueryService lineQueryService;

    @MockBean
    private LineService lineService;

    @Nested
    class 노선을_생성할_떄 {

        @Test
        void 성공한다() throws Exception {
            // given
            final LineCreateRequest request = new LineCreateRequest("1호선", "잠실역", "사당역", 10, 0);
            given(lineService.create(any()))
                    .willReturn(1L);

            // when & then
            노선을_생성한다(request)
                    .andExpect(status().isCreated())
                    .andExpect(header().string("location", endsWith("1")));
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 노선이_널이거나_공백이면_예외(final String nullAndEmpty) throws Exception {
            // given
            final LineCreateRequest request = new LineCreateRequest(nullAndEmpty, "잠실역", "사당역", 10, 0);

            // when & then
            노선을_생성한다(request)
                    .andExpect(status().isUnprocessableEntity());
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 상행역이_널이거나_공백이면_예외(final String nullAndEmpty) throws Exception {
            // given
            final LineCreateRequest request = new LineCreateRequest("1호선", nullAndEmpty, "사당역", 10, 0);

            // when & then
            노선을_생성한다(request)
                    .andExpect(status().isUnprocessableEntity());
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 하행역이_널이거나_공백이면_예외(final String nullAndEmpty) throws Exception {
            // given
            final LineCreateRequest request = new LineCreateRequest("1호선", "잠실역", nullAndEmpty, 10, 0);

            // when & then
            노선을_생성한다(request)
                    .andExpect(status().isUnprocessableEntity());
        }

        @Test
        void 거리가_널이면_예외() throws Exception {
            // given
            final LineCreateRequest request = new LineCreateRequest("1호선", "잠실역", "사당역", null, 0);

            // when & then
            노선을_생성한다(request)
                    .andExpect(status().isUnprocessableEntity());
        }

        @Test
        void 추가요금이_널이면_예외() throws Exception {
            // given
            final LineCreateRequest request = new LineCreateRequest("1호선", "잠실역", "사당역", 10, null);

            // when & then
            노선을_생성한다(request)
                    .andExpect(status().isUnprocessableEntity());
        }

        private ResultActions 노선을_생성한다(final LineCreateRequest request) throws Exception {
            return mockMvc.perform(post(API_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(request)))
                    .andDo(print());
        }
    }

    @Test
    void 노선을_조회한다() throws Exception {
        // given
        final List<SectionQueryResponse> sectionQueryResponses = List.of(
                new SectionQueryResponse("잠실새내역", "잠실역", 10),
                new SectionQueryResponse("잠실역", "잠실나루역", 15)
        );
        final LineQueryResponse response = new LineQueryResponse("1호선", sectionQueryResponses);
        given(lineQueryService.findById(1L))
                .willReturn(response);

        // when & then
        mockMvc.perform(get(API_URL + "/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lineName").value("1호선"))
                .andExpect(jsonPath("$.stationQueryResponseList[0].upStationName").value("잠실새내역"))
                .andExpect(jsonPath("$.stationQueryResponseList[0].downStationName").value("잠실역"))
                .andExpect(jsonPath("$.stationQueryResponseList[1].upStationName").value("잠실역"))
                .andExpect(jsonPath("$.stationQueryResponseList[1].downStationName").value("잠실나루역"))
                .andExpect(jsonPath("$.stationQueryResponseList[1].distance").value(15))
                .andExpect(jsonPath("$.stationQueryResponseList[0].distance").value(10))
                .andDo(print());
    }

    @Test
    void 모든_노선을_조회한다() throws Exception {
        // given
        final List<SectionQueryResponse> sectionQueryResponses1 = List.of(
                new SectionQueryResponse("잠실새내역", "잠실역", 10),
                new SectionQueryResponse("잠실역", "잠실나루역", 15)
        );

        final List<SectionQueryResponse> sectionQueryResponses2 = List.of(
                new SectionQueryResponse("경북대역", "부산대역", 10),
                new SectionQueryResponse("부산대역", "전남대역", 15)
        );

        final List<LineQueryResponse> responses = List.of(
                new LineQueryResponse("1호선", sectionQueryResponses1),
                new LineQueryResponse("2호선", sectionQueryResponses2));
        given(lineQueryService.findAll())
                .willReturn(responses);

        // when & then
        mockMvc.perform(get(API_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].lineName").value("1호선"))
                .andExpect(jsonPath("$.[0].stationQueryResponseList[0].upStationName").value("잠실새내역"))
                .andExpect(jsonPath("$.[0].stationQueryResponseList[0].downStationName").value("잠실역"))
                .andExpect(jsonPath("$.[0].stationQueryResponseList[0].distance").value(10))
                .andExpect(jsonPath("$.[0].stationQueryResponseList[1].upStationName").value("잠실역"))
                .andExpect(jsonPath("$.[0].stationQueryResponseList[1].downStationName").value("잠실나루역"))
                .andExpect(jsonPath("$.[0].stationQueryResponseList[1].distance").value(15))
                .andExpect(jsonPath("$.[1].lineName").value("2호선"))
                .andExpect(jsonPath("$.[1].stationQueryResponseList[0].upStationName").value("경북대역"))
                .andExpect(jsonPath("$.[1].stationQueryResponseList[0].downStationName").value("부산대역"))
                .andExpect(jsonPath("$.[1].stationQueryResponseList[0].distance").value(10))
                .andExpect(jsonPath("$.[1].stationQueryResponseList[1].upStationName").value("부산대역"))
                .andExpect(jsonPath("$.[1].stationQueryResponseList[1].downStationName").value("전남대역"))
                .andExpect(jsonPath("$.[1].stationQueryResponseList[1].distance").value(15))
                .andDo(print());
    }
}
