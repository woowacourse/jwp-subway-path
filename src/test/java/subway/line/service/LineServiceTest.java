package subway.line.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.line.domain.ShortestPath;
import subway.domain.line.service.LineService;
import subway.domain.station.entity.StationEntity;

@SpringBootTest
@Sql({"classpath:schema.sql", "classpath:data.sql"})
@TestPropertySource(properties = "spring.config.location = classpath:application.yml")
class LineServiceTest {

    @Autowired
    private LineService lineService;

    @Test
    void 환승_최단거리_구하기() {
        ShortestPath shortestPath = lineService.findShortestPath(7L, 8L);
        org.junit.jupiter.api.Assertions.assertAll(
                () -> Assertions.assertThat(shortestPath.getPath()).containsExactly(
                        new StationEntity(7L, "서초역"),
                        new StationEntity(6L, "방배역"),
                        new StationEntity(5L, "사당역"),
                        new StationEntity(4L, "낙성대역"),
                        new StationEntity(9L, "강남역"),
                        new StationEntity(8L, "교대역")
                ),
                () -> Assertions.assertThat(shortestPath.getDistance()).isEqualTo(35)
        );
    }
}
