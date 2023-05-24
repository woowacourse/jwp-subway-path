package subway.presentation;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import subway.application.StationService;
import subway.application.dto.CreationStationDto;
import subway.application.dto.ReadStationDto;
import subway.domain.station.Station;
import subway.exception.GlobalExceptionHandler;
import subway.presentation.dto.request.CreateStationRequest;

@WebMvcTest(controllers = StationController.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings({"NonAsciiCharacters", "SpellCheckingInspection"})
class StationControllerTest {

    @MockBean
    StationService stationService;

    @Autowired
    StationController stationController;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(stationController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .alwaysDo(print())
                .build();
    }

    @Test
    void createStation_메소드는_station을_저장하고_저장한_데이터를_반환한다() throws Exception {
        final Station station = Station.of(1L, "12역");
        given(stationService.saveStation(anyString())).willReturn(CreationStationDto.from(station));
        final CreateStationRequest request = CreateStationRequest.from(station.getName());

        mockMvc.perform(post("/stations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id", is(1)),
                        jsonPath("$.name", is("12역"))
                );
    }

    @Test
    void createStation_메소드는_지정한_역_이름이_이미_존재하는_경우_예외가_발생한다() throws Exception {
        given(stationService.saveStation(anyString()))
                .willThrow(new IllegalArgumentException("지정한 역의 이름은 이미 존재하는 이름입니다."));
        final CreateStationRequest request = CreateStationRequest.from("12역");

        mockMvc.perform(post("/stations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.message", is("지정한 역의 이름은 이미 존재하는 이름입니다."))
                );
    }

    @Test
    void showStation_메소드는_저장되어_있는_id를_전달하면_해당_station을_반환한다() throws Exception {
        final Station station = Station.of(1L, "12역");
        given(stationService.findStationById(anyLong())).willReturn(ReadStationDto.from(station));

        mockMvc.perform(get("/stations/{stationId}", 1L))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id", is(1)),
                        jsonPath("$.name", is("12역"))
                );
    }

    @Test
    void showStation_메소드는_없는_id를_전달하면_예외가_발생한다() throws Exception {
        given(stationService.findStationById(anyLong())).willThrow(new IllegalArgumentException("존재하지 않는 역입니다."));

        mockMvc.perform(get("/stations/{stationId}", 1L))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.message", is("존재하지 않는 역입니다."))
                );
    }

    @Test
    void deleteStation_메소드는_노선에_등록되지_않은_id를_전달하면_해당_id를_가진_station을_삭제한다() throws Exception {
        willDoNothing().given(stationService).deleteStationById(anyLong());

        mockMvc.perform(delete("/stations/{stationId}", 1L))
                .andExpectAll(
                        status().isNoContent()
                );
    }

    @Test
    void deleteStation_메소드는_노선에_등록된_id를_전달하면_예외가_발생한다() throws Exception {
        willThrow(new IllegalArgumentException("노선에 등록되어 있는 역은 삭제할 수 없습니다."))
                .given(stationService).deleteStationById(anyLong());

        mockMvc.perform(delete("/stations/{stationId}", 1L))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.message", is("노선에 등록되어 있는 역은 삭제할 수 없습니다."))
                );
    }
}
