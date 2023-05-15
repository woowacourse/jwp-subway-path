package subway.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.application.SectionCreateRequest;
import subway.ui.dto.LineRequest;

@DisplayName("지하철 구간 관련 기능")
public class SectionIntegrationTest extends IntegrationTest{

	private LineRequest lineRequest1;
	private SectionCreateRequest sectionRequest1;

	@BeforeEach
	public void setUp(){
		super.setUp();
		lineRequest1 = new LineRequest("2호선");

		sectionRequest1 = new SectionCreateRequest("2호선","잠실역","역삼역", 5L);
	}

	@DisplayName("지하철 구간을 생성한다")
	@Test
	void createSection(){
		// when
		ExtractableResponse<Response> lineResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(lineRequest1)
			.when().post("/lines")
			.then().log().all().
			extract();

		ExtractableResponse<Response> response = RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(sectionRequest1)
			.when().post("/sections")
			.then().log().all()
			.extract();

		// then
		Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}
}
