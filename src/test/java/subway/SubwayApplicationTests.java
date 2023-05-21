//package subway;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.restassured.RestAssured;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.jdbc.Sql;
//import subway2.dto.SectionRequest;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Sql("classpath:initializeTestDb.sql")
//class SubwayApplicationTests {
//
//	@LocalServerPort
//	private int port;
//
//	@BeforeEach
//	void setUp() {
//		RestAssured.port = this.port;
//	}
//
//	@Autowired
//	ObjectMapper objectMapper;
//
//	@DisplayName("특정 빈 노선에 역 추가하기")
//	@Test
//	void createSectionEmptyLine() throws JsonProcessingException {
//        RestAssured.given()
//				.body(objectMapper.writeValueAsString(new SectionRequest(7L, 8L, 6)))
//				.contentType(MediaType.APPLICATION_JSON_VALUE)
//				.when()
//				.post("/sections/2")
//				.then()
//				.statusCode(HttpStatus.CREATED.value())
//				.header("Location", "/lines/2");
//	}
//
//	@DisplayName("특정 비지 않은 노선에 역 추가하기")
//	@Test
//	void createSectionInLine() throws JsonProcessingException {
//		RestAssured.given()
//				.body(objectMapper.writeValueAsString(new SectionRequest(1L, 3L, 5)))
//				.contentType(MediaType.APPLICATION_JSON_VALUE)
//				.when()
//				.post("/sections/1")
//				.then()
//				.statusCode(HttpStatus.CREATED.value())
//				.header("Location", "/lines/1");
//	}
//
//}
