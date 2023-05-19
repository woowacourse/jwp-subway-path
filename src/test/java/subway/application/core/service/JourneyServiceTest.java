package subway.application.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import subway.StationFixture;
import subway.application.core.domain.Distance;
import subway.application.core.domain.Line;
import subway.application.core.domain.LineProperty;
import subway.application.core.domain.Section;
import subway.application.core.service.dto.in.JourneyCommand;
import subway.application.core.service.dto.out.JourneyResult;
import subway.application.port.LineRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("classpath:dummy.sql")
class JourneyServiceTest {

    @Autowired
    private JourneyService journeyService;
    @Autowired
    private LineRepository lineRepository;

    @Test
    @DisplayName("출발지, 종착지가 주어지면 최단 경로를 반환할 수 있다")
    void findShortestJourney() {
        // given
        LineProperty lineProperty = new LineProperty(1L, "1호선", "파랑");

        lineRepository.insert(new Line(lineProperty, List.of(
                new Section(
                        StationFixture.of(1L, "잠실역"),
                        StationFixture.of(2L, "방배역"),
                        new Distance(1)),
                new Section(
                        StationFixture.of(2L, "방배역"),
                        StationFixture.of(3L, "서초역"),
                        new Distance(2))
        )));

        JourneyCommand command = new JourneyCommand(1L, 3L);

        // when
        JourneyResult result = journeyService.findShortestJourney(command);

        // then
        assertThat(result.getPath()).containsExactly(
                StationFixture.of(1L, "잠실역"), StationFixture.of(2L, "방배역"),
                StationFixture.of(3L, "서초역")
        );
        assertThat(result.getDistance()).isEqualTo(3);
        assertThat(result.getFare()).isEqualTo(1_250);
    }
}
