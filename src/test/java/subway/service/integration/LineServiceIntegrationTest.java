package subway.service.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.request.LineCreateRequest;
import subway.dto.request.LineUpdateRequest;
import subway.dto.response.LineResponse;
import subway.dto.response.LinesResponse;
import subway.service.LineService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/data.sql")
public class LineServiceIntegrationTest {

	private static final LineCreateRequest LINE_REQUEST_TWO = new LineCreateRequest("2호선");
	private static final LineCreateRequest LINE_REQUEST_SINBUNDANG = new LineCreateRequest("신분당선");
	@LocalServerPort
	private int port;

	@Autowired
	private LineService lineService;

	@Autowired
	private ApplicationEventPublisher publisher;

	@BeforeEach
	void setUp() {
		RestAssured.port = this.port;
	}

	@Test
	@DisplayName("노선 생성 서비스 인수 테스트")
	void createLine() {
		// when
		lineService.saveLine(LINE_REQUEST_TWO);

		// then
		LinesResponse lineEntities = lineService.findAll();
		assertThat(lineEntities.getLines().size()).isEqualTo(1);
	}

	@Test
	@DisplayName("전체 노선 조회 서비스 인수 테스트")
	void findAll() {
		// given
		lineService.saveLine(LINE_REQUEST_TWO);
		lineService.saveLine(LINE_REQUEST_SINBUNDANG);

		// when
		LinesResponse result = lineService.findAll();

		// then
		assertAll(
			() -> assertThat(result.getLines().size()).isEqualTo(2),
			() -> assertThat(result.getLines().get(0).getName()).isEqualTo(LINE_REQUEST_TWO.getName()),
			() -> assertThat(result.getLines().get(1).getName()).isEqualTo(LINE_REQUEST_SINBUNDANG.getName())
		);
	}

	@Test
	@DisplayName("노선 갱신 서비스 인수 테스트")
	void updateLine() {
		// given
		Long id = lineService.saveLine(LINE_REQUEST_TWO);

		LineUpdateRequest lineUpdateRequest = new LineUpdateRequest("신분당선");

		// when
		lineService.updateLineById(id, lineUpdateRequest);

		// then
		LineResponse lineEntity = lineService.findAll().getLines().get(0);
		assertThat(lineEntity.getName()).isEqualTo(lineUpdateRequest.getName());
	}

	@Test
	@DisplayName("노선 삭제 서비스 인수 테스트")
	void deleteLine() {
		// given
		Long id = lineService.saveLine(LINE_REQUEST_TWO);

		// when
		lineService.deleteLineById(id);

		// then
		LinesResponse result = lineService.findAll();
		assertThat(result.getLines().size()).isEqualTo(0);
	}
}
