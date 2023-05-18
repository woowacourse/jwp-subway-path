package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subway.integration.IntegrationTest;
import subway.ui.request.PathRequest;
import subway.ui.request.SectionRequest;
import subway.ui.response.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static subway.integration.IntegrationFixture.*;

class SectionServiceTest extends IntegrationTest {

    @Autowired
    private SectionService sectionService;
    @Autowired
    private LineService lineService;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        lineService.registerStation(LINE1.getId(), new SectionRequest(STATION_A.getName(), STATION_B.getName(), DISTANCE5.getValue()));
        lineService.registerStation(LINE1.getId(), new SectionRequest(STATION_B.getName(), STATION_C.getName(), DISTANCE5.getValue()));
        lineService.registerStation(LINE2.getId(), new SectionRequest(STATION_B.getName(), STATION_D.getName(), DISTANCE10.getValue()));
        lineService.registerStation(LINE3.getId(), new SectionRequest(STATION_D.getName(), STATION_C.getName(), DISTANCE3.getValue()));
        lineService.registerStation(LINE3.getId(), new SectionRequest(STATION_C.getName(), STATION_E.getName(), DISTANCE3.getValue()));
    }

    @DisplayName("경로 조회 기능을 테스트한다.")
    @Test
    void findPathTest() {
        final PathRequest request = new PathRequest(STATION_A.getName(), STATION_D.getName());

        final List<StationResponse> pathResponses = sectionService.findPath(request);

        assertThat(pathResponses)
                .extracting(StationResponse::getId)
                .containsExactly(
                        STATION_A.getId(),
                        STATION_B.getId(),
                        STATION_C.getId(),
                        STATION_D.getId()
                );
    }

    @DisplayName("경로가 존재하지 않을 경우 예외가 발생한다.")
    @Test
    void findUnablePathTest() {
        final PathRequest request = new PathRequest(STATION_A.getName(), STATION_F.getName());

        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> sectionService.findPath(request)
        );
        assertThat(exception.getMessage()).isEqualTo("경로를 찾을 수 없습니다.");
    }
}
