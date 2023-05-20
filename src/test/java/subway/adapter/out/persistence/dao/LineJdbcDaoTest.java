package subway.adapter.out.persistence.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import subway.adapter.out.persistence.entity.LineEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
class LineJdbcDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LineJdbcDao lineJdbcDao;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @BeforeEach
    void setUp() {
        lineJdbcDao = new LineJdbcDao(namedParameterJdbcTemplate, jdbcTemplate);
    }

    @Test
    @DisplayName("노선을 추가한다.")
    void createLine() {
        final LineEntity lineEntity = new LineEntity("1호선", 1000);
        Long lineId = lineJdbcDao.createLine(lineEntity);

        LineEntity result = lineJdbcDao.findLineById(lineId).get();
        assertThat(lineEntity.getName()).isEqualTo(result.getName());

    }

    @Test
    @DisplayName("노선을 삭제한다.")
    void deleteById() {
        final LineEntity lineEntity = new LineEntity("1호선",2000);
        final Long lineId = lineJdbcDao.createLine(lineEntity);

        lineJdbcDao.deleteById(lineId);

        assertThat(lineJdbcDao.findAll()).hasSize(0);
    }

    @Test
    void findAll() {
        final LineEntity lineEntity1 = new LineEntity("1호선", 1000);
        final LineEntity lineEntity2 = new LineEntity("2호선", 1000);
        final LineEntity lineEntity3 = new LineEntity("3호선", 1000);
        Long lineId1 = lineJdbcDao.createLine(lineEntity1);
        Long lineId2 = lineJdbcDao.createLine(lineEntity2);
        Long lineId3 = lineJdbcDao.createLine(lineEntity3);

        List<LineEntity> result = lineJdbcDao.findAll();
        assertAll(
                () -> assertThat(result).usingRecursiveComparison().isEqualTo(
                        List.of(new LineEntity(lineId1, lineEntity1.getName(), lineEntity1.getSurcharge()),
                                new LineEntity(lineId2, lineEntity2.getName(), lineEntity2.getSurcharge()),
                                new LineEntity(lineId3, lineEntity3.getName(), lineEntity3.getSurcharge())
                        )
                )
        );
    }
}
