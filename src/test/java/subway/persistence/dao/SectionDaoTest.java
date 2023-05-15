package subway.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static subway.integration.IntegrationFixture.GANGNAM;
import static subway.integration.IntegrationFixture.JAMSIL;
import static subway.integration.IntegrationFixture.LINE_2;
import static subway.integration.IntegrationFixture.LINE_3;
import static subway.integration.IntegrationFixture.SAMSUNG;
import static subway.integration.IntegrationFixture.SECTION_1;
import static subway.integration.IntegrationFixture.SECTION_2;
import static subway.integration.IntegrationFixture.SEONGLENUG;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import subway.domain.Distance;
import subway.domain.Section;

@JdbcTest
@Import(SectionDao.class)
class SectionDaoTest {

    @Autowired
    private SectionDao sectionDao;

    @DisplayName("복수개의 Section을 삭제하는 기능 테스트")
    @Test
    void delete() {
        sectionDao.delete(List.of(SECTION_1, SECTION_2));

        final List<Section> emptySections = sectionDao.findByLineId(LINE_2.getId());
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

    @DisplayName("lineId로 List<Section>을 반환하는 기능 테스트")
    void findByLineId() {
        final List<Section> foundSections = sectionDao.findByLineId(LINE_3.getId());

        assertThat(foundSections)
                .extracting(Section::getPrevStation, Section::getNextStation, Section::getDistance)
                .contains(
                        tuple(GANGNAM, SEONGLENUG, new Distance(5)),
                        tuple(SEONGLENUG, SAMSUNG, new Distance(5))
                );
    }
}
