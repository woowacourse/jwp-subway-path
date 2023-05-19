package subway.ui;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static subway.helper.RestDocsHelper.constraint;
import static subway.helper.RestDocsHelper.prettyDocument;
import static subway.helper.SubwayPathFixture.stationResponsesFixture;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.station.StationService;
import subway.dto.station.StationCreateRequest;
import subway.dto.station.StationUpdateRequest;

@WebMvcTest(StationController.class)
@AutoConfigureRestDocs
@SuppressWarnings("NonAsciiCharacters")
class StationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    StationService stationService;

    @Test
    @DisplayName("/stations로 POST 요청과 함께 station의 정보를 보내면, HTTP 201 코드와 응답이 반환되어야 한다.")
    void createStation_success() throws Exception {
        // given
        StationCreateRequest request = new StationCreateRequest("서울역");
        doNothing()
                .when(stationService)
                .saveStation(any(StationCreateRequest.class));
        String encodedLocation = URLEncoder.encode("서울역", StandardCharsets.UTF_8);

        // expect
        mockMvc.perform(post("/stations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "/stations/" + encodedLocation))
                .andDo(print())
                .andDo(prettyDocument("stations/create",
                        requestFields(
                                fieldWithPath("stationName").description("역 이름")
                                        .attributes(constraint("역으로 끝나고, 10글자 이내"))
                        )));
    }

    @Test
    @DisplayName("역을 생성할 때 StaionName이 10글자를 넘으면 HTTP 400 코드와 응답이 반환되어야 한다.")
    void createStation_stationNameOverThan10Characters() throws Exception {
        // given
        StationCreateRequest request = new StationCreateRequest("1234567890역");

        // expect
        mockMvc.perform(post("/stations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validation.stationName").value("역의 이름은 2글자 이상, 10글자 미만이어야 합니다."));
    }

    @Test
    @DisplayName("역을 생성할 때 StationName이 역으로 끝나지 않으면 HTTP 400 코드와 응답이 반환되어야 한다.")
    void createStation_stationNameNotEnd_역() throws Exception {
        // given
        StationCreateRequest request = new StationCreateRequest("서울약");

        // expect
        mockMvc.perform(post("/stations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validation.stationName").value("역의 이름은 '역'으로 끝나야 합니다."));
    }

    @Test
    @DisplayName("/stations로 GET 요청을 보내면, HTTP 200 코드와 응답이 반환되어야 한다.")
    void findAllStations_success() throws Exception {
        // given
        given(stationService.findAllStationResponses())
                .willReturn(stationResponsesFixture());

        // expect
        mockMvc.perform(get("/stations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.size()").value(3))
                .andDo(prettyDocument("stations/inquiry"));
    }

    @Test
    @DisplayName("/stations/:stationName으로 PUT 요청과 station의 정보를 보내면, HTTP 200 코드와 응답이 반환되어야 한다.")
    void updateStation_success() throws Exception {
        // given
        StationUpdateRequest request = new StationUpdateRequest("용산역");

        // expect
        mockMvc.perform(put("/stations/{stationName}", "서울역")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(prettyDocument("stations/update",
                        pathParameters(
                                parameterWithName("stationName").description("역 이름")
                        ),
                        requestFields(
                                fieldWithPath("stationName").description("역 이름")
                                        .attributes(constraint("역으로 끝나고, 10글자 이내"))
                        )));
    }

    @Test
    @DisplayName("역을 수정할 때 StaionName이 10글자를 넘으면 HTTP 400 코드와 응답이 반환되어야 한다.")
    void updateStation_stationNameOverThan10Characters() throws Exception {
        // given
        StationUpdateRequest request = new StationUpdateRequest("1234567890역");

        // expect
        mockMvc.perform(put("/stations/{stationName}", "서울역")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validation.stationName").value("역의 이름은 2글자 이상, 10글자 미만이어야 합니다."));
    }

    @Test
    @DisplayName("역을 수정할 때 StationName이 역으로 끝나지 않으면 HTTP 400 코드와 응답이 반환되어야 한다.")
    void updateStation_stationNameNotEnd_역() throws Exception {
        // given
        StationUpdateRequest request = new StationUpdateRequest("서울약");

        // expect
        mockMvc.perform(put("/stations/{stationName}", "서울역")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validation.stationName").value("역의 이름은 '역'으로 끝나야 합니다."));
    }

    @Test
    @DisplayName("/stations/:stationName로 DELETE 요청을 보내면, HTTP 200 코드와 응답이 반환되어야 한다.")
    void deleteStation_success() throws Exception {
        // expect
        mockMvc.perform(delete("/stations/{stationName}", "서울역")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(prettyDocument("stations/delete",
                        pathParameters(
                                parameterWithName("stationName").description("역 이름")
                        )));
    }
}
