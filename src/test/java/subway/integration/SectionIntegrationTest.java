package subway.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.ui.dto.request.SectionRequest;

@DisplayName("지하철 구간 관련 기능")
@Sql("/test-data.sql")
public class SectionIntegrationTest extends IntegrationTest{

	private SectionRequest sectionRequest1;

	@BeforeEach
	public void setUp(){
		super.setUp();
		sectionRequest1 = new SectionRequest("2호선","잠실","역삼", 5L);
	}

	@DisplayName("지하철 구간을 생성한다")
	@Test
	void createSection(){
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
