package subway.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.application.SectionService;
import subway.dto.request.SectionCreateRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SectionControllerTest {

    @LocalServerPort
    private int port;

    @MockBean
    private SectionService sectionService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void create() {
        //given
        when(sectionService.create(any(), any())).thenReturn(1L);

        //when
        //then
        RestAssured.given().log().all()
            .body(new SectionCreateRequest("기준역", "추가역", "상행", 1))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/1/sections")
            .then().log().all()
            .header("Location", "/lines/1/sections/1");
    }

    @Test
    void deleteContains() {
        //given
        //when
        //then
        RestAssured.given().log().all()
            .when()
            .delete("/lines/1/sections?stationName=역")
            .then().log().all()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
