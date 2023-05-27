package subway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import subway.dto.request.LineCreateRequest;
import subway.dto.request.LineUpdateRequest;
import subway.exception.LineNotFoundException;
import subway.exception.BlankNameException;
import subway.service.LineService;
import subway.service.SubwayService;

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
public class LineControllerTest {
	String lineName;
	LineCreateRequest lineCreateRequest;
	@MockBean
	private LineService lineService;

	@MockBean
	private SubwayService subwayService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp(){
		lineName = "2호선";
		lineCreateRequest = new LineCreateRequest("2호선");
	}

	@Test
	@DisplayName("노선 생성 테스트")
	void createLine() throws Exception {
		// when & then
		mockMvc.perform(
				post("/lines")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(lineCreateRequest)))
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("노선의 이름이 없으면 예외가 발생한다")
	void exception_whenLineNameEmpty() throws Exception {
		// given
		lineCreateRequest = new LineCreateRequest("");
		doAnswer(invocation -> {
			throw new BlankNameException();
		}).when(lineService).saveLine(any(LineCreateRequest.class));

		// when & then
		mockMvc.perform(
				post("/lines")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(lineCreateRequest)))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("노선 전체 조회 테스트")
	void findAll() throws Exception {
		// when & then
		mockMvc.perform(
			get("/lines")
		).andExpect(status().isOk());

		verify(lineService).findAll();
	}

	@Test
	@DisplayName("지하철 조회 테스트")
	void findSubway() throws Exception {
		// when & then
		mockMvc.perform(
			get("/lines/" + lineName)
		).andExpect(status().isOk());

		verify(subwayService).findStationsByLineName(lineName);
	}

	@Test
	@DisplayName("노선 갱신 테스트")
	void updateLine() throws Exception {
		// given
		LineUpdateRequest lineUpdateRequest = new LineUpdateRequest(lineName);

		// when & then
		mockMvc.perform(
			patch("/lines/" + lineName)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(lineUpdateRequest))
		).andExpect(status().isNoContent());

		verify(lineService).updateLineByLineName(eq(lineName), any(LineUpdateRequest.class));
	}

	@Test
	@DisplayName("존재하지 않는 노선 조회 시 예외가 발생한다")
	void exception_whenLineNotFound() throws Exception {
		// given
		LineUpdateRequest lineUpdateRequest = new LineUpdateRequest(lineName);

		doAnswer(invocation -> {
			throw new LineNotFoundException();
		}).when(lineService).updateLineByLineName(eq(lineName), any(LineUpdateRequest.class));

		// then
		mockMvc.perform(
			patch("/lines/" + lineName)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(lineUpdateRequest))
		).andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("노선 삭제 테스트")
	void deleteLine() throws Exception {
		// then
		mockMvc.perform(
			delete("/lines/" + lineName)
		).andExpect(status().isNoContent());

		verify(lineService).deleteLineByName(lineName);
	}
}
