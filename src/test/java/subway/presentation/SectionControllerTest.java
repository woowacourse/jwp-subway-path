package subway.presentation;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import subway.application.CreateSectionService;
import subway.application.DeleteSectionService;
import subway.application.dto.CreateSectionDto;
import subway.domain.line.Line;
import subway.domain.section.Direction;
import subway.domain.section.Distance;
import subway.domain.station.Station;
import subway.exception.GlobalExceptionHandler;
import subway.presentation.dto.request.CreateSectionRequest;
import subway.presentation.dto.request.DeleteSectionRequest;

@WebMvcTest(controllers = SectionController.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings({"NonAsciiCharacters", "SpellCheckingInspection"})
class SectionControllerTest {

    @MockBean
    CreateSectionService createSectionService;

    @MockBean
    DeleteSectionService deleteSectionService;

    @Autowired
    SectionController sectionController;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(sectionController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .alwaysDo(print())
                .build();
    }

    @Test
    void addSection_메소드는_section을_저장하고_저장한_데이터를_반환한다() throws Exception {
        final Line line = Line.of(1L, "12호선", "bg-red-500");
        final Station upStation = Station.of(1L, "12역");
        final Station downStation = Station.of(2L, "23역");
        line.createSection(upStation, downStation, Distance.from(5), Direction.DOWN);
        given(createSectionService.addSection(anyLong(), anyLong(), anyLong(), any(Direction.class), anyInt()))
                .willReturn(CreateSectionDto.from(line));
        final CreateSectionRequest request = CreateSectionRequest.of(1L, 2L, 5, Direction.DOWN);

        mockMvc.perform(post("/lines/{lineId}/sections", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.stationResponses[0].id", is(1)),
                        jsonPath("$.stationResponses[0].name", is("12역")),
                        jsonPath("$.stationResponses[1].id", is(2)),
                        jsonPath("$.stationResponses[1].name", is("23역"))
                );
    }

    @Test
    void deleteSection_메소드는_지정한_역의_구역을_삭제한다() throws Exception {
        willDoNothing().given(deleteSectionService).removeSection(anyLong(), anyLong());
        final DeleteSectionRequest request = DeleteSectionRequest.of(1L);

        mockMvc.perform(delete("/lines/{lineId}/sections", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isNoContent()
                );
    }
}
