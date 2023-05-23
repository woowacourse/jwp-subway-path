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
import subway.persistence.entity.SectionEntity;

@ActiveProfiles({"test"})
@JdbcTest
class SectionDaoTest {

    private final SectionDao sectionDao;
    private final LineDao lineDao;

    @Autowired
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    SectionDaoTest(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.sectionDao = new SectionDao(namedParameterJdbcTemplate);
        this.lineDao = new LineDao(namedParameterJdbcTemplate);
    }

    @DisplayName("DB에 구간을 삽입한다.")
    @Test
    void shouldInsertSectionWhenRequest() {
        LineEntity lineEntity = new LineEntity("2호선");
        LineEntity lineEntityAfterSave = lineDao.insert(lineEntity);

        SectionEntity sectionEntityToInsert = new SectionEntity(
                lineEntityAfterSave.getId(),
                1L,
                2L,
                3);
        sectionDao.insert(sectionEntityToInsert);

        List<SectionEntity> sectionEntities = sectionDao.findAllByLineId(lineEntityAfterSave.getId());
        assertAll(
                () -> assertThat(sectionEntities.get(0).getUpwardStationId())
                        .isEqualTo(sectionEntityToInsert.getUpwardStationId()),
                () -> assertThat(sectionEntities.get(0).getDownwardStationId())
                        .isEqualTo(sectionEntityToInsert.getDownwardStationId())
        );
    }

    @DisplayName("DB에서 특정 노선에 속하는 모든 구간을 가져온다.")
    @Test
    void shouldReadAllSectionsWhenRequestLineId() {
        LineEntity lineEntity = new LineEntity("2호선");
        LineEntity lineEntityAfterSave = lineDao.insert(lineEntity);

        SectionEntity sectionEntityToInsert1 = new SectionEntity(
                lineEntityAfterSave.getId(),
                1L,
                2L,
                3);
        sectionDao.insert(sectionEntityToInsert1);

        SectionEntity sectionEntityToInsert2 = new SectionEntity(
                lineEntityAfterSave.getId(),
                2L,
                3L,
                3);
        sectionDao.insert(sectionEntityToInsert2);

        List<SectionEntity> sectionEntities = sectionDao.findAllByLineId(lineEntityAfterSave.getId());
        assertAll(
                () -> assertThat(sectionEntities).hasSize(2),
                () -> assertThat(sectionEntities.get(0).getUpwardStationId())
                        .isEqualTo(sectionEntityToInsert1.getUpwardStationId()),
                () -> assertThat(sectionEntities.get(1).getDownwardStationId())
                        .isEqualTo(sectionEntityToInsert2.getDownwardStationId())
        );
    }

    @DisplayName("DB에서 특정 노선에 속하는 모든 구간을 삭제한다.")
    @Test
    void shouldDeleteSectionsWhenRequestLineId() {
        LineEntity lineEntity = new LineEntity("2호선");
        LineEntity lineEntityAfterSave = lineDao.insert(lineEntity);

        SectionEntity sectionEntityToInsert1 = new SectionEntity(
                lineEntityAfterSave.getId(),
                1L,
                2L,
                3);
        sectionDao.insert(sectionEntityToInsert1);

        SectionEntity sectionEntityToInsert2 = new SectionEntity(
                lineEntityAfterSave.getId(),
                2L,
                3L,
                3);
        sectionDao.insert(sectionEntityToInsert2);

        sectionDao.deleteAllByLineId(lineEntityAfterSave.getId());

        List<SectionEntity> sectionEntities = sectionDao.findAllByLineId(lineEntityAfterSave.getId());
        assertThat(sectionEntities).isEmpty();
    }
}
