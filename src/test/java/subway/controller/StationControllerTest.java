package subway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import subway.domain.subway.Station;
import subway.dto.request.StationCreateRequest;
import subway.dto.request.StationUpdateRequest;
import subway.dto.response.StationResponse;
import subway.dto.response.StationsResponse;
import subway.exception.StationNotFoundException;
import subway.repository.StationRepository;
import subway.service.StationService;
import subway.service.SubwayService;

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
public class StationControllerTest {

	@MockBean
	private StationService stationService;

	@MockBean
	private StationRepository stationRepository;

	@MockBean
	private SubwayService subwayService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("역 생성 테스트")
	void createStation() throws Exception {
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
	@DisplayName("역 전체 조회 테스트")
	void findAll() throws Exception {
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
	@DisplayName("id를 사용한 역 조회 테스트")
	void findById() throws Exception {
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
	@DisplayName("존재하지 않는 역에 대해 조회를 시도하면 예외가 발생한다")
	void exception_whenStationNotFound() throws Exception {
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
	@DisplayName("역 갱신 테스트")
	void updateStation() throws Exception {
		// given
		Long id = 1L;
		StationUpdateRequest stationUpdateRequest = new StationUpdateRequest("교대역");

		// when & then
		mockMvc.perform(
			patch("/stations/" + id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(stationUpdateRequest))
		).andExpect(status().isNoContent());

		verify(stationService).updateStation(eq(id), any(StationUpdateRequest.class));
	}
}
