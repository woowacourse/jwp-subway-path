package subway.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import subway.dto.request.PathRequest;
import subway.service.SubwayService;

@WebMvcTest(PathController.class)
public class PathControllerTest {

	@MockBean
	private SubwayService subwayService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("최단 경로 조회 테스트")
	void findShortestPath() throws Exception {
		// given
		PathRequest req = new PathRequest("잠실역", "선릉역");

		// when & then
		mockMvc.perform(get("/routes")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(req))
		).andExpect(status().isOk());

		verify(subwayService).findShortestPath(any(PathRequest.class));
	}
}
