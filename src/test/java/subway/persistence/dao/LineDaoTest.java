package subway.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import subway.persistence.entity.LineEntity;

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
        LineEntity lineEntityToSave = new LineEntity("2호선", "잠실역", "몽촌토성역");
        LineEntity lineEntityAfterSave = lineDao.insert(lineEntityToSave);

        assertAll(
                () -> assertThat(lineEntityAfterSave.getUpwardTerminus())
                        .isEqualTo(lineEntityToSave.getUpwardTerminus()),
                () -> assertThat(lineEntityAfterSave.getDownwardTerminus())
                        .isEqualTo(lineEntityToSave.getDownwardTerminus())
        );
    }

    @DisplayName("DB에서 ID로 특정 노선을 조회한다")
    @Test
    void shouldFindLineByIdFromDbWhenRequest() {
        LineEntity lineEntityToSave = new LineEntity("2호선", "잠실역", "몽촌토성역");
        LineEntity lineEntityAfterSave = lineDao.insert(lineEntityToSave);

        LineEntity lineEntityFoundById = lineDao.findById(lineEntityAfterSave.getId());

        assertAll(
                () -> assertThat(lineEntityFoundById.getUpwardTerminus())
                        .isEqualTo(lineEntityToSave.getUpwardTerminus()),
                () -> assertThat(lineEntityFoundById.getDownwardTerminus())
                        .isEqualTo(lineEntityToSave.getDownwardTerminus())
        );
    }

    @DisplayName("DB에서 모든 노선을 조회한다")
    @Test
    void shouldFindAllLinesWhenRequest() {
        LineEntity lineEntity1 = new LineEntity("2호선", "잠실역", "몽촌토성역");
        LineEntity lineEntity2 = new LineEntity("3호선", "수서역", "교대역");
        lineDao.insert(lineEntity1);
        lineDao.insert(lineEntity2);

        List<LineEntity> lineEntitiesFromDb = lineDao.findAll();

        assertAll(
                () -> assertThat(lineEntitiesFromDb).hasSize(2),
                () -> assertThat(lineEntitiesFromDb.get(0).getUpwardTerminus())
                        .isEqualTo(lineEntity1.getUpwardTerminus()),
                () -> assertThat(lineEntitiesFromDb.get(0).getDownwardTerminus())
                        .isEqualTo(lineEntity1.getDownwardTerminus())
        );
    }

    @DisplayName("DB에 특정 노선을 업데이트한다.")
    @Test
    void shouldUpdateLineWhenRequest() {
        LineEntity lineEntityToSave = new LineEntity("2호선", "잠실역", "몽촌토성역");
        LineEntity lineEntityAfterSave = lineDao.insert(lineEntityToSave);

        LineEntity lineEntityToUpdate = new LineEntity(lineEntityAfterSave.getId(), "2호선", "강남역", "몽촌토성역");
        LineEntity lineEntityAfterUpdate = lineDao.update(lineEntityToUpdate);

        assertAll(
                () -> assertThat(lineEntityAfterUpdate.getUpwardTerminus())
                        .isEqualTo(lineEntityToUpdate.getUpwardTerminus()),
                () -> assertThat(lineEntityAfterUpdate.getDownwardTerminus())
                        .isEqualTo(lineEntityToSave.getDownwardTerminus())
        );
    }
}
