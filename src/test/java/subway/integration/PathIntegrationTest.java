package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.application.LineService;
import subway.application.SectionService;
import subway.application.dto.LineDto;
import subway.application.dto.SectionDto;
import subway.presentation.dto.response.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

class PathIntegrationTest extends IntegrationTest {

    @Autowired
    private LineService lineService;

    @Autowired
    private SectionService sectionService;

    @BeforeEach
    void setUp() {
        RestAssured.port = super.port;
    }

    @DisplayName("최단 경로를 조회할 수 있다.")
    @Test
    void getShortestPath() {
        Long 호선1 = lineService.saveLine("1호선");
        Long 호선2 = lineService.saveLine("2호선");

        sectionService.saveSection(호선1, new SectionDto("회기", "왕십리", 5));
        sectionService.saveSection(호선1, new SectionDto("왕십리", "청량리", 3));
        sectionService.saveSection(호선2, new SectionDto("왕십리", "건대입구", 25));

        LineDto 호선1Line = lineService.findLineById(호선1);
        LineDto 호선2Line = lineService.findLineById(호선2);

        Long 청량리Id = findStationIdByName(호선1Line, "청량리");
        Long 건대입구Id = findStationIdByName(호선2Line, "건대입구");

        ExtractableResponse<Response> response = given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/subway/path/{startStationId}/{endStationId}", 청량리Id, 건대입구Id)
                .then().log().all()
                .extract();

        List<String> path = response.jsonPath().getList("stations", StationResponse.class).stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(path).containsExactly("청량리", "왕십리", "건대입구");
    }

    private Long findStationIdByName(LineDto lineDto, String name) {
        return lineDto.getStations()
                .stream()
                .filter(station -> station.getName().equals(name))
                .findFirst()
                .get()
                .getId();
    }
}
