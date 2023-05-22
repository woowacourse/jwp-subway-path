package subway.application.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import subway.StationFixture;
import subway.application.core.service.dto.in.EnrollStationCommand;
import subway.application.core.service.dto.in.JourneyCommand;
import subway.application.core.service.dto.out.JourneyResult;
import subway.application.core.service.dto.out.StationResult;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("classpath:dummy.sql")
class JourneyServiceTest {

    @Autowired
    private JourneyService journeyService;
    @Autowired
    private LineService lineService;

    @Test
    @DisplayName("출발지, 종착지가 주어지면 최단 경로를 반환할 수 있다")
    void findShortestJourney() {
        // given
        lineService.enrollStation(new EnrollStationCommand(1L, 1L, 2L, 3));
        lineService.enrollStation(new EnrollStationCommand(1L, 2L, 3L, 1));

        JourneyCommand command = new JourneyCommand(1L, 3L);

        // when
        JourneyResult result = journeyService.findShortestJourney(command);

        // then
        assertThat(result.getPath()).hasSize(3);
        assertThat(result.getDistance()).isEqualTo(4);
        assertThat(result.getFare()).isEqualTo(1_250);
    }
}
