package subway.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import subway.persistence.repository.H2StationsInLineRepository;
import subway.service.line.LineRepository;
import subway.service.line.domain.Line;
import subway.service.section.domain.Distance;
import subway.service.section.domain.Section;
import subway.service.section.domain.Sections;
import subway.service.station.StationRepository;
import subway.service.station.domain.Station;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.fixture.LineFixture.EIGHT_LINE_NO_ID;
import static subway.fixture.LineFixture.SECOND_LINE_NO_ID;
import static subway.fixture.StationFixture.GANGNAM_NO_ID;
import static subway.fixture.StationFixture.JAMSIL_NO_ID;
import static subway.fixture.StationFixture.SEOKCHON;
import static subway.fixture.StationFixture.SEONLEUNG_NO_ID;
import static subway.fixture.StationFixture.YUKSAM_NO_ID;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
@Sql("/schema-test.sql")
class H2StationsInLineRepositoryTest {

    @Autowired
    H2StationsInLineRepository sectionRepository;
    @Autowired
    StationRepository stationRepository;

    @Autowired
    LineRepository lineRepository;

    Station savedJamsil;
    Station savedSeonleung;
    Station savedGangnam;

    Line savedSecondLine;
    Line savedEightLine;

    @BeforeEach
    public void setUp() {
        savedJamsil = stationRepository.insert(JAMSIL_NO_ID);
        savedSeonleung = stationRepository.insert(SEONLEUNG_NO_ID);
        savedGangnam = stationRepository.insert(GANGNAM_NO_ID);

        savedSecondLine = lineRepository.insert(SECOND_LINE_NO_ID);
        savedEightLine = lineRepository.insert(EIGHT_LINE_NO_ID);
    }

    @Test
    void 섹션_추가() {
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
        Station savedSeokchon = stationRepository.insert(SEOKCHON);

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

    @Test
    void 해당_라인의_구간이_하나_남아있으면_true() {
        Distance seonleungJamsilDistance = new Distance(10);
        Section seonleungJamsilSection = new Section(savedJamsil, savedSeonleung, seonleungJamsilDistance);

        sectionRepository.insertSection(seonleungJamsilSection, savedSecondLine);
        assertThat(sectionRepository.isLastSectionInLine(savedSecondLine)).isTrue();
    }

    @Test
    void 해당_라인의_구간이_둘_이상이면_false() {
        Station savedYuksam = stationRepository.insert(YUKSAM_NO_ID);

        Distance seonleungJamsilDistance = new Distance(10);
        Section seonleungJamsilSection = new Section(savedJamsil, savedSeonleung, seonleungJamsilDistance);
        Section seonleungYuksamSection = new Section(savedYuksam, savedSeonleung, new Distance(3));

        sectionRepository.insertSection(seonleungJamsilSection, savedSecondLine);
        sectionRepository.insertSection(seonleungYuksamSection, savedSecondLine);
        assertThat(sectionRepository.isLastSectionInLine(savedSecondLine)).isFalse();
    }

    @Test
    void 모든_섹션_조회() {
        Station savedYuksam = stationRepository.insert(YUKSAM_NO_ID);

        Distance seonleungJamsilDistance = new Distance(10);
        Section seonleungJamsilSection = new Section(savedJamsil, savedSeonleung, seonleungJamsilDistance);
        Section seonleungYuksamSection = new Section(savedYuksam, savedSeonleung, new Distance(3));

        Section savedSeonleungJamsil = sectionRepository.insertSection(seonleungJamsilSection, savedSecondLine);
        Section savedSeonleungYuksam = sectionRepository.insertSection(seonleungYuksamSection, savedEightLine);
        assertThat(sectionRepository.findAll()).containsExactlyInAnyOrder(savedSeonleungJamsil, savedSeonleungYuksam);
    }
}
