package subway.application.core.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import subway.application.core.service.dto.in.DeleteStationCommand;
import subway.application.core.service.dto.in.EnrollStationCommand;
import subway.application.core.service.dto.in.IdCommand;
import subway.application.core.service.dto.out.StationResult;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("classpath:dummy.sql")
class LineServiceTest {

    @Autowired
    private LineService lineService;

    @Test
    @DisplayName("역을 노선에 등록할 수 있다")
    void enrollStation() {
        // given
        EnrollStationCommand command = new EnrollStationCommand(1L, 1L, 2L, 3);

        // when
        lineService.enrollStation(command);

        // then
        assertThat(lineService.findAllRouteMap()).isNotEmpty();
    }

    @Test
    @DisplayName("역을 노선에서 삭제할 수 있다")
    void deleteStation() {
        // given
        lineService.enrollStation(new EnrollStationCommand(1L, 1L, 2L, 3));
        DeleteStationCommand command = new DeleteStationCommand(1L, 1L);

        // when
        lineService.deleteStation(command);

        // then
        assertThat(lineService.findAllRouteMap().get("1호선")).hasSize(0);
    }

    @Test
    @DisplayName("노선을 찾을 수 있다")
    void findRouteMap() {
        // given
        lineService.enrollStation(new EnrollStationCommand(1L, 1L, 2L, 3));

        // when
        IdCommand idCommand = new IdCommand(1L);
        List<StationResult> routeMap = lineService.findRouteMap(idCommand);

        // then
        assertThat(routeMap.get(0).getName()).isEqualTo("잠실역");
        assertThat(routeMap.get(1).getName()).isEqualTo("방배역");
    }

    @Test
    @DisplayName("모든 노선을 찾을 수 있다")
    void findAllRouteMap() {
        // given
        lineService.enrollStation(new EnrollStationCommand(1L, 1L, 2L, 3));
        lineService.enrollStation(new EnrollStationCommand(2L, 2L, 1L, 3));

        // when
        Map<String, List<StationResult>> allRouteMap = lineService.findAllRouteMap();

        // then
        assertThat(allRouteMap.get("1호선")).hasSize(2);
        assertThat(allRouteMap.get("2호선")).hasSize(2);
    }
}
