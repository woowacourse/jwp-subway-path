package subway.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.vo.Distance;

@JdbcTest(includeFilters = {
    @Filter(type = FilterType.ANNOTATION, value = Repository.class)
})
class LineRepositoryTest {

    private final Station topStation = new Station(1L, "topStation");
    private final Station midUpStation = new Station(2L, "midUpStation");
    private final Station midDownStation = new Station(3L, "midDownStation");
    private final Station bottomStation = new Station(4L, "bottomStation");
    private final Distance distance = new Distance(10L);
    private final Line line = new Line("lineName", "lineColor", 100L);
    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private SectionRepository sectionRepository;

    @Test
    @DisplayName("line을 저장한다.")
    void testInsert() {
        //given
        //when
        final Line savedLine = lineRepository.save(line);

        //then
        assertThat(lineRepository.findById(savedLine.getId())).isNotNull();
    }

    @Test
    @DisplayName("이름으로 라인을 조회한다.")
    void testFindByName() {
        //given
        final Line savedLine = lineRepository.save(line);
        final Station savedTopStation = stationRepository.save(topStation);
        final Station savedMidUpStation = stationRepository.save(midUpStation);
        final Station savedMidDownStation = stationRepository.save(midDownStation);
        final Station savedBottomStation = stationRepository.save(bottomStation);
        final Section topSection = new Section(savedTopStation, savedMidUpStation, distance);
        final Section midSection = new Section(savedMidUpStation, savedMidDownStation, distance);
        final Section bottomSection = new Section(savedMidDownStation, savedBottomStation, distance);
        final Section savedTopSection = sectionRepository.save(topSection, savedLine.getId());
        final Section savedMidSection = sectionRepository.save(midSection, savedLine.getId());
        final Section savedBottomSection = sectionRepository.save(bottomSection, savedLine.getId());

        //when
        final Optional<Line> result = lineRepository.findByName(savedLine.getName());

        //then
        assertThat(result).isPresent();
        final Line line = result.get();
        assertThat(line.getId()).isEqualTo(savedLine.getId());
        assertThat(line.getColor()).isEqualTo(savedLine.getColor());
        assertThat(line.getName()).isEqualTo(savedLine.getName());
        assertThat(line.getCharge()).isEqualTo(savedLine.getCharge());
        final Sections sections = line.getSections();
        assertThat(sections.size()).isEqualTo(3);
        assertThat(sections.findStation(0)).isEqualTo(savedTopStation);
        assertThat(sections.findStation(1)).isEqualTo(savedMidUpStation);
        assertThat(sections.findStation(2)).isEqualTo(savedMidDownStation);
        assertThat(sections.findStation(3)).isEqualTo(savedBottomStation);
        assertThat(sections.findSection(0).orElseThrow(RuntimeException::new))
            .isEqualTo(savedTopSection);
        assertThat(sections.findSection(1).orElseThrow(RuntimeException::new))
            .isEqualTo(savedMidSection);
        assertThat(sections.findSection(2).orElseThrow(RuntimeException::new))
            .isEqualTo(savedBottomSection);
    }
}
