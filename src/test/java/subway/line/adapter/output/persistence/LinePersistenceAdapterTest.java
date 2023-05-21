package subway.line.adapter.output.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.line.domain.Line;
import subway.section.adapter.output.persistence.SectionDao;
import subway.station.adapter.output.persistence.StationDao;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class LinePersistenceAdapterTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private LinePersistenceAdapter adapter;
    
    @BeforeEach
    void setUp() {
        final LineDao lineDao = new LineDao(jdbcTemplate);
        final SectionDao sectionDao = new SectionDao(jdbcTemplate);
        final StationDao stationDao = new StationDao(jdbcTemplate);
        adapter = new LinePersistenceAdapter(lineDao, sectionDao, stationDao);
    }
    
    @Test
    void 노선_저장하기() {
        // given
        final Line line1 = new Line("1호선", "파랑", 0L);
        final Line line2 = new Line("2호선", "초록", 0L);
        
        // when
        final Long lineId1 = adapter.save(line1);
        final Long lineId2 = adapter.save(line2);
        
        // then
        assertAll(
                () -> assertThat(lineId1).isPositive(),
                () -> assertThat(lineId2).isPositive(),
                () -> assertThat(lineId2).isGreaterThan(lineId1)
        );
    }
    
    @Test
    void 모든_노선_찾기() {
        // given
        final Line line1 = new Line("1호선", "파랑", 0L);
        final Line line2 = new Line("2호선", "초록", 0L);
        adapter.save(line1);
        adapter.save(line2);
        
        // when
        final Set<Line> lines = adapter.getAll();
        
        // then
        assertThat(lines).contains(line1, line2);
    }
    
    @Test
    void id로_노선_찾기() {
        // given
        final String name = "1호선";
        final String color = "파랑";
        final Line line = new Line(name, color, 0L);
        final Long id = adapter.save(line);
        
        // when
        final Line result = adapter.getLineById(id);
        
        // then
        assertThat(result).isEqualTo(new Line(name, color, 0L));
    }
    
    @Test
    void id로_노선_찾을시_존재하지_않는_노선이면_예외_발생() {
        // given
        final Long id = 1L;
        
        // expect
        assertThatIllegalArgumentException()
                .isThrownBy(() -> adapter.getLineById(id));
    }
    
    @Test
    void id로_노선_삭제() {
        // given
        final String name = "1호선";
        final String color = "파랑";
        final Line line = new Line(name, color, 0L);
        final Long id = adapter.save(line);
        
        // expect
        assertThatNoException()
                .isThrownBy(() -> adapter.deleteById(id));
    }
}
