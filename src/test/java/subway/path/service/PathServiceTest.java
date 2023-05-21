package subway.path.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.line.domain.Path;
import subway.domain.line.service.PathService;
import subway.domain.line.entity.StationEntity;

import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@SuppressWarnings("NonAsciiCharacters")
@Sql({"classpath:schema.sql", "classpath:data.sql"})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestPropertySource(properties = "spring.config.location = classpath:application.yml")
class PathServiceTest {

    @Autowired
    private PathService pathService;

    @Test
    void 환승_최단거리_구하기() {
        Path path = pathService.findShortestPath(7L, 8L);
        assertAll(
                () -> Assertions.assertThat(path.getPath()).containsExactly(
                        new StationEntity(7L, "서초역"),
                        new StationEntity(6L, "방배역"),
                        new StationEntity(5L, "사당역"),
                        new StationEntity(4L, "낙성대역"),
                        new StationEntity(9L, "강남역"),
                        new StationEntity(8L, "교대역")
                ),
                () -> Assertions.assertThat(path.getDistance()).isEqualTo(35)
        );
    }
}
