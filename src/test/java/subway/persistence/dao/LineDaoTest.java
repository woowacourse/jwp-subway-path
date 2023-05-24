package subway.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import subway.persistence.entity.LineEntity;

@ActiveProfiles({"test"})
@JdbcTest
class LineDaoTest {

    private final LineDao lineDao;

    @Autowired
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    LineDaoTest(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.lineDao = new LineDao(namedParameterJdbcTemplate);
    }


    @DisplayName("DB에 노선을 삽입힌다.")
    @Test
    void shouldInsertLineWhenRequest() {
        LineEntity lineEntityToSave = new LineEntity("2호선", 0);
        LineEntity lineEntityAfterSave = lineDao.insert(lineEntityToSave);

        assertThat(lineEntityAfterSave.getName()).isEqualTo(lineEntityToSave.getName());
    }

    @DisplayName("DB에서 ID로 특정 노선을 조회한다")
    @Test
    void shouldFindLineByIdFromDbWhenRequest() {
        LineEntity lineEntityToSave = new LineEntity("2호선", 0);
        LineEntity lineEntityAfterSave = lineDao.insert(lineEntityToSave);

        LineEntity lineEntityFoundById = lineDao.findById(lineEntityAfterSave.getId());

        assertThat(lineEntityFoundById.getName()).isEqualTo(lineEntityAfterSave.getName());
    }

    @DisplayName("DB에서 모든 노선을 조회한다")
    @Test
    void shouldFindAllLinesWhenRequest() {
        LineEntity lineEntity1 = new LineEntity("2호선", 0);
        LineEntity lineEntity2 = new LineEntity("3호선", 0);
        lineDao.insert(lineEntity1);
        lineDao.insert(lineEntity2);

        List<LineEntity> lineEntitiesFromDb = lineDao.findAll();

        assertAll(
                () -> assertThat(lineEntitiesFromDb).hasSize(2),
                () -> assertThat(lineEntitiesFromDb.get(0).getName()).isEqualTo(lineEntity1.getName()),
                () -> assertThat(lineEntitiesFromDb.get(1).getName()).isEqualTo(lineEntity2.getName())
        );
    }
}
