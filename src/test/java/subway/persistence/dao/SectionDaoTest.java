package subway.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.persistence.entity.SectionEntity;

@JdbcTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings({"NonAsciiCharacters", "SpellCheckingInspection"})
class SectionDaoTest {

    SectionDao sectionDao;

    @BeforeEach
    void setUp(@Autowired JdbcTemplate jdbcTemplate, @Autowired DataSource dataSource) {
        sectionDao = new SectionDao(jdbcTemplate, dataSource);
    }

    @Test
    void insert_메소드는_section을_저장하고_저장한_데이터를_반환한다() {
        final SectionEntity sectionEntity = SectionEntity.Builder
                .builder()
                .lineId(1L)
                .upStationId(1L)
                .downStationId(2L)
                .distance(5)
                .build();

        final SectionEntity actual = sectionDao.insert(sectionEntity);

        assertAll(
                () -> assertThat(actual.getId()).isPositive(),
                () -> assertThat(actual.getLineId()).isEqualTo(sectionEntity.getLineId()),
                () -> assertThat(actual.getUpStationId()).isEqualTo(sectionEntity.getUpStationId()),
                () -> assertThat(actual.getDownStationId()).isEqualTo(sectionEntity.getDownStationId()),
                () -> assertThat(actual.getDistance()).isEqualTo(sectionEntity.getDistance())
        );
    }

    @Test
    void insertAll_메소드는_전달한_모든_section을_저장한다() {
        final List<SectionEntity> sectionEntities = new ArrayList<>();
        final SectionEntity firstSectionEntity = SectionEntity.Builder
                .builder()
                .lineId(1L)
                .upStationId(1L)
                .downStationId(2L)
                .distance(5)
                .build();
        final SectionEntity secondSectionEntity = SectionEntity.Builder
                .builder()
                .lineId(1L)
                .upStationId(2L)
                .downStationId(3L)
                .distance(5)
                .build();
        sectionEntities.add(firstSectionEntity);
        sectionEntities.add(secondSectionEntity);

        assertDoesNotThrow(() -> sectionDao.insertAll(sectionEntities));
    }

    @Test
    void findAllByLineId_메소드는_lineId를_전달하면_해당하는_모든_section을_반환한다() {
        final SectionEntity sectionEntity = SectionEntity.Builder
                .builder()
                .lineId(1L)
                .upStationId(1L)
                .downStationId(2L)
                .distance(5)
                .build();
        sectionDao.insert(sectionEntity);

        final List<SectionEntity> actual = sectionDao.findAllByLineId(sectionEntity.getLineId());

        assertThat(actual).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    void findAllByStationId_메소드는_stationId를_전달하면_해당하는_모든_section을_반환한다() {
        final SectionEntity sectionEntity = SectionEntity.Builder
                .builder()
                .lineId(1L)
                .upStationId(1L)
                .downStationId(2L)
                .distance(5)
                .build();
        sectionDao.insert(sectionEntity);

        final List<SectionEntity> actual = sectionDao.findAllByStationId(sectionEntity.getUpStationId());

        assertThat(actual).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    void deleteAllByLineId_메소드는_stationId를_전달하면_해당하는_모든_section을_삭제한다() {
        final SectionEntity sectionEntity = SectionEntity.Builder
                .builder()
                .lineId(1L)
                .upStationId(1L)
                .downStationId(2L)
                .distance(5)
                .build();
        sectionDao.insert(sectionEntity);

        assertDoesNotThrow(() -> sectionDao.deleteAllByLineId(sectionEntity.getLineId()));
    }
}
