package subway.service.section;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.subway.Sections;
import subway.domain.subway.Station;
import subway.dto.section.SectionCreateRequest;
import subway.dto.section.SectionDeleteRequest;
import subway.entity.LineEntity;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;
import subway.service.SectionService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.fixture.SectionsFixture.createSections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/data.sql")
public class SectionServiceIntegrationTest {

    @Autowired
    private SectionService sectionService;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    @Test
    @DisplayName("구간을 삽입한다.")
    void save_section_success() {
        // given
        SectionCreateRequest req = new SectionCreateRequest("2호선", "종합운동장역", "삼성역", 3);

        stationRepository.insertStation(new Station("잠실역"));
        stationRepository.insertStation(new Station("잠실새내역"));
        stationRepository.insertStation(new Station("종합운동장역"));
        stationRepository.insertStation(new Station("삼성역"));

        lineRepository.insertLine(new LineEntity(1L, 2, "2호선", "초록색"));
        lineRepository.updateLine(createSections(), 2);

        // when
        sectionService.insertSection(req);

        // then
        Sections sections = sectionRepository.findSectionsByLineName("2호선");
        assertAll(
                () -> assertThat(sections.getSections().size()).isEqualTo(3),
                () -> assertThat(sections.getSections().get(0).getUpStation().getName()).isEqualTo("잠실역")
        );

    }

    @Test
    @DisplayName("구간을 삭제한다.")
    void delete_section_success() {
        // given
        SectionDeleteRequest req = new SectionDeleteRequest(2, "종합운동장역");

        stationRepository.insertStation(new Station("잠실역"));
        stationRepository.insertStation(new Station("잠실새내역"));
        stationRepository.insertStation(new Station("종합운동장역"));

        lineRepository.insertLine(new LineEntity(1L, 2, "2호선", "초록색"));
        lineRepository.updateLine(createSections(), 2);

        // when
        sectionService.deleteSection(req);

        // then
        Sections sections = sectionRepository.findSectionsByLineName("2호선");
        assertAll(
                () -> assertThat(sections.getSections().size()).isEqualTo(1),
                () -> assertThat(sections.getSections().get(0).getUpStation().getName()).isEqualTo("잠실역")
        );
    }
}
