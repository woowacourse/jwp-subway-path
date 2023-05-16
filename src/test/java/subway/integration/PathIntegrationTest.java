package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.Station;
import subway.dto.request.PathRequest;
import subway.dto.response.PathAndFeeResponse;
import subway.dto.response.StationsResponse;
import subway.exceptions.ExceptionResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("classpath:/testData.sql")
@DisplayName("경로 조회, 요금 부과 기능")
public class PathIntegrationTest extends IntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /*             천호
    (거리:10) 8호선-> ㅣ  \ <-8호선 (거리:5)
                   잠실 - 잠실새내 - 종합운동장
             (거리:10) 2호선     2호선 (거리:10)
    */
    @BeforeEach
    public void setUp() {
        super.setUp();
        // 천호 - 잠실새내 8호선 연결
        String sql = "insert into section (id, up_bound_station_id, down_bound_station_id, line_id, distance) values (4, 2, 5, 2, 5)";
        jdbcTemplate.execute(sql);
    }

    @DisplayName("SourceStation 부터 TargetStation 까지 경로와 요금을 조회한다.")
    @Test
    void findPathAndFee() {
        // given
        // 종합운동장 -> 천호
        PathRequest pathRequest = new PathRequest(3L, 5L);
        PathAndFeeResponse expectedResponse = new PathAndFeeResponse(
                new StationsResponse(List.of(
                        new Station(3L, "종합운동장"),
                        new Station(2L, "잠실새내"),
                        new Station(5L, "천호")
                )),
                1350
        );

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(pathRequest)
                .when().post("/paths")
                .then().log().all().
                extract();
        PathAndFeeResponse bodyResponse = response.as(PathAndFeeResponse.class);
        StationsResponse actualStations = bodyResponse.getStationsResponse();
        int actualFee = bodyResponse.getFee();

        // then
        assertThat(actualStations.getStations()).containsExactlyElementsOf(expectedResponse.getStationsResponse().getStations());
        assertThat(actualFee).isEqualTo(expectedResponse.getFee());
    }

    @DisplayName("SourceStation 또는 TargetStation 이 등록된 역이 아니면 ExceptionResponse 를 반환한다.")
    @Test
    void findPathAndFeeWithNotExistingStations() {
        // given
        PathRequest pathRequest = new PathRequest(10L, 5L);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(pathRequest)
                .when().post("/paths")
                .then().log().all().
                extract();
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        // then
        assertThat(exceptionResponse.getMessage())
                .contains("역");
    }
}
