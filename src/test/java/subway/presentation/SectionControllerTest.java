package subway.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.business.SectionService;
import subway.presentation.dto.request.SectionRequest;
import subway.presentation.dto.response.LineResponse;
import subway.presentation.dto.response.SectionResponse;
import subway.presentation.dto.response.StationResponse;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SectionController.class)
public class SectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SectionService sectionService;

    @Nested
    @DisplayName("노선에 역 추가 - POST /sections")
    class Create {

        @Test
        @DisplayName("성공 - 상행 끝점에 추가")
        void success_upper_end_point() throws Exception {
            // given
            final SectionRequest requestDto = new SectionRequest("2호선", "UP", "잠실", "송파", 5);
            final String requestBody = objectMapper.writeValueAsString(requestDto);
            final List<SectionResponse> sectionResponses = List.of(
                    new SectionResponse(5L,
                            new LineResponse(1L, "2호선", "bg-green-600"),
                            5,
                            new StationResponse(1L, "잠실"),
                            new StationResponse(5L, "송파"))
            );
            given(sectionService.save(any())).willReturn(sectionResponses);

            // when
            final String responseBody =
                    "[{\"id\":5,\"" +
                            "line\":{" +
                            "\"id\":1,\"name\":\"2호선\",\"color\":\"bg-green-600\"" +
                            "},\"distance\":5," +
                            "\"previousStation\":{" +
                            "\"id\":1,\"name\":\"잠실\"}," +
                            "\"nextStation\":{\"id\":5,\"name\":\"송파\"}" +
                            "}]";
            mockMvc.perform(post("/sections")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(header().string("Location", "/sections"))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(responseBody));
        }

        @Test
        @DisplayName("성공 - 하행 끝점에 추가")
        void success_down_end_point() throws Exception {
            // given
            final SectionRequest requestDto = new SectionRequest("2호선", "DOWN", "종합운동장", "송파", 5);
            final String requestBody = objectMapper.writeValueAsString(requestDto);
            final List<SectionResponse> sectionResponses = List.of(
                    new SectionResponse(5L,
                            new LineResponse(1L, "2호선", "bg-green-600"),
                            5,
                            new StationResponse(5L, "송파"),
                            new StationResponse(3L, "종합운동장"))
            );
            given(sectionService.save(any())).willReturn(sectionResponses);

            // when
            final String responseBody =
                    "[{\"id\":5,\"" +
                            "line\":{" +
                            "\"id\":1,\"name\":\"2호선\",\"color\":\"bg-green-600\"" +
                            "},\"distance\":5," +
                            "\"nextStation\":{" +
                            "\"id\":3,\"name\":\"종합운동장\"}," +
                            "\"previousStation\":{\"id\":5,\"name\":\"송파\"}" +
                            "}]";
            mockMvc.perform(post("/sections")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(header().string("Location", "/sections"))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(responseBody));
        }
    }

}
