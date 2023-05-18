package subway.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static subway.TestFixture.GANGNAM;
import static subway.TestFixture.JAMSIL;
import static subway.TestFixture.LINE_3;
import static subway.TestFixture.SECTION_1;
import static subway.TestFixture.SECTION_2;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import subway.domain.Distance;
import subway.domain.section.Section;

@JdbcTest
@Import(SectionDao.class)
class SectionDaoTest {

    @Autowired
    private SectionDao sectionDao;

    @DisplayName("복수개의 Section을 삭제하는 기능 테스트")
    @Test
    void delete() {
        sectionDao.delete(List.of(SECTION_1, SECTION_2));

        final List<Section> emptySections = sectionDao.findByLineId(LINE_3.getId());
        assertThat(emptySections)
                .isEmpty();
    }

    @DisplayName("새로운 section들을 추가하는 기능 테스트")
    @Test
    void insert() {
        final List<Section> sections = List.of(new Section(JAMSIL, GANGNAM, new Distance(1)));

        sectionDao.insert(LINE_3.getId(), sections);

        final List<Section> foundSections = sectionDao.findByLineId(LINE_3.getId());
        assertThat(foundSections)
                .extracting(Section::getPrevStation, Section::getNextStation, Section::getDistance)
                .contains(tuple(JAMSIL, GANGNAM, new Distance(1)));
    }

    @DisplayName("저장된 모든 Section을 조회하는 기능 테스트")
    @Test
    void findAll() {
        final List<Section> all = sectionDao.findAll();

        assertThat(all)
                .extracting(Section::getPrevStation, Section::getNextStation, Section::getDistance)
                .contains(
                        tuple(SECTION_1.getPrevStation(), SECTION_1.getNextStation(), SECTION_1.getDistance()),
                        tuple(SECTION_2.getPrevStation(), SECTION_2.getNextStation(), SECTION_2.getDistance())
                );
    }
}
