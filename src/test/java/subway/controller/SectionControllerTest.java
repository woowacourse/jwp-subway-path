package subway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import subway.dto.request.SectionCreateRequest;
import subway.dto.request.SectionDeleteRequest;
import subway.exception.DistanceLessThatOneException;
import subway.exception.SameSectionException;
import subway.exception.InvalidSectionDistanceException;
import subway.exception.StationNotConnectedException;
import subway.exception.SectionNotFoundException;
import subway.exception.UpStationNotFoundException;
import subway.service.SectionService;
import subway.service.SubwayService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SectionController.class)
public class SectionControllerTest {

	@MockBean
	private SectionService sectionService;

	@MockBean
	private SubwayService subwayService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("구간 생성 테스트")
	void createSection() throws Exception {
		// given
		SectionCreateRequest sectionCreateRequest = new SectionCreateRequest("2호선", "잠실역", "잠실새내역", 3);

		// when & then
		mockMvc.perform(
				post("/sections")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(sectionCreateRequest)))
			.andExpect(status().isCreated());

		verify(sectionService).insertSection(any(SectionCreateRequest.class));
	}

	@Test
	@DisplayName("존재하지 않는 노선에 대한 조회 시도 시 예외가 발생한다")
	void exception_whenSectionNotFound() throws Exception {
		// given
		SectionCreateRequest sectionCreateRequest = new SectionCreateRequest("2호선", "잠실역", "잠실새내역", 3);
		doAnswer(invocation -> {
			throw new SectionNotFoundException();
		}).when(sectionService).insertSection(any(SectionCreateRequest.class));

		// when & then
		mockMvc.perform(
				post("/sections")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(sectionCreateRequest)))
			.andExpect(status().isNotFound());

		verify(sectionService).insertSection(any(SectionCreateRequest.class));
	}

	@Test
	@DisplayName("상행 종점을 찾을 수 없으면 예외가 발생한다")
	void exception_whenUpStationNotFound() throws Exception {
		// given
		SectionCreateRequest sectionCreateRequest = new SectionCreateRequest("2호선", "잠실역", "잠실새내역", 3);
		doAnswer(invocation -> {
			throw new UpStationNotFoundException();
		}).when(sectionService).insertSection(any(SectionCreateRequest.class));

		// when & then
		mockMvc.perform(
				post("/sections")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(sectionCreateRequest)))
			.andExpect(status().isNotFound());

		verify(sectionService).insertSection(any(SectionCreateRequest.class));
	}

	@Test
	@DisplayName("역 사이의 거리가 1 미만이면 예외가 발생한다")
	void exception_whenDistanceLessThanOne() throws Exception {
		// given
		SectionCreateRequest sectionCreateRequest = new SectionCreateRequest("2호선", "잠실역", "잠실새내역", 3);
		doAnswer(invocation -> {
			throw new DistanceLessThatOneException();
		}).when(sectionService).insertSection(any(SectionCreateRequest.class));

		// when & then
		mockMvc.perform(
				post("/sections")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(sectionCreateRequest)))
			.andExpect(status().isBadRequest());

		verify(sectionService).insertSection(any(SectionCreateRequest.class));
	}

	@Test
	@DisplayName("요청 구간에 겹치는 역이 없으면 예외가 발생한다")
	void exception_whenSectionNotConnected() throws Exception {
		// given
		SectionCreateRequest sectionCreateRequest = new SectionCreateRequest("2호선", "잠실역", "잠실새내역", 3);
		doAnswer(invocation -> {
			throw new StationNotConnectedException();
		}).when(sectionService).insertSection(any(SectionCreateRequest.class));

		// when & then
		mockMvc.perform(
				post("/sections")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(sectionCreateRequest)))
			.andExpect(status().isBadRequest());

		verify(sectionService).insertSection(any(SectionCreateRequest.class));
	}

	@Test
	@DisplayName("구간이 중복되면 예외가 발생한다")
	void exception_whenSectionDuplicate() throws Exception {
		// given
		SectionCreateRequest sectionCreateRequest = new SectionCreateRequest("2호선", "잠실역", "잠실새내역", 3);
		doAnswer(invocation -> {
			throw new SameSectionException();
		}).when(sectionService).insertSection(any(SectionCreateRequest.class));

		// when & then
		mockMvc.perform(
				post("/sections")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(sectionCreateRequest)))
			.andExpect(status().isBadRequest());

		verify(sectionService).insertSection(any(SectionCreateRequest.class));
	}

	@Test
	@DisplayName("요청 구간이 기존 구간 거리보다 길면 예외가 발생한다")
	void exception_whenInvalidDistance() throws Exception {
		// given
		SectionCreateRequest sectionCreateRequest = new SectionCreateRequest("2호선", "잠실역", "잠실새내역", 3);
		doAnswer(invocation -> {
			throw new InvalidSectionDistanceException();
		}).when(sectionService).insertSection(any(SectionCreateRequest.class));

		// when & then
		mockMvc.perform(
				post("/sections")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(sectionCreateRequest)))
			.andExpect(status().isBadRequest());

		verify(sectionService).insertSection(any(SectionCreateRequest.class));
	}

	@Test
	@DisplayName("구간 삭제 테스트")
	void deleteSection() throws Exception {
		// given
		SectionDeleteRequest sectionDeleteRequest = new SectionDeleteRequest("2호선", "잠실역");

		// when
		mockMvc.perform(
				delete("/sections")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(sectionDeleteRequest)))
			.andExpect(status().isNoContent());

		// then
		verify(sectionService).deleteSection(any(SectionDeleteRequest.class));
	}
}
