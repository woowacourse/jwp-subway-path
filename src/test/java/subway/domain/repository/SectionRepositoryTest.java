package subway.domain.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.service.line.domain.Line;
import subway.service.section.domain.Distance;
import subway.service.section.domain.Section;
import subway.service.section.domain.Sections;
import subway.service.section.repository.SectionRepository;
import subway.service.station.domain.Station;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.domain.LineFixture.EIGHT_LINE_NO_ID;
import static subway.domain.LineFixture.SECOND_LINE_NO_ID;
import static subway.domain.StationFixture.GANGNAM_NO_ID;
import static subway.domain.StationFixture.JAMSIL_NO_ID;
import static subway.domain.StationFixture.SEOKCHON;
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

        Section jamsilToSeonleung = new Section(savedJamsil, savedSeonleung, new Distance(10));
        Section gangnamToSeonleung = new Section(savedSeonleung, savedGangnam, new Distance(3));

        sectionRepository.insertSection(jamsilToSeonleung, savedSecondLine);
        sectionRepository.insertSection(gangnamToSeonleung, savedSecondLine);

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
                () -> assertThat(findSeonleungGangnamSection.getDistance()).isEqualTo(new Distance(3))
        );

    }

    @Test
    void 섹션_삭제() {
        // given
        Station savedJamsil = stationDao.insert(JAMSIL_NO_ID);
        Station savedSeonleung = stationDao.insert(SEONLEUNG_NO_ID);
        Station savedGangnam = stationDao.insert(GANGNAM_NO_ID);

        Line savedSecondLine = lineDao.insert(SECOND_LINE_NO_ID);
        Line savedEightLine = lineDao.insert(EIGHT_LINE_NO_ID);

        Section seonleungToJamsilSection = new Section(savedJamsil, savedSeonleung, new Distance(10));
        Section gangnamToSeonleungSection = new Section(savedSeonleung, savedGangnam, new Distance(5));

        Section eightLineSection = new Section(savedJamsil, savedGangnam, new Distance(7));


        Section savedSeonleungToJamsilSection = sectionRepository.insertSection(seonleungToJamsilSection, savedSecondLine);
        Section savedGangnamToSeonleungSection = sectionRepository.insertSection(gangnamToSeonleungSection, savedSecondLine);
        Section savedSectionInEightLine = sectionRepository.insertSection(eightLineSection, savedEightLine);

        //when
        sectionRepository.deleteSection(savedSeonleungToJamsilSection);

        Sections sectionsByLine = sectionRepository.findSectionsByLine(savedSecondLine);

        //then
        assertAll(
                () -> assertThat(sectionsByLine.getSections()).doesNotContain(savedSectionInEightLine),
                () -> assertThat(sectionsByLine.getSections()).doesNotContain(savedSeonleungToJamsilSection),
                () -> assertThat(sectionsByLine.getSections()).contains(savedGangnamToSeonleungSection),
                () -> assertThat(sectionsByLine.getSections()).hasSize(1)
        );

    }

    @Test
    void 호선_상관없이_지하철_역과_연결된_역_조회() {
        // given
        Station savedJamsil = stationDao.insert(JAMSIL_NO_ID);
        Station savedSeonleung = stationDao.insert(SEONLEUNG_NO_ID);
        Station savedGangnam = stationDao.insert(GANGNAM_NO_ID);
        Station savedSeokchon = stationDao.insert(SEOKCHON);

        Line savedSecondLine = lineDao.insert(SECOND_LINE_NO_ID);
        Line savedEightLine = lineDao.insert(EIGHT_LINE_NO_ID);

        Section seonleungToJamsilSection = new Section(savedJamsil, savedSeonleung, new Distance(10));
        Section gangnamToSeonleungSection = new Section(savedSeonleung, savedGangnam, new Distance(5));

        Section seokchonToJamsil = new Section(savedJamsil, savedSeokchon, new Distance(7));

        sectionRepository.insertSection(seonleungToJamsilSection, savedSecondLine);
        sectionRepository.insertSection(seokchonToJamsil, savedEightLine);
        sectionRepository.insertSection(gangnamToSeonleungSection, savedSecondLine);

        // when
        Map<Line, Sections> sectionsPerLine = sectionRepository.findSectionsByStation(savedJamsil);

        // 2호선 역 아이디 추출
        Sections secondSections = sectionsPerLine.get(savedSecondLine);

        List<Long> secondUpStations = secondSections.getSections().stream()
                .map(section -> section.getUpStation().getId())
                .collect(Collectors.toList());

        List<Long> secondDownStations = secondSections.getSections().stream()
                .map(section -> section.getDownStation().getId())
                .collect(Collectors.toList());

        secondUpStations.addAll(secondDownStations);
        List<Long> secondDistinctIds = secondUpStations.stream()
                .distinct()
                .collect(Collectors.toList());

        // 8호선 역 아이디 추출
        Sections eightSections = sectionsPerLine.get(savedEightLine);
        List<Long> eightUpStations = eightSections.getSections().stream()
                .map(section -> section.getUpStation().getId())
                .collect(Collectors.toList());

        List<Long> eightDownStations = eightSections.getSections().stream()
                .map(section -> section.getDownStation().getId())
                .collect(Collectors.toList());

        eightUpStations.addAll(eightDownStations);
        List<Long> eightDistinctIds = eightUpStations.stream()
                .distinct()
                .collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(sectionsPerLine.keySet()).containsExactlyInAnyOrder(savedEightLine, savedSecondLine),
                () -> assertThat(secondSections.getSections()).hasSize(1),
                () -> assertThat(secondDistinctIds).containsExactlyInAnyOrder(savedJamsil.getId(), savedSeonleung.getId()),
                () -> assertThat(eightDistinctIds).containsExactlyInAnyOrder(savedJamsil.getId(), savedSeokchon.getId())
        );
    }
}
