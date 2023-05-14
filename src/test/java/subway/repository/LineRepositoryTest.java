package subway.repository;

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

@JdbcTest(includeFilters = {
    @Filter(type = FilterType.ANNOTATION, value = Repository.class)
})
class LineRepositoryTest {

    private final Station topStation = new Station(1L, "topStation");
    private final Station midUpStation = new Station(2L, "midUpStation");
    private final Station midDownStation = new Station(3L, "midDownStation");
    private final Station bottomStation = new Station(4L, "bottomStation");
    private final long distance = 10L;
    private final Section topSection = new Section(1L, topStation, midUpStation, distance);
    private final Section midSection = new Section(2L, midUpStation, midDownStation, distance);
    private final Section bottomSection = new Section(3L, midDownStation, bottomStation, distance);
    private final Line line = new Line("lineName", "lineColor");
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
    @DisplayName("")
    void testFindByName() {
        //given
        final Line savedLine = lineRepository.save(line);
        final Station topStation = stationRepository.save(this.topStation);
        final Station midUpStation = stationRepository.save(this.midUpStation);
        final Station midDownStation = stationRepository.save(this.midDownStation);
        final Station bottomStation = stationRepository.save(this.bottomStation);
        final Section topSection = sectionRepository.save(this.topSection, savedLine.getId());
        final Section midSection = sectionRepository.save(this.midSection, savedLine.getId());
        final Section bottomSection = sectionRepository.save(this.bottomSection, savedLine.getId());

        //when
        final Optional<Line> result = lineRepository.findByName(savedLine.getName());

        //then
        assertThat(result).isPresent();
        final Line line = result.get();
        assertThat(line.getId()).isEqualTo(savedLine.getId());
        assertThat(line.getColor()).isEqualTo(savedLine.getColor());
        assertThat(line.getName()).isEqualTo(savedLine.getName());
        final Sections sections = line.getSections();
        assertThat(sections.size()).isEqualTo(3);
        assertThat(sections.findStation(0)).isEqualTo(topStation);
        assertThat(sections.findStation(1)).isEqualTo(midUpStation);
        assertThat(sections.findStation(2)).isEqualTo(midDownStation);
        assertThat(sections.findStation(3)).isEqualTo(bottomStation);
        assertThat(sections.findSection(0)).isEqualTo(topSection);
        assertThat(sections.findSection(1)).isEqualTo(midSection);
        assertThat(sections.findSection(2)).isEqualTo(bottomSection);
    }
}
