package subway.controller.line;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.controller.LineController;
import subway.dto.line.LineCreateRequest;
import subway.dto.line.LineEditRequest;
import subway.exception.ColorNotBlankException;
import subway.exception.LineNotFoundException;
import subway.exception.LineNumberUnderMinimumNumber;
import subway.exception.NameIsBlankException;
import subway.service.LineService;
import subway.service.SubwayMapService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LineController.class)
public class LineControllerUnitTest {

    @MockBean
    private LineService lineService;

    @MockBean
    private SubwayMapService subwayMapService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("노선을 생성한다.")
    void create_line_success() throws Exception {
        // given
        LineCreateRequest lineCreateRequest = new LineCreateRequest("2호선", 2, "green");

        // when & then
        mockMvc.perform(
                        post("/lines")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(lineCreateRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("노선의 색상이 없으면 예외를 발생시킨다.")
    void throws_exception_when_line_color_empty() throws Exception {
        // given
        LineCreateRequest lineCreateRequest = new LineCreateRequest("2호선", 2, "");
        doAnswer(invocation -> {
            throw new ColorNotBlankException();
        }).when(lineService).saveLine(any(LineCreateRequest.class));

        // when & then
        mockMvc.perform(
                        post("/lines")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(lineCreateRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("노선의 번호가 음수이면 예외를 발생시킨다.")
    void throws_exception_when_line_number_under_zero() throws Exception {
        // given
        LineCreateRequest lineCreateRequest = new LineCreateRequest("2호선", -1, "");
        doAnswer(invocation -> {
            throw new LineNumberUnderMinimumNumber();
        }).when(lineService).saveLine(any(LineCreateRequest.class));

        // when & then
        mockMvc.perform(
                        post("/lines")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(lineCreateRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("노선의 이름이 없으면 예외를 발생시킨다.")
    void throws_exception_when_line_name_empty() throws Exception {
        // given
        LineCreateRequest lineCreateRequest = new LineCreateRequest("", 2, "green");
        doAnswer(invocation -> {
            throw new NameIsBlankException();
        }).when(lineService).saveLine(any(LineCreateRequest.class));

        // when & then
        mockMvc.perform(
                        post("/lines")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(lineCreateRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("모든 노선을 조회한다.")
    void find_all_lines_success() throws Exception {
        // when & then
        mockMvc.perform(
                get("/lines")
        ).andExpect(status().isOk());

        verify(lineService).findAll();
    }

    @Test
    @DisplayName("노선의 구성을 조회한다.")
    void find_line_map_by_line_number_success() throws Exception {
        // given
        Long lineNumber = 1L;

        // when & then
        mockMvc.perform(
                get("/lines/" + lineNumber)
        ).andExpect(status().isOk());

        verify(subwayMapService).showLineMapByLineNumber(lineNumber);
    }

    @Test
    @DisplayName("노선을 수정한다.")
    void edit_line_success() throws Exception {
        // given
        Long id = 1L;
        LineEditRequest lineEditRequest = new LineEditRequest("2호선", 10, "blue");

        // when & then
        mockMvc.perform(
                patch("/lines/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lineEditRequest))
        ).andExpect(status().isNoContent());

        verify(lineService).editLineById(eq(id), any(LineEditRequest.class));
    }

    @Test
    @DisplayName("노선을 찾지 못하면 예외를 발생시킨다.")
    void throws_exception_when_line_not_found() throws Exception {
        // given
        Long id = 1L;
        LineEditRequest lineEditRequest = new LineEditRequest("2호선", 10, "blue");

        doAnswer(invocation -> {
            throw new LineNotFoundException();
        }).when(lineService).editLineById(eq(id), any(LineEditRequest.class));


        // when & then
        mockMvc.perform(
                patch("/lines/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lineEditRequest))
        ).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("노선을 삭제한다.")
    void delete_line_success() throws Exception {
        // given
        Long id = 1L;

        // when & then
        mockMvc.perform(
                delete("/lines/" + id)
        ).andExpect(status().isNoContent());

        verify(lineService).deleteLineById(id);
    }
}
