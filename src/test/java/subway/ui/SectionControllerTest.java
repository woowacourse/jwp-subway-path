package subway.ui;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import io.restassured.RestAssured;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import subway.application.SectionService;
import subway.dto.SectionCreateRequest;

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
        final SectionCreateRequest request = new SectionCreateRequest("기준역", "추가역", "상행", 10);

        //when
        //then
        RestAssured.given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/1/sections")
            .then().log().all()
            .header("Location", "/lines/1/sections/1");
    }

    @Test
    void deleteContains() {
        //given
        when(sectionService.delete(any(), any())).thenReturn(List.of(1L, 2L));

        //when
        //then
        RestAssured.given().log().all()
            .when()
            .delete("/lines/1/sections?stationId=1")
            .then().log().all()
            .body("removedSectionsIds", hasSize(2))
            .body("removedSectionsIds", Matchers.contains(1, 2));
    }
}
