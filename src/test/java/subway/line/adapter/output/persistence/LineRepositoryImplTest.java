package subway.line.adapter.output.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.line.domain.Line;
import subway.section.adapter.output.persistence.SectionDao;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class LineRepositoryImplTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private LineRepositoryImpl lineRepository;
    
    @BeforeEach
    void setUp() {
        final LineDao lineDao = new LineDao(jdbcTemplate);
        final SectionDao sectionDao = new SectionDao(jdbcTemplate);
        lineRepository = new LineRepositoryImpl(lineDao, sectionDao);
    }
    
    @Test
    void 노선_저장하기() {
        // given
        final Line line1 = new Line("1호선", "파랑");
        final Line line2 = new Line("2호선", "초록");
        
        // when
        final Long lineId1 = lineRepository.save(line1);
        final Long lineId2 = lineRepository.save(line2);
        
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
        final Line line1 = new Line("1호선", "파랑");
        final Line line2 = new Line("2호선", "초록");
        lineRepository.save(line1);
        lineRepository.save(line2);
        
        // when
        final Set<Line> lines = lineRepository.findAll();
        
        // then
        assertThat(lines).contains(line1, line2);
    }
}
