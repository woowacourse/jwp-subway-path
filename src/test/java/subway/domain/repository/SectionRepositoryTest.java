package subway.domain.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.dao.entity.SectionEntity;
import subway.service.line.domain.Line;
import subway.service.section.domain.Distance;
import subway.service.section.domain.Section;
import subway.service.section.domain.Sections;
import subway.service.section.repository.SectionRepository;
import subway.service.station.domain.Station;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.domain.LineFixture.FIRST_LINE_NO_ID;
import static subway.domain.LineFixture.SECOND_LINE_NO_ID;
import static subway.domain.StationFixture.GANGNAM_NO_ID;
import static subway.domain.StationFixture.JAMSIL_NO_ID;
import static subway.domain.StationFixture.SEONLEUNG_NO_ID;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
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
    void 섹션_추가() {
        Station savedJamsil = stationDao.insert(JAMSIL_NO_ID);
        Station savedSeonleung = stationDao.insert(SEONLEUNG_NO_ID);

        Line savedSecondLine = lineDao.insert(SECOND_LINE_NO_ID);

        Distance seonleungJamsilDistance = new Distance(10);
        Section seonleungJamsilSection = new Section(savedJamsil, savedSeonleung, seonleungJamsilDistance);

        Section savedSection = sectionRepository.insertSection(seonleungJamsilSection, savedSecondLine);
        assertAll(
                () -> assertThat(savedSection.getId()).isPositive(),
                () -> assertThat(savedSection.getUpStation()).isEqualTo(savedJamsil),
                () -> assertThat(savedSection.getDownStation()).isEqualTo(savedSeonleung),
                () -> assertThat(savedSection.getDistance()).isEqualTo(seonleungJamsilDistance)
        );
    }

    @Test
    void 섹션_조회() {
        Station savedJamsil = stationDao.insert(JAMSIL_NO_ID);
        Station savedSeonleung = stationDao.insert(SEONLEUNG_NO_ID);
        Station savedGangnam = stationDao.insert(GANGNAM_NO_ID);

        Line savedSecondLine = lineDao.insert(SECOND_LINE_NO_ID);
        Line savedFirstLine = lineDao.insert(FIRST_LINE_NO_ID);

        SectionEntity seonleungJamsilSectionEntity = new SectionEntity(savedJamsil.getId(), savedSeonleung.getId(), 10, savedSecondLine.getId());
        SectionEntity gangnamJamsilSectionEntity = new SectionEntity(savedSeonleung.getId(), savedGangnam.getId(), 3, savedSecondLine.getId());

        sectionDao.insert(seonleungJamsilSectionEntity);
        sectionDao.insert(gangnamJamsilSectionEntity);

        Sections sectionsByLine = sectionRepository.findSectionsByLine(savedSecondLine);

        Section findSeonleungJamsilSection = sectionsByLine.getSections().stream()
                .filter(section -> section.contains(savedJamsil) && section.contains(savedSeonleung))
                .findAny().get();

        Section findSeonleungGangnamSection = sectionsByLine.getSections().stream()
                .filter(section -> section.contains(savedSeonleung) && section.contains(savedGangnam))
                .findAny().get();


        assertAll(
                () -> assertThat(sectionsByLine.getSections()).hasSize(2),
                () -> assertThat(findSeonleungJamsilSection.getDistance()).isEqualTo(new Distance(10)),
                () -> assertThat(findSeonleungGangnamSection.getDistance()).isEqualTo(new Distance(3)),
                () -> assertThatThrownBy(() -> sectionRepository.findSectionsByLine(savedFirstLine))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("해당 노선에 역이 존재하지 않습니다.")
        );

    }
}
