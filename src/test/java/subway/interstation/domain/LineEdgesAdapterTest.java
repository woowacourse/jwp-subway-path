package subway.interstation.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.line.application.LineQueryService;
import subway.line.db.LineRepositoryImpl;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.route.domain.Edges;
import subway.route.domain.InterStationEdge;
import subway.route.line.LineEdgesAdapter;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("경로 조회용 역할 어댑터 테스트")
@JdbcTest
class LineEdgesAdapterTest {

    private LineRepository lineRepository;
    private LineEdgesAdapter lineEdgesAdapter;
    private long line2호선_id;
    private long line3호선_id;

    @Autowired
    private void setUp(JdbcTemplate jdbcTemplate) {
        lineRepository = new LineRepositoryImpl(jdbcTemplate);
        LineQueryService lineFindAllService = new LineQueryService(lineRepository);
        lineEdgesAdapter = new LineEdgesAdapter(lineFindAllService);
    }

    @BeforeEach
    void setLine() {
        line2호선_id = lineRepository.save(new Line("2호선", "노란색", 3L, 4L, 3L)).getId();
        line3호선_id = lineRepository.save(new Line("3호선", "빨간색", 4L, 5L, 3L)).getId();
    }

    @Test
    void 정상적으로_모든_역을_그래프_형태로_반환한다() {
        // when
        Edges allEdges = lineEdgesAdapter.findAllEdges();
        // then

        assertThat(allEdges.getEdges()).usingRecursiveComparison()
                .ignoringExpectedNullFields().isEqualTo(List.of(
                        new InterStationEdge(3L, 4L, 3L, line2호선_id),
                        new InterStationEdge(4L, 5L, 3L, line3호선_id)));
    }
}
