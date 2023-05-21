package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.dto.SectionDto;
import subway.dao.entity.SectionEntity;

@JdbcTest
@Sql("/section_initialize.sql")
@ActiveProfiles("test")
class SectionDaoTest {
    private static final long lineId = 1L;
    @Autowired
    JdbcTemplate jdbcTemplate;

    SectionDao sectionDao;


    @BeforeEach
    void setUp() {
        this.sectionDao = new SectionDao(jdbcTemplate);
    }

    @Test
    @DisplayName("노선의 구간 갯수가 정확하게 반환되어야 한다.")
    void countByLineId_success() {
        // given
        sectionDao.insert(new SectionEntity(lineId, 1L, 2L, 1));
        sectionDao.insert(new SectionEntity(lineId, 2L, 3L, 1));

        // when
        Long count = sectionDao.countByLineId(lineId);

        // then
        assertThat(count)
                .isEqualTo(2L);
    }

    @Test
    @DisplayName("노선에 구간이 없으면 갯수가 0이어야 한다.")
    void countByLineId_emptySection() {
        // when
        Long count = sectionDao.countByLineId(lineId);

        // then
        assertThat(count)
                .isZero();
    }

    @Test
    @DisplayName("노선의 수정이 정상적으로 되어야 한다.")
    void update_success() {
        // given
        SectionEntity sectionEntity = new SectionEntity(lineId, 1L, 2L, 1);
        long savedId = sectionDao.insert(sectionEntity);
        sectionEntity = new SectionEntity(savedId, lineId, 1L, 2L, 1);
        sectionEntity.updateDistance(3);
        sectionEntity.updateEndStationId(3L);
        sectionEntity.updateStartStationId(2L);

        // when
        sectionDao.update(sectionEntity);

        List<SectionDto> sections = sectionDao.findAllSectionsWithStationNameByLineId(lineId);

        List<SectionEntity> sectionEntities = List.of(sectionEntity);
        assertThat(sections).usingRecursiveComparison()
                .ignoringFields("startStationName", "endStationName")
                .isEqualTo(sectionEntities);
    }

    @ParameterizedTest
    @CsvSource(value = {"1:true", "2:false", "3:true"}, delimiter = ':')
    @DisplayName("주어진 역이 구간에 있는지 확인한다.")
    void existsByStartStationNameAndLineId(Long stationId, boolean exists) {
        // given
        sectionDao.insert(new SectionEntity(lineId, 1L, 3L, 1));
        // when
        boolean expect = sectionDao.isStationInLineById(lineId, stationId);

        // then
        assertThat(expect)
                .isEqualTo(exists);
    }

    @Test
    @DisplayName("노선에 구간이 비어있는지 확인한다.")
    void isEmptyByLineId_true() {
        // expect
        assertThat(sectionDao.isEmptyByLineId(lineId))
                .isTrue();
    }

    @Test
    @DisplayName("노선에 구간이 있는지 확인한다.")
    void isEmptyByLineId_false() {
        // given
        sectionDao.insert(new SectionEntity(lineId, 1L, 2L, 1));

        // expect
        assertThat(sectionDao.isEmptyByLineId(lineId))
                .isFalse();
    }

    @Test
    @DisplayName("노선에 속해있는 구간 역들의 이름을 조회한다.")
    void findAllSectionNamesByLineId_success() {
        // given
        long savedId1 = sectionDao.insert(new SectionEntity(lineId, 1L, 2L, 1));
        long savedId2 = sectionDao.insert(new SectionEntity(lineId, 2L, 3L, 1));

        // when
        List<SectionDto> sections = sectionDao.findAllSectionsWithStationNameByLineId(lineId);

        // then
        assertThat(sections).usingRecursiveComparison()
                .isEqualTo(List.of(
                        new SectionDto(savedId1, 1L, 2L, "삼성역", "선릉역", 1),
                        new SectionDto(savedId2, 2L, 3L, "선릉역", "역삼역", 1)
                ));
    }

    @Test
    @DisplayName("구간이 삭제되면 더 이상 조회되지 않는다.")
    void deleteSection_success() {
        // given
        Long savedId1 = sectionDao.insert(new SectionEntity(lineId, 1L, 2L, 1));
        Long savedId2 = sectionDao.insert(new SectionEntity(lineId, 2L, 3L, 1));

        // when
        sectionDao.deleteById(savedId1);

        // then
        assertThat(sectionDao.findAllSectionsWithStationNameByLineId(lineId))
                .usingRecursiveComparison()
                .ignoringFields("startStationName", "endStationName")
                .isEqualTo(List.of(new SectionEntity(savedId2, lineId, 2L, 3L, 1)));
    }

    @Test
    @DisplayName("모든 노선에 존재하는 전체 구간을 조회한다.")
    void findAllSections_inEveryLine() {
        // given
        Long savedId1 = sectionDao.insert(new SectionEntity(lineId, 1L, 2L, 1));
        Long savedId2 = sectionDao.insert(new SectionEntity(lineId, 2L, 3L, 1));
        Long savedId3 = sectionDao.insert(new SectionEntity(2L, 3L, 4L, 1));
        Long savedId4 = sectionDao.insert(new SectionEntity(2L, 4L, 5L, 1));

        // when
        List<SectionDto> sectionsWithStationName = sectionDao.findAllSectionsWithStationName();

        // then
        assertThat(sectionsWithStationName)
                .usingRecursiveComparison()
                .isEqualTo(List.of(
                        new SectionDto(savedId1, 1L, 2L, "삼성역", "선릉역", 1),
                        new SectionDto(savedId2, 2L, 3L, "선릉역", "역삼역", 1),
                        new SectionDto(savedId3, 3L, 4L, "역삼역", "강남역", 1),
                        new SectionDto(savedId4, 4L, 5L, "강남역", "교대역", 1)
                ));
    }

}
