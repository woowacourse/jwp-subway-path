package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subway.application.request.PathRequest;
import subway.application.request.SectionRequest;
import subway.application.response.PathResponse;
import subway.application.response.StationResponse;
import subway.integration.IntegrationTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static subway.integration.IntegrationFixture.*;

class PathServiceTest extends IntegrationTest {

    @Autowired
    private PathService pathService;
    @Autowired
    private LineService lineService;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        lineService.registerStation(LINE1.getId(), new SectionRequest(STATION_A.getName().getValue(), STATION_B.getName().getValue(), DISTANCE5.getValue()));
        lineService.registerStation(LINE1.getId(), new SectionRequest(STATION_B.getName().getValue(), STATION_C.getName().getValue(), DISTANCE5.getValue()));
        lineService.registerStation(LINE2.getId(), new SectionRequest(STATION_B.getName().getValue(), STATION_D.getName().getValue(), DISTANCE10.getValue()));
        lineService.registerStation(LINE3.getId(), new SectionRequest(STATION_D.getName().getValue(), STATION_C.getName().getValue(), DISTANCE3.getValue()));
        lineService.registerStation(LINE3.getId(), new SectionRequest(STATION_C.getName().getValue(), STATION_E.getName().getValue(), DISTANCE3.getValue()));
    }

    @DisplayName("경로 조회 기능을 테스트한다.")
    @Test
    void findPathTest() {
        final PathRequest request = new PathRequest(STATION_A.getName().getValue(), STATION_D.getName().getValue());

        final PathResponse response = pathService.findPath(request);

        assertAll(
                () -> assertThat(response.getFare()).isEqualTo(1350),
                () -> assertThat(response.getPath())
                        .extracting(StationResponse::getId)
                        .containsExactly(
                                STATION_A.getId(),
                                STATION_B.getId(),
                                STATION_C.getId(),
                                STATION_D.getId()
                        ),
                () -> assertThat(response.getDistance()).isEqualTo(13)
        );
    }

    @DisplayName("경로가 존재하지 않을 경우 예외가 발생한다.")
    @Test
    void findUnablePathTest() {
        final PathRequest request = new PathRequest(STATION_A.getName().getValue(), STATION_F.getName().getValue());

        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pathService.findPath(request)
        );

        assertThat(exception.getMessage()).isEqualTo("경로를 찾을 수 없습니다.");
    }
}
