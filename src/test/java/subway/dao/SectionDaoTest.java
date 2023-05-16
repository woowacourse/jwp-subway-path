package subway.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import subway.entity.SectionEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ContextConfiguration(classes = SectionDao.class)
public class SectionDaoTest {

    @Autowired
    private SectionDao sectionDao;

    @Test
    public void insert_shouldReturnInsertedSectionWithId() {
        // given
        SectionEntity section = new SectionEntity(null, 1L, 1L, 2L, 10);

        // when
        SectionEntity insertedSection = sectionDao.insert(section);

        // then
        assertThat(insertedSection.getId()).isNotNull();
        assertThat(insertedSection.getLineId()).isEqualTo(1L);
        assertThat(insertedSection.getLeftStationId()).isEqualTo(1L);
        assertThat(insertedSection.getRightStationId()).isEqualTo(2L);
        assertThat(insertedSection.getDistance()).isEqualTo(10);
    }

    @Test
    public void deleteByLineId_shouldDeleteSectionsWithGivenLineId() {
        // given
        SectionEntity section1 = new SectionEntity(null, 1L, 1L, 2L, 10);
        SectionEntity section2 = new SectionEntity(null, 1L, 2L, 3L, 15);
        sectionDao.insert(section1);
        sectionDao.insert(section2);

        // when
        sectionDao.deleteByLineId(1L);

        // then
        assertThat(sectionDao.findByLineId(1L)).isEmpty();
    }

    @Test
    public void findByLineId_shouldReturnSectionsWithGivenLineId() {
        // given
        SectionEntity section1 = new SectionEntity(null, 1L, 1L, 2L, 10);
        SectionEntity section2 = new SectionEntity(null, 1L, 2L, 3L, 15);
        SectionEntity section3 = new SectionEntity(null, 2L, 3L, 4L, 12);
        sectionDao.insert(section1);
        sectionDao.insert(section2);
        sectionDao.insert(section3);

        // when
        List<SectionEntity> sections = sectionDao.findByLineId(1L);

        // then
        assertThat(sections).hasSize(2);
        assertThat(sections.get(0).getLineId()).isEqualTo(1L);
        assertThat(sections.get(0).getLeftStationId()).isEqualTo(1L);
        assertThat(sections.get(0).getRightStationId()).isEqualTo(2L);
        assertThat(sections.get(0).getDistance()).isEqualTo(10);
        assertThat(sections.get(1).getLineId()).isEqualTo(1L);
        assertThat(sections.get(1).getLeftStationId()).isEqualTo(2L);
        assertThat(sections.get(1).getRightStationId()).isEqualTo(3L);
        assertThat(sections.get(1).getDistance()).isEqualTo(15);
    }
}
