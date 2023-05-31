package subway.station.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import subway.line.ui.dto.LineCreationRequest;
import subway.line.ui.dto.StationAdditionRequest;
import subway.station.domain.Station;
import subway.station.repository.StationRepository;
import subway.utils.BaseTest;

import static subway.utils.StationFixture.JAMSIL_NARU_STATION;
import static subway.utils.StationFixture.JAMSIL_STATION;
import static subway.utils.Steps.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/schema.sql")
public class StationAcceptanceTest extends BaseTest {

    public static final String VALID_STATION_NAME = "서울대입구";
    public static final int DISTANCE = 5;

    @LocalServerPort
    int port;

    @Autowired
    StationRepository stationRepository;

    private Station upstream;
    private Station downstream;
    private long lineId;

    @BeforeEach
    public void setUp() {
        super.setUp(port);

        upstream = stationRepository.createStation(JAMSIL_STATION);
        downstream = stationRepository.createStation(JAMSIL_NARU_STATION);

        노선을_만든다(new LineCreationRequest("2호선", upstream.getName(), downstream.getName(), 5, 0));
        lineId = 1;
    }

    @Test
    @DisplayName("노선에 새로운 역을 추가할 수 있다.")
    void addStationSuccess() {
        final StationAdditionRequest stationAdditionRequest = new StationAdditionRequest(VALID_STATION_NAME, upstream.getId(), downstream.getId(), DISTANCE - 1);

        역을_추가한다(lineId, stationAdditionRequest)
                .statusCode(HttpStatus.CREATED.value());
    }

    @ParameterizedTest(name = "역 이름 길이가 맞지 않으면 역을 추가할 수 없다.")
    @ValueSource(strings = {"서", "서울대입구서울대16자이름입니다"})
    void addStationFail1(String invalidStationName) {
        final StationAdditionRequest invalidStationAdditionRequest = new StationAdditionRequest(invalidStationName, upstream.getId(), downstream.getId(), DISTANCE - 1);

        역을_추가한다(lineId, invalidStationAdditionRequest)
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("노선 아이디가 존재하지 않으면 않으면 역을 추가할 수 없다.")
    void addStationFail2() {
        final StationAdditionRequest stationAdditionRequest = new StationAdditionRequest(VALID_STATION_NAME, upstream.getId(), downstream.getId(), DISTANCE - 1);

        역을_추가한다(0, stationAdditionRequest)
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("상행역이 없으면 역을 추가할 수 없다.")
    void addStationFail3() {
        final StationAdditionRequest invalidStationAdditionRequest = new StationAdditionRequest(VALID_STATION_NAME, 100L, downstream.getId(), DISTANCE - 1);

        역을_추가한다(lineId, invalidStationAdditionRequest)
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("하행역이 없으면 역을 추가할 수 없다.")
    void addStationFail4() {
        final StationAdditionRequest invalidStationAdditionRequest = new StationAdditionRequest(VALID_STATION_NAME, upstream.getId(), 100L, DISTANCE - 1);

        역을_추가한다(lineId, invalidStationAdditionRequest)
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("upstream-distance가 기존 상행역 하행역 간의 거리와 같거나 크면 추가할 수 없다.")
    void addStationFail5() {
        final StationAdditionRequest invalidStationAdditionRequest = new StationAdditionRequest(VALID_STATION_NAME, upstream.getId(), downstream.getId(), DISTANCE);

        역을_추가한다(lineId, invalidStationAdditionRequest)
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("상행역과 하행역이 Section을 이루지 않는 경우 추가할 수 없다.")
    void addStationFail6() {
        final StationAdditionRequest invalidStationAdditionRequest = new StationAdditionRequest("건대입구", upstream.getId(), 0L, 3);

        역을_추가한다(lineId, invalidStationAdditionRequest)
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("station-name이 이미 Line에 존재하는 경우 추가할 수 없다.")
    void addStationFail7() {
        역을_추가한다(lineId, new StationAdditionRequest(upstream.getName(), upstream.getId(), downstream.getId(), DISTANCE - 1))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("역이 두 개인 경우 노선을 삭제할 수 있다.")
    void deleteStationSuccess1() {
        역을_삭제한다(lineId, upstream.getId())
                .statusCode(HttpStatus.NO_CONTENT.value());

        노선을_찾는다(LINE_URL + "/1")
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("역이 두 개인 경우 노선을 삭제할 수 있다.")
    void deleteStationSuccess2() {
        역을_삭제한다(lineId, downstream.getId())
                .statusCode(HttpStatus.NO_CONTENT.value());

        노선을_찾는다(LINE_URL + "/1")
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("역 id가 없는 경우 NOT_FOUND 반환한다")
    void deleteStationFail1() {
        역을_삭제한다(lineId, 0)
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("노선 id가 없는 경우 NOT_FOUND 반환한다")
    void deleteStationFail2() {
        역을_삭제한다(0, upstream.getId())
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
