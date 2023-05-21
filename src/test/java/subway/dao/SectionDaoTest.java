package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SectionDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SectionDao sectionDao;

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(jdbcTemplate);
    }

    @Test
    @DisplayName("save() : section을 저장할 수 있다.")
    void test_save() throws Exception {
        //given
        final String currentStationName = "H";
        final String nextStationName = "Z";
        final int distance = 1;
        final long lineId = 2L;

        final SectionEntity sectionEntity = new SectionEntity(
                currentStationName,
                nextStationName,
                distance,
                lineId
        );

        final int beforeSize = sectionDao.findSectionsByLineId(lineId).size();

        //when
        sectionDao.save(sectionEntity);

        //then
        final int afterSize = sectionDao.findSectionsByLineId(lineId).size();

        assertEquals(afterSize, beforeSize + 1);
    }

    @Test
    @DisplayName("findSectionsByLineId() : 라인에 속한 모든 section을 조회할 수 있다.")
    void test_findSectionsByLineId() throws Exception {
        //given
        final long lineId = 2L;

        //when
        final int size = sectionDao.findSectionsByLineId(lineId).size();

        //then
        assertEquals(4, size);
    }

    @Test
    @DisplayName("deleteAll() : 라인에 포함되있는 section을 모두 삭제할 수 있다.")
    void test_deleteAll() throws Exception {
        //given
        final long lineId = 2L;

        //when
        sectionDao.deleteAll(lineId);

        //then
        assertEquals(0, sectionDao.findSectionsByLineId(lineId).size());
    }

    @Test
    @DisplayName("update() : id를 통해 세션의 정보를 수정할 수 있다.")
    void test_update() throws Exception {
        //given
        final Long sectionId = 7L;
        final String updatedCurrentStationName = "Z";
        final String updatedNextStationName = "K";
        final int updatedDistance = 3;
        final long lineId = 2L;

        final SectionEntity updatedSectionEntity = new SectionEntity(
                7L,
                updatedCurrentStationName,
                updatedNextStationName,
                updatedDistance,
                lineId
        );

        //when
        sectionDao.update(updatedSectionEntity);

        //then
        final SectionEntity sectionEntity =
                sectionDao.findSectionsByLineId(lineId)
                          .stream()
                          .filter(it -> it.getId().equals(sectionId))
                          .findAny()
                          .orElseThrow();

        assertAll(
                () -> assertEquals(sectionEntity.getDistance(), updatedSectionEntity.getDistance()),
                () -> assertEquals(sectionEntity.getCurrentStationName(),
                                   updatedSectionEntity.getCurrentStationName()),
                () -> assertEquals(sectionEntity.getNextStationName(),
                                   updatedSectionEntity.getNextStationName())
        );
    }

    @Test
    @DisplayName("deleteById() : id를 통해 section을 삭제할 수 있다.")
    void test_deleteById() throws Exception {
        //given
        final long sectionId = 1L;
        final long lineId = 1L;

        final int beforeSize = sectionDao.findSectionsByLineId(lineId).size();

        //when
        sectionDao.deleteById(sectionId);

        //then
        final int afterSize = sectionDao.findSectionsByLineId(lineId).size();

        assertEquals(afterSize, beforeSize - 1);
    }
}
