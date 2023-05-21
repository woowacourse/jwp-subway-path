package subway.controller.station;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import subway.controller.StationController;
import subway.domain.subway.Station;
import subway.dto.station.StationCreateRequest;
import subway.dto.station.StationEditRequest;
import subway.dto.station.StationResponse;
import subway.dto.station.StationsResponse;
import subway.exception.StationNotFoundException;
import subway.repository.StationRepository;
import subway.service.StationService;
import subway.service.SubwayMapService;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StationController.class)
public class StationControllerUnitTest {

    @MockBean
    private StationService stationService;

    @MockBean
    private StationRepository stationRepository;

    @MockBean
    private SubwayMapService subwayMapService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("역을 생성한다.")
    void create_station_success() throws Exception {
        // given
        StationCreateRequest stationCreateRequest = new StationCreateRequest("잠실역");

        // when
        MvcResult result = mockMvc.perform(
                        post("/stations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(stationCreateRequest))
                ).andExpect(status().isCreated())
                .andReturn();

        // then
        String locationHeader = result.getResponse().getHeader("Location");
        assertThat(Objects.requireNonNull(locationHeader).startsWith("/stations")).isTrue();
        verify(stationService).saveStation(any(StationCreateRequest.class));
    }

    @Test
    @DisplayName("역을 모두 조회한다.")
    void show_stations_success() throws Exception {
        // given
        List<StationResponse> stations = List.of(StationResponse.from(new Station("잠실역")));
        StationsResponse expected = StationsResponse.from(stations);
        given(stationService.findAllStationResponses()).willReturn(expected);

        // when & then
        mockMvc.perform(
                get("/stations")
        ).andExpect(status().isOk());

        verify(stationService).findAllStationResponses();
    }

    @Test
    @DisplayName("역을 단건 조회한다.")
    void show_station_success() throws Exception {
        // given
        Long id = 1L;
        StationResponse stationResponse = StationResponse.from(new Station("잠실역"));
        given(stationService.findStationEntityById(id)).willReturn(stationResponse);

        // when & then
        mockMvc.perform(
                get("/stations/" + id)
        ).andExpect(status().isOk());

        verify(stationService).findStationEntityById(id);
    }

    @Test
    @DisplayName("역이 존재하지 않으면 예외를 발생시킨다.")
    void throws_exception_when_station_not_found() throws Exception {
        // given
        Long id = 1L;
        given(stationService.findStationEntityById(id)).willThrow(StationNotFoundException.class);

        // when & then
        mockMvc.perform(
                get("/stations/" + id)
        ).andExpect(status().isNotFound());

        verify(stationService).findStationEntityById(id);
    }

    @Test
    @DisplayName("역을 수정한다.")
    void edit_station_success() throws Exception {
        // given
        Long id = 1L;
        StationEditRequest stationEditRequest = new StationEditRequest("선릉역");

        // when & then
        mockMvc.perform(
                patch("/stations/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stationEditRequest))
        ).andExpect(status().isNoContent());

        verify(stationService).editStation(eq(id), any(StationEditRequest.class));
    }
}
