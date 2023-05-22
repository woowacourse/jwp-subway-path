package subway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import subway.dto.request.PathRequest;
import subway.exception.LinesEmptyException;
import subway.service.SubwayService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
