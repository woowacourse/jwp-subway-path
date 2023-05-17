package subway.presentation;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import subway.application.LineService;
import subway.application.dto.CreationLineDto;
import subway.application.dto.ReadLineDto;
import subway.domain.line.Line;
import subway.exception.GlobalExceptionHandler;
import subway.presentation.dto.request.CreateLineRequest;

@WebMvcTest(controllers = LineController.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings({"NonAsciiCharacters", "SpellCheckingInspection"})
class LineControllerTest {

    @MockBean
    LineService lineService;

    @Autowired
    LineController lineController;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(lineController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .alwaysDo(print())
                .build();
    }

    @Test
    void createLine_메소드는_line을_저장하고_저장한_데이터를_반환한다() throws Exception {
        final Line line = Line.of(1L, "12호선", "bg-red-500");
        given(lineService.saveLine(anyString(), anyString())).willReturn(CreationLineDto.from(line));
        final CreateLineRequest request = CreateLineRequest.of(line.getName(), line.getColor());

        mockMvc.perform(post("/lines")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id", is(1)),
                        jsonPath("$.name", is(line.getName())),
                        jsonPath("$.color", is(line.getColor()))
                );
    }

    @Test
    void createLine_메소드는_지정한_노선_이름이_이미_존재하는_경우_예외가_발생한다() throws Exception {
        given(lineService.saveLine(anyString(), anyString()))
                .willThrow(new IllegalArgumentException("지정한 노선의 이름은 이미 존재하는 이름입니다."));
        final CreateLineRequest request = CreateLineRequest.of("12호선", "bg-red-500");

        mockMvc.perform(post("/lines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.message", is("지정한 노선의 이름은 이미 존재하는 이름입니다."))
                );
    }

    @Test
    void findAllLines_메소드는_모든_line을_반환한다() throws Exception {
        final Line line = Line.of(1L, "12호선", "bg-red-500");
        given(lineService.findAllLine()).willReturn(List.of(ReadLineDto.from(line)));

        mockMvc.perform(get("/lines"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].id", is(1)),
                        jsonPath("$[0].name", is(line.getName())),
                        jsonPath("$[0].color", is(line.getColor()))
                );
    }

    @Test
    void findLineById_메소드는_저장되어_있는_id를_전달하면_해당_line을_반환한다() throws Exception {
        final Line line = Line.of(1L, "12호선", "bg-red-500");
        given(lineService.findLineById(anyLong())).willReturn(ReadLineDto.from(line));

        mockMvc.perform(get("/lines/{lineId}", line.getId()))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id", is(1)),
                        jsonPath("$.name", is(line.getName())),
                        jsonPath("$.color", is(line.getColor()))
                );
    }

    @Test
    void findLineById_메소드는_없는_id를_전달하면_예외가_발생한다() throws Exception {
        given(lineService.findLineById(anyLong())).willThrow(new IllegalArgumentException("존재하지 않는 노선입니다."));
        final CreateLineRequest request = CreateLineRequest.of("12호선", "bg-red-500");

        mockMvc.perform(get("/lines/{lineId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.message", is("존재하지 않는 노선입니다."))
                );
    }

    @Test
    void deleteLine_메소드는_id를_전달하면_해당_id를_가진_line을_삭제한다() throws Exception {
        willDoNothing().given(lineService).deleteLineById(anyLong());

        mockMvc.perform(delete("/lines/{lineId}", 1L))
                .andExpectAll(
                        status().isNoContent()
                );
    }
}
