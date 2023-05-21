package subway.ui;

import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static subway.helper.RestDocsHelper.constraint;
import static subway.helper.RestDocsHelper.prettyDocument;
import static subway.helper.SubwayPathFixture.sectionResponsesFixture;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.section.SectionService;
import subway.dto.section.SectionCreateRequest;
import subway.dto.section.SectionDeleteRequest;

@WebMvcTest(SectionController.class)
@AutoConfigureRestDocs
class SectionControllerTest {
    private static final Long LINE_ID = 1L;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    SectionService sectionService;

    @Test
    @DisplayName("/sections/{lineId}로 POST 요청과 함께 station의 정보를 보내면, HTTP 200 코드와 응답이 반환되어야 한다.")
    void addSection_success() throws Exception {
        // given
        SectionCreateRequest request = new SectionCreateRequest("잠실역", "잠실나루역", 10);

        // expect
        mockMvc.perform(post("/sections/{lineId}", LINE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("구간이 생성되었습니다."))
                .andDo(prettyDocument("section/create",
                        pathParameters(
                                parameterWithName("lineId").description("노선의 ID")
                        ),
                        requestFields(
                                fieldWithPath("upBoundStationName").description("구간의 상행역")
                                        .attributes(constraint("10글자 이내")),
                                fieldWithPath("downBoundStationName").description("구간의 하행역")
                                        .attributes(constraint("10글자 이내")),
                                fieldWithPath("distance").description("구간의 길이")
                                        .attributes(constraint("1~100 사이의 값"))
                        )));
    }

    @Test
    @DisplayName("구간을 생성할 때 상행, 하행역 이름의 길이가 10글자를 넘으면 예외가 발생해야 한다.")
    void addSection_stationNameOverThan10Characters() throws Exception {
        // given
        SectionCreateRequest request = new SectionCreateRequest("1234567890역", "1234567890역", 10);

        // expect
        mockMvc.perform(post("/sections/{lineId}", LINE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validation.upBoundStationName").exists())
                .andExpect(jsonPath("$.validation.downBoundStationName").exists());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 101})
    @DisplayName("구간을 생성할 때 길이가 1 미만 또는 100을 초과하면 예외가 발생해야 한다.")
    void addSection_distanceLessThan1(int distance) throws Exception {
        // given
        SectionCreateRequest request = new SectionCreateRequest("잠실역", "잠실나루역", distance);

        // expect
        mockMvc.perform(post("/sections/{lineId}", LINE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validation.distance").value("구간의 길이는 1~100 사이여야 합니다."));
    }

    @Test
    @DisplayName("/sections/{lineId}로 DELETE 요청과 함께 station의 정보를 보내면, HTTP 200 코드와 응답이 반환되어야 한다.")
    void deleteSection_success() throws Exception {
        // given
        SectionDeleteRequest request = new SectionDeleteRequest("잠실역");

        // expect
        mockMvc.perform(delete("/sections/{lineId}", LINE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("구간이 삭제되었습니다."))
                .andDo(prettyDocument("section/delete",
                        pathParameters(
                                parameterWithName("lineId").description("노선의 ID")
                        ),
                        requestFields(
                                fieldWithPath("stationName").description("역의 이름")
                                        .attributes(constraint("10글자 이내"))
                        )));
    }

    @Test
    @DisplayName("/sections/{lineId}로 GET 요청을 보내면, HTTP 200 코드와 응답이 반환되어야 한다.")
    void findAllSectionsByLineId_success() throws Exception {
        // given
        given(sectionService.findSectionsByLineId(anyLong()))
                .willReturn(sectionResponsesFixture());

        // expect
        mockMvc.perform(get("/sections/{lineId}", LINE_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.size()").value(3))
                .andDo(prettyDocument("section/inquiry",
                        pathParameters(
                                parameterWithName("lineId").description("노선의 ID")
                        )));
    }
}
