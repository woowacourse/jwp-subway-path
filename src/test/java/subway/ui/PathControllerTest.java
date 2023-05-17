package subway.ui;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static subway.helper.RestDocsHelper.constraint;
import static subway.helper.RestDocsHelper.prettyDocument;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.PathService;
import subway.dto.path.PathRequest;
import subway.dto.path.PathResponse;

@WebMvcTest(PathController.class)
@AutoConfigureRestDocs
class PathControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    PathService pathService;

    @Test
    @DisplayName("/path로 POST 요청과 함께 path의 정보를 보내면, HTTP 200 코드와 응답이 반환되어야 한다.")
    void findPath_success() throws Exception {
        // given
        PathRequest request = new PathRequest("서울역", "용산역");
        given(pathService.findPath(any(PathRequest.class)))
                .willReturn(new PathResponse(
                        List.of(
                                "서울역",
                                "잠실역",
                                "이수역",
                                "용산역"
                        ),
                        28,
                        2300
                ));

        // expect
        mockMvc.perform(post("/path")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(prettyDocument("path/find",
                        requestFields(
                                fieldWithPath("originStationName").description("출발역")
                                        .attributes(constraint("10글자 이내")),
                                fieldWithPath("destinationStationName").description("도착역")
                                        .attributes(constraint("10글자 이내"))
                        ),
                        relaxedResponseFields(
                                fieldWithPath("result.stations").description("역 목록"),
                                fieldWithPath("result.totalDistance").description("총 거리"),
                                fieldWithPath("result.totalPrice").description("요금")
                        )
                ));
    }
}
