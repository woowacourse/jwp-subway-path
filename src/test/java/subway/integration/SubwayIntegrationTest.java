package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.LineInPath;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.LineInPathDto;
import subway.dto.request.FindShortestRouteRequest;
import subway.dto.response.FindShortestRouteResponse;
import subway.repository.LineRepository;

public class SubwayIntegrationTest extends IntegrationTest {

    private Station STATION_A;
    private Station STATION_B;
    private Station STATION_C;
    private Station STATION_D;
    private Station STATION_E;
    private Station STATION_F;

    private Line LINEA;
    private Line LINEB;
    private Line LINEC;


    @Autowired
    private StationDao stationDao;
    @Autowired
    private LineDao lineDao;

    @Autowired
    private SectionDao sectionDao;
    @Autowired
    private LineRepository lineRepository;


    @BeforeEach
    public void setUp() {
        super.setUp();

        STATION_A = stationDao.insert(new Station("A"));
        STATION_B = stationDao.insert(new Station("B"));
        STATION_C = stationDao.insert(new Station("C"));
        STATION_D = stationDao.insert(new Station("D"));
        STATION_E = stationDao.insert(new Station("E"));
        STATION_F = stationDao.insert(new Station("F"));

        LINEA = lineRepository.assembleLine(lineDao.insert(new Line("LINEA", 100)));
        LINEB = lineRepository.assembleLine(lineDao.insert(new Line("LINEB", 0)));
        LINEC = lineRepository.assembleLine(lineDao.insert(new Line("LINEC", 500)));

        // a-b-c
        sectionDao.insert(LINEA.getId(), new Section(STATION_A, STATION_B, 3));
        sectionDao.insert(LINEA.getId(), new Section(STATION_B, STATION_C, 25));

        // d-b-e-c
        sectionDao.insert(LINEB.getId(), new Section(STATION_D, STATION_B, 4));
        sectionDao.insert(LINEB.getId(), new Section(STATION_B, STATION_E, 3));
        sectionDao.insert(LINEB.getId(), new Section(STATION_E, STATION_C, 1));

        // c-f
        sectionDao.insert(LINEC.getId(), new Section(STATION_C, STATION_F, 2));
    }

    @Test
    @DisplayName("최소 경로를 조회한다")
    void findShortestRoute() {
        FindShortestRouteRequest request = new FindShortestRouteRequest(24, STATION_A.getId(), STATION_F.getId());

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/routes")
                .then().log().all().
                extract();

        List<LineInPathDto> pathDtos = List.of(
                LineInPathDto.from(new LineInPath(LINEA.getId(), LINEA.getName(), List.of(STATION_A, STATION_B))),
                LineInPathDto.from(new LineInPath(LINEB.getId(), LINEB.getName(), List.of(STATION_B, STATION_E, STATION_C))),
                LineInPathDto.from(new LineInPath(LINEC.getId(), LINEC.getName(), List.of(STATION_C, STATION_F))
                ));
        FindShortestRouteResponse expectedResponse = new FindShortestRouteResponse(pathDtos, 9.0, 1750.0);


        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(FindShortestRouteResponse.class)).usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }
}
