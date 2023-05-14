package subway.line.adapter.output.persistence;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class LineDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private LineDao lineDao;
    
    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate);
    }
    
    @Test
    void 라인_저장하기() {
        // given
        final LineEntity lineEntity = new LineEntity("1호선", "파랑");
        
        // when
        final Long lineId = lineDao.insert(lineEntity);
        
        // then
        assertThat(lineId).isPositive();
    }
    
    @Test
    void 모든_라인_찾기() {
        // given
        final Long lineId1 = lineDao.insert(new LineEntity("1호선", "파랑"));
        final Long lineId2 = lineDao.insert(new LineEntity("2호선", "초록"));
        
        // when
        final List<LineEntity> lineEntities = lineDao.findAll();
        
        // then
        assertThat(lineEntities).contains(new LineEntity(lineId1, "1호선", "파랑"), new LineEntity(lineId2, "2호선", "초록"));
    }
}
