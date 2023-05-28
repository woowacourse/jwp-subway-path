package subway.integration;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.dto.DistanceResponse;
import subway.dto.FareResponse;
import subway.dto.LineAndSectionsResponse;
import subway.dto.PathRequest;
import subway.dto.PathResponse;
import subway.dto.StationAddRequest;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static subway.common.fixture.DomainFixture.디노;
import static subway.common.fixture.DomainFixture.로운;
import static subway.common.fixture.DomainFixture.조앤;
import static subway.common.fixture.DomainFixture.후추;
import static subway.common.fixture.WebFixture.디노_요청;
import static subway.common.fixture.WebFixture.로운_요청;
import static subway.common.fixture.WebFixture.이호선_초록색_요청;
import static subway.common.fixture.WebFixture.일호선_남색_요청;
import static subway.common.fixture.WebFixture.조앤_요청;
import static subway.common.fixture.WebFixture.후추_요청;
import static subway.common.step.LineStep.addStationToLine;
import static subway.common.step.LineStep.createLineAndGetId;
import static subway.common.step.StationStep.createStationAndGetId;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class PathIntegrationTest extends IntegrationTest{

    @Test
    void 최단_경로를_반환한다() {
        //given
        final Long 일호선_id = createLineAndGetId(일호선_남색_요청);
        final Long 이호선_id = createLineAndGetId(이호선_초록색_요청);

        final Long 후추_id = createStationAndGetId(후추_요청);
        final Long 디노_id = createStationAndGetId(디노_요청);
        final Long 조앤_id = createStationAndGetId(조앤_요청);
        final Long 로운_id = createStationAndGetId(로운_요청);

        addStationToLine(new StationAddRequest(후추_id, 디노_id, 7), 일호선_id);
        addStationToLine(new StationAddRequest(디노_id, 조앤_id, 5), 일호선_id);
        addStationToLine(new StationAddRequest(디노_id, 로운_id, 3), 이호선_id);

        final PathRequest pathRequest = new PathRequest(후추_id, 로운_id);

        //when
        final ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(pathRequest)
                .when()
                .get("/routes/shortest")
                .then().log().all()
                .extract();

        //then
        final PathResponse pathResponse = response.as(PathResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(pathResponse).usingRecursiveComparison()
                    .isEqualTo(new PathResponse(
                            List.of(
                                    LineAndSectionsResponse.of(
                                            new Line(일호선_id, "일호선", "남색", List.of(new Section(1L, 후추, 디노, 7), new Section(2L, 디노, 조앤, 5))),
                                            List.of(new Section(1L, 후추, 디노, 7))
                                    ),
                                    LineAndSectionsResponse.of(
                                            new Line(이호선_id, "이호선", "초록색", List.of(new Section(3L, 디노, 로운, 3))),
                                            List.of(new Section(3L, 디노, 로운, 3))
                                    )
                            ),
                            new DistanceResponse(10),
                            new FareResponse(1250)
                    ));
        });
    }
}
