package subway.line.infrastructure.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.line.domain.fixture.SectionFixtures.포함된_구간들을_검증한다;
import static subway.line.domain.fixture.StationFixture.역1;
import static subway.line.domain.fixture.StationFixture.역2;
import static subway.line.domain.fixture.StationFixture.역3;
import static subway.line.domain.fixture.StationFixture.역4;
import static subway.line.domain.fixture.StationFixture.역5;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subway.common.RepositoryTest;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.line.domain.Section;
import subway.line.domain.Sections;
import subway.line.domain.Station;
import subway.line.domain.StationRepository;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("JdbcLineRepository 은(는)")
@RepositoryTest
class JdbcLineRepositoryTest {

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        stationRepository.save(역1);
        stationRepository.save(역2);
        stationRepository.save(역3);
        stationRepository.save(역4);
        stationRepository.save(역5);
    }

    @Test
    void 노선과_노선에_속한_구간을_저장한다() {
        // given
        final Sections sections = new Sections(List.of(
                new Section(역1, 역2, 1),
                new Section(역2, 역3, 2),
                new Section(역3, 역4, 3),
                new Section(역4, 역5, 4)
        ));

        // when
        lineRepository.save(new Line("1호선", 0, sections));

        // then
        final List<Line> lines = lineRepository.findAll();
        assertThat(lines).hasSize(1);
        assertThat(lines.get(0).sections()).hasSize(4);
    }

    @Test
    void 노선을_업데이트_한다() {
        // given
        final Station 역6 = new Station("역6");
        stationRepository.save(역6);
        final Sections sections = new Sections(List.of(
                new Section(역1, 역2, 1),
                new Section(역2, 역3, 2),
                new Section(역3, 역4, 3),
                new Section(역4, 역5, 4)
        ));
        lineRepository.save(new Line("1호선", 0, sections));
        final Line line = lineRepository.findByName("1호선").get();
        line.removeStation(역3);
        line.addSection(new Section(역4, 역6, 3));

        // when
        lineRepository.update(line);

        // then
        final List<Section> updated = lineRepository.findByName("1호선").get().sections();
        포함된_구간들을_검증한다(updated,
                "역1-[1km]-역2",
                "역2-[5km]-역4",
                "역4-[3km]-역6",
                "역6-[1km]-역5"
        );
    }

    @Test
    void ID로_단일_노선을_조회한다() {
        // given
        final Sections sections = new Sections(List.of(
                new Section(역1, 역2, 1),
                new Section(역2, 역3, 2),
                new Section(역3, 역4, 3),
                new Section(역4, 역5, 4)
        ));
        final Line line = new Line("1호선", 0, sections);
        lineRepository.save(line);

        // when
        final List<Section> find = lineRepository.findById(line.id()).get().sections();

        // then
        포함된_구간들을_검증한다(find,
                "역1-[1km]-역2",
                "역2-[2km]-역3",
                "역3-[3km]-역4",
                "역4-[4km]-역5"
        );
    }

    @Test
    void 이름으로_단일_노선을_조회한다() {
        // given
        final Sections sections = new Sections(List.of(
                new Section(역1, 역2, 1),
                new Section(역2, 역3, 2),
                new Section(역3, 역4, 3),
                new Section(역4, 역5, 4)
        ));
        lineRepository.save(new Line("1호선", 0, sections));

        // when
        final List<Section> find = lineRepository.findByName("1호선").get().sections();

        // then
        포함된_구간들을_검증한다(find,
                "역1-[1km]-역2",
                "역2-[2km]-역3",
                "역3-[3km]-역4",
                "역4-[4km]-역5"
        );
    }

    @Test
    void 모든_노선을_조회한다() {
        // given
        final Sections sections1 = new Sections(List.of(
                new Section(역1, 역2, 1),
                new Section(역2, 역3, 2)
        ));
        lineRepository.save(new Line("1호선", 0, sections1));

        final Sections sections2 = new Sections(List.of(
                new Section(역3, 역4, 3),
                new Section(역4, 역5, 4)
        ));
        lineRepository.save(new Line("2호선", 0, sections2));

        // when
        final List<Line> lines = lineRepository.findAll();

        // then
        assertThat(lines).hasSize(2);
        포함된_구간들을_검증한다(lines.get(0).sections(),
                "역1-[1km]-역2",
                "역2-[2km]-역3"
        );
        포함된_구간들을_검증한다(lines.get(1).sections(),
                "역3-[3km]-역4",
                "역4-[4km]-역5"
        );
    }
}
