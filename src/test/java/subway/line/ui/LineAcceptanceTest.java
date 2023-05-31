package subway.line.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import subway.line.ui.dto.LineCreationRequest;
import subway.utils.BaseTest;
import subway.utils.Steps;

import static org.hamcrest.Matchers.*;
import static subway.utils.Steps.노선을_만든다;
import static subway.utils.Steps.노선을_찾는다;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/schema.sql")
public class LineAcceptanceTest extends BaseTest {

    private static final String VALID_LINE_NAME = "9호선";
    private static final String VALID_UPSTREAM_NAME = "잠실";
    private static final String VALID_DOWNSTREAM_NAME = "잠실나루";
    private static final int FIVE_DISTANCE = 5;
    private static final int VALID_ADDITIONAL_FARE = 0;

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        super.setUp(port);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void addLineSuccess() {
        final LineCreationRequest lineCreationRequest = new LineCreationRequest(VALID_LINE_NAME, "장산", "서울대입구서울15자이름입니다", 1, VALID_ADDITIONAL_FARE);

        노선을_만든다(lineCreationRequest)
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", containsString("/lines/" + 1));
    }

    @ParameterizedTest(name = "상행역 이름 길이가 맞지 않으면 역을 추가할 수 없다.")
    @ValueSource(strings = {"서", "서울대입구서울대16자이름입니다"})
    void addLineFail1(String invalidStationName) {
        final LineCreationRequest invalidLineCreationRequest = new LineCreationRequest(VALID_LINE_NAME, VALID_UPSTREAM_NAME, invalidStationName, FIVE_DISTANCE, VALID_ADDITIONAL_FARE);

        노선을_만든다(invalidLineCreationRequest)
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @ParameterizedTest(name = "하행역 이름 길이가 맞지 않으면 역을 추가할 수 없다.")
    @ValueSource(strings = {"서", "서울대입구서울대16자이름입니다"})
    void addLineFail2(String invalidStationName) {
        final LineCreationRequest invalidLineCreationRequest = new LineCreationRequest(VALID_LINE_NAME, invalidStationName, VALID_DOWNSTREAM_NAME, FIVE_DISTANCE, VALID_ADDITIONAL_FARE);

        노선을_만든다(invalidLineCreationRequest)
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @ParameterizedTest(name = "노선 이름 길이가 맞지 않으면 역을 추가할 수 없다.")
    @ValueSource(strings = {"서", "서울대입구서울대16자이름입니다"})
    void addLineFail3(String invalidLineName) {
        final LineCreationRequest invalidLineCreationRequest = new LineCreationRequest(invalidLineName, VALID_UPSTREAM_NAME, VALID_DOWNSTREAM_NAME, FIVE_DISTANCE, VALID_ADDITIONAL_FARE);

        노선을_만든다(invalidLineCreationRequest)
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("거리가 1보다 작으면 역을 추가할 수 없다.")
    void addLineFail4() {
        final LineCreationRequest invalidLineCreationRequest = new LineCreationRequest(VALID_LINE_NAME, VALID_UPSTREAM_NAME, VALID_DOWNSTREAM_NAME, 0, VALID_ADDITIONAL_FARE);

        노선을_만든다(invalidLineCreationRequest)
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("/lines post 노선 이름이 이미 존재하면 역을 추가할 수 없다.")
    void addLineFail5() {
        final LineCreationRequest lineCreationRequest = new LineCreationRequest(VALID_LINE_NAME, VALID_UPSTREAM_NAME, VALID_DOWNSTREAM_NAME, FIVE_DISTANCE, VALID_ADDITIONAL_FARE);

        노선을_만든다(lineCreationRequest);

        노선을_만든다(lineCreationRequest)
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("/lines post 노선 추가 요금이 음수이면 예외를 발생시킨다.")
    void addLineFail6() {
        final LineCreationRequest lineCreationRequest = new LineCreationRequest(VALID_LINE_NAME, VALID_UPSTREAM_NAME, VALID_DOWNSTREAM_NAME, FIVE_DISTANCE, -1);

        노선을_만든다(lineCreationRequest)
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("id로 노선을 조회한다.")
    @Test
    void getLineSuccess() {
        final LineCreationRequest lineCreationRequest = new LineCreationRequest(VALID_LINE_NAME, VALID_UPSTREAM_NAME, VALID_DOWNSTREAM_NAME, 5, VALID_ADDITIONAL_FARE);

        final String resourceLocation = 노선을_만든다(lineCreationRequest).extract()
                                                                    .header("Location");

        노선을_찾는다(resourceLocation)
                .statusCode(HttpStatus.OK.value())
                .body("lineName", equalTo(VALID_LINE_NAME))
                .body("sections.size()", is(1))
                .body("sections[0].upstreamName", equalTo(VALID_UPSTREAM_NAME))
                .body("sections[0].downstreamName", equalTo(VALID_DOWNSTREAM_NAME))
                .body("sections[0].distance", equalTo(5));
    }

    @Test
    @DisplayName("Line id가 존재하지 않는 경우 예외를 던진다.")
    void getLineFail() {
        노선을_찾는다(LINE_URL + "/0")
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("모든 노선을 조회한다.")
    void getLinesSuccess() {
        final LineCreationRequest lineCreationRequest = new LineCreationRequest(VALID_LINE_NAME, VALID_UPSTREAM_NAME, VALID_DOWNSTREAM_NAME, 5, VALID_ADDITIONAL_FARE);

        노선을_만든다(lineCreationRequest);

        Steps.모든_노선을_찾는다()
             .statusCode(HttpStatus.OK.value())
             .body("[0].lineName", equalTo(VALID_LINE_NAME))
             .body("[0].sections.size()", is(1))
             .body("[0].sections[0].upstreamName", equalTo(VALID_UPSTREAM_NAME))
             .body("[0].sections[0].downstreamName", equalTo(VALID_DOWNSTREAM_NAME))
             .body("[0].sections[0].distance", equalTo(5));
    }
}
