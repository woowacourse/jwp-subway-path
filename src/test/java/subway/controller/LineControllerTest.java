package subway.controller;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;


@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@SpringBootTest()
class LineControllerTest {

//    @MockBean
//    private LineService lineService;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    void saveRequest를_받아_호선을_생성한다() throws Exception {
//        // given
//        final LineRequest saveRequest = new LineRequest("2호선", "초록");
//        final String request = objectMapper.writeValueAsString(saveRequest);
//
//        // when, then
//        mockMvc.perform(post("/lines")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(request))
//                .andExpect(status().isCreated())
//                .andDo(print())
//                .andReturn();
//    }
//
//    @Test
//    void 모든_호선을_조회한다() throws Exception {
//        // given
//        final List<LineResponse> responses = List.of(
//                new LineResponse(1L, "1호선", "파랑"),
//                new LineResponse(2L, "2호선", "초록")
//        );
//        when(lineService.findLineResponses()).thenReturn(responses);
//        final String responseJson = objectMapper.writeValueAsString(responses);
//
//        // when, then
//        mockMvc.perform(get("/lines")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().json(responseJson))
//                .andDo(print());
//    }
//
//    @Test
//    void id를_받아_해당_호선을_조회한다() throws Exception {
//        // given
//        final Long id = 1L;
//        final LineResponse lineResponse = new LineResponse(id, "1호선", "파랑");
//
//        when(lineService.findLineResponseById(id)).thenReturn(lineResponse);
//        final String responseJson = objectMapper.writeValueAsString(lineResponse);
//
//        // when, then
//        mockMvc.perform(get("/lines/" + id)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().json(responseJson))
//                .andDo(print());
//    }
//
//    @Test
//    void id와_saveRequest를_받아_해당_호선을_수정한다() throws Exception {
//        // given
//        final Long id = 1L;
//        final LineRequest line = new LineRequest("2호선", "검정");
//        final String requestJson = objectMapper.writeValueAsString(line);
//        lineService.saveLine(line);
//
//        // when, then
//        mockMvc.perform(put("/lines/" + id)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestJson))
//                .andExpect(status().isOk())
//                .andDo(print());
//
//        verify(lineService, times(1)).updateLine(id, line);
//    }
//
//    @Test
//    void id를_받아_해당_호선을_삭제한다() throws Exception {
//        // given
//        final Long id = 1L;
//        final LineRequest line = new LineRequest("1호선", "파랑");
//        lineService.saveLine(line);
//
//        // when, then
//        mockMvc.perform(delete("/lines/" + id)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNoContent())
//                .andDo(print());
//
//        verify(lineService, times(1)).deleteLineById(id);
//    }
}
