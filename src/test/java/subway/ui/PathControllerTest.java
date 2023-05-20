package subway.ui;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static subway.TestSource.*;

import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import subway.application.PathService;
import subway.ui.dto.PathRequest;
import subway.ui.dto.PathResponse;

@WebMvcTest(PathController.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PathControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PathService pathService;

    @Test
    void 단일_노선_path_조회_테스트() throws Exception {
        // given
        PathRequest request = new PathRequest(cheonho.getId(), jangji.getId());
        String jsonRequest = objectMapper.writeValueAsString(request);
        PathResponse response = PathResponse.from(1250, List.of(cheonho, jamsil, jangji));
        String jsonResponse = objectMapper.writeValueAsString(response);
        given(pathService.findPath(any(PathRequest.class))).willReturn(response);

        // when & then
        mockMvc.perform(post("/path")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonRequest))
            .andExpect(status().isOk())
            .andExpect(content().json(jsonResponse, true));
    }
}
