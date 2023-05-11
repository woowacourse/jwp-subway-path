package subway.domain.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import subway.domain.*;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.domain.LineFixture.SECOND_LINE_NO_ID;
import static subway.domain.StationFixture.*;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
class SectionRepositoryTest {

    @Autowired
    SectionRepository sectionRepository;

    @Autowired
    SectionDao sectionDao;

    @Autowired
    StationDao stationDao;

    @Autowired
    LineDao lineDao;

    @Test
    void 섹션_조회() {
        Station savedJamsil = stationDao.insert(JAMSIL_NO_ID);
        Station savedSeonleung = stationDao.insert(SEONLEUNG_NO_ID);
        Station savedGangnam = stationDao.insert(GANGNAM_NO_ID);

        Line savedSecondLine = lineDao.insert(SECOND_LINE_NO_ID);

        Section seonleungJamsilSection = new Section(savedJamsil, savedSeonleung, new Distance(10));
        Section gangnamJamsilSection = new Section(savedSeonleung, savedGangnam, new Distance(3));

        sectionDao.insert(seonleungJamsilSection, savedSecondLine);
        sectionDao.insert(gangnamJamsilSection, savedSecondLine);
        Sections sectionsByLine = sectionRepository.findSectionsByLine(savedSecondLine);

        Section findSeonleungJamsilSection = sectionsByLine.getSections().stream()
                .filter(section -> section.contains(savedJamsil) && section.contains(savedSeonleung))
                .findAny().get();

        Section findSeonleungGangnamSection = sectionsByLine.getSections().stream()
                .filter(section -> section.contains(savedSeonleung) && section.contains(savedGangnam))
                .findAny().get();


        Assertions.assertAll(
                () -> assertThat(sectionsByLine.getSections()).hasSize(2),
                () -> assertThat(findSeonleungJamsilSection.getDistance()).isEqualTo(new Distance(10)),
                () -> assertThat(findSeonleungGangnamSection.getDistance()).isEqualTo(new Distance(3))
        );
    }
}
