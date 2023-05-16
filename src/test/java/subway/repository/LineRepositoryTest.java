package subway.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.Line;
import subway.domain.section.Distance;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
class LineRepositoryTest {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineRepository lineRepository;

    private Station A;
    private Station B;
    private Station C;
    private Station D;
    Line savedLine;

    @BeforeEach
    void setUp() {
        // given
        A = stationRepository.save(new Station("A"));
        B = stationRepository.save(new Station("B"));
        C = stationRepository.save(new Station("C"));
        D = stationRepository.save(new Station("D"));

        final Sections sections = new Sections(
                List.of(
                        new Section(A, B, new Distance(1)),
                        new Section(B, C, new Distance(2)),
                        new Section(C, D, new Distance(3))
                )
        );

        final Line line = new Line("1호선", sections);
        savedLine = lineRepository.saveWithSections(line);
    }

    @DisplayName("Sections를 포함한 Line을 저장할 수 있다.")
    @Test
    void saveWithSections() {
        // when, then
        assertAll(
                () -> assertThat(savedLine.getId()).isNotNull(),
                () -> assertThat(savedLine.getName()).isEqualTo("1호선"),
                () -> assertThat(savedLine.sections()).hasSize(3)
        );
    }

    @DisplayName("lineId를 통해 가져온 Line은 Sections 정보를 포함하여 가져올 수 있다.")
    @Test
    void findLineWithSectionsByLineId() {
        // when
        final Optional<Line> lineOptional = lineRepository.findLineWithSectionsByLineId(savedLine.getId());
        final Line line = lineOptional.orElseThrow();

        // then
        assertAll(
                () -> assertThat(line.getId()).isNotNull(),
                () -> assertThat(line.getName()).isEqualTo("1호선"),
                () -> assertThat(line.sections()).hasSize(3)
        );
    }

    @DisplayName("모든 노선을 조회하면 모든 노선의 Sections 정보를 포함하여 가져올 수 있다.")
    @Test
    void findAllWithSections() {
        // given
        final Sections sections = new Sections(
                List.of(
                        new Section(A, B, new Distance(1)),
                        new Section(B, C, new Distance(2)),
                        new Section(C, D, new Distance(3))
                )
        );

        final Line line = new Line("2호선", sections);
        savedLine = lineRepository.saveWithSections(line);

        // when
        final List<Line> lines = lineRepository.findAllWithSections();

        // then
        assertAll(
                () -> assertThat(lines).hasSize(2),
                () -> assertThat(lines.get(0).getId()).isNotNull(),
                () -> assertThat(lines.get(0).getName()).isEqualTo("1호선"),
                () -> assertThat(lines.get(0).sections()).hasSize(3),
                () -> assertThat(lines.get(1).getId()).isNotNull(),
                () -> assertThat(lines.get(1).getName()).isEqualTo("2호선"),
                () -> assertThat(lines.get(1).sections()).hasSize(3)
        );
    }
}
