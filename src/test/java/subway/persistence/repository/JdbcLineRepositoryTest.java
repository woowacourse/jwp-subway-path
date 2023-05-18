package subway.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.domain.fixture.SectionFixtures.포함된_구간들을_검증한다;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.persistence.RepositoryTest;


@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("JdbcLineRepository 은(는)")
@RepositoryTest
class JdbcLineRepositoryTest {

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    private Station 역1;
    private Station 역2;
    private Station 역3;
    private Station 역4;
    private Station 역5;

    @BeforeEach
    void setUp() {
        역1 = new Station(stationRepository.save(new Station("역1")), "역1");
        역2 = new Station(stationRepository.save(new Station("역2")), "역2");
        역3 = new Station(stationRepository.save(new Station("역3")), "역3");
        역4 = new Station(stationRepository.save(new Station("역4")), "역4");
        역5 = new Station(stationRepository.save(new Station("역5")), "역5");
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
        lineRepository.save(new Line("1호선", sections));

        // then
        final List<Line> lines = lineRepository.findAll().getLines();
        assertThat(lines).hasSize(1);
        assertThat(lines.get(0).getSections()).hasSize(4);
    }

    @Test
    void 노선을_업데이트_한다() {
        // given
        final Station 역6 = new Station(stationRepository.save(new Station("역6")), "역6");
        final Sections sections = new Sections(List.of(
                new Section(역1, 역2, 1),
                new Section(역2, 역3, 2),
                new Section(역3, 역4, 3),
                new Section(역4, 역5, 4)
        ));
        lineRepository.save(new Line("1호선", sections));
        final Line line = lineRepository.findByName("1호선").get();
        line.removeStation(역3);
        line.addSection(new Section(역4, 역6, 3));

        // when
        lineRepository.update(line);

        // then
        final List<Section> updated = lineRepository.findByName("1호선").get().getSections();
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
        final Long id = lineRepository.save(new Line("1호선", sections));

        // when
        final List<Section> find = lineRepository.findById(id).get().getSections();

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
        lineRepository.save(new Line("1호선", sections));

        // when
        final List<Section> find = lineRepository.findByName("1호선").get().getSections();

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
        lineRepository.save(new Line("1호선", sections1));

        final Sections sections2 = new Sections(List.of(
                new Section(역3, 역4, 3),
                new Section(역4, 역5, 4)
        ));
        lineRepository.save(new Line("2호선", sections2));

        // when
        final List<Line> lines = lineRepository.findAll().getLines();

        // then
        assertThat(lines).hasSize(2);
        포함된_구간들을_검증한다(lines.get(0).getSections(),
                "역1-[1km]-역2",
                "역2-[2km]-역3"
        );
        포함된_구간들을_검증한다(lines.get(1).getSections(),
                "역3-[3km]-역4",
                "역4-[4km]-역5"
        );
    }
}
