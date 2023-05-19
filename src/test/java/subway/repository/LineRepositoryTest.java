package subway.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.repository.DomainFixtures.강남_역삼_거리5_구간;
import static subway.repository.DomainFixtures.광화문_민트_거리5_구간;
import static subway.repository.DomainFixtures.교대_강남_거리10_구간;
import static subway.repository.DomainFixtures.명동_광화문_거리7_구간;
import static subway.repository.DomainFixtures.민트_서울_거리5_구간;
import static subway.repository.DomainFixtures.박스터_교대_거리2_구간;
import static subway.repository.DomainFixtures.서울_명동_거리10_구간;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.Subway;
import subway.repository.dao.LineDao;
import subway.repository.dao.SectionDao;
import subway.repository.dao.StationDao;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class LineRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private LineRepository lineRepository;

    @BeforeEach
    void setUp() {
        StationDao stationDao = new StationDao(jdbcTemplate);
        lineRepository = new LineRepository(new SectionDao(jdbcTemplate), new LineDao(jdbcTemplate),
                stationDao);
    }

    @Test
    void 노선을_저장한다() {
        // given
        List<Section> sections = List.of(교대_강남_거리10_구간, 강남_역삼_거리5_구간);
        Line line = new Line(null, "2호선", sections);

        // when
        Long saveId = lineRepository.save(line);

        // then
        assertThat(saveId).isPositive();
    }

    @Test
    void 전체_노선을_조회한다() {
        // given
        List<Section> firstSections = List.of(서울_명동_거리10_구간, 명동_광화문_거리7_구간, 민트_서울_거리5_구간);
        Line firstLine = new Line("1호선", firstSections);
        List<Section> secondSections = List.of(교대_강남_거리10_구간, 강남_역삼_거리5_구간, 박스터_교대_거리2_구간);
        Line secondLine = new Line(null, "2호선", secondSections);
        lineRepository.save(firstLine);
        lineRepository.save(secondLine);

        // when
        List<Line> lines = lineRepository.findAll();

        // then
        assertThat(lines).hasSize(2);
        assertThat(lines).flatExtracting(Line::getStations)
                .map(Station::getName)
                .contains(
                        "서울역",
                        "명동역",
                        "광화문역",
                        "민트역",
                        "교대역",
                        "강남역",
                        "역삼역",
                        "박스터역"
                );
    }

    @Test
    void ID를_기준으로_노선을_조회한다() {
        // given
        List<Section> sections = List.of(교대_강남_거리10_구간, 강남_역삼_거리5_구간);
        final Line line = new Line("2호선", sections);
        final Long saveId = lineRepository.save(line);

        // when
        final Line findLine = lineRepository.findById(saveId);

        // then
        assertThat(findLine.getName()).isEqualTo(line.getName());
    }

    @Test
    void 노선의_하행종점을_삭제한다() {
        // given
        List<Section> sections = List.of(
                서울_명동_거리10_구간,
                명동_광화문_거리7_구간,
                광화문_민트_거리5_구간
        );
        Line line = new Line(null, "1호선", sections);
        final Long lineId = lineRepository.save(line);

        final Subway subway = new Subway(lineRepository.findAll());
        final Line saveLine = subway.findLineById(lineId);
        final int lineSize = saveLine.getStations().size();
        final Station lastStation = saveLine.getStations().get(lineSize - 1);

        // when
        lineRepository.deleteStationByLineIdAndStationId(saveLine.getId(), lastStation.getId());
        final Line findLine = lineRepository.findById(lineId);

        // then
        assertThat(findLine.getSections()).hasSize(2);
        assertThat(findLine.getStations()).flatMap(Station::getName)
                .containsExactly("서울역", "명동역", "광화문역");
    }

    @Test
    void 노선의_상행종점을_삭제한다() {
        // given
        List<Section> sections = List.of(
                서울_명동_거리10_구간,
                명동_광화문_거리7_구간,
                광화문_민트_거리5_구간
        );

        Line line = new Line(null, "1호선", sections);
        final Long lineId = lineRepository.save(line);

        final Subway subway = new Subway(lineRepository.findAll());
        final Line saveLine = subway.findLineById(lineId);
        final Station firstStation = saveLine.getStations().get(0);

        // when
        lineRepository.deleteStationByLineIdAndStationId(saveLine.getId(), firstStation.getId());
        final Line findLine = lineRepository.findById(lineId);

        // then
        assertThat(findLine.getSections()).hasSize(2);
        assertThat(findLine.getStations()).flatMap(Station::getName)
                .containsExactly("명동역", "광화문역", "민트역");
    }

    @Test
    void 노선의_중간_역을_삭제한다() {
        // given
        List<Section> sections = List.of(
                서울_명동_거리10_구간,
                명동_광화문_거리7_구간,
                광화문_민트_거리5_구간
        );
        Line line = new Line(null, "1호선", sections);
        final Long lineId = lineRepository.save(line);

        final Subway subway = new Subway(lineRepository.findAll());
        final Line saveLine = subway.findLineById(lineId);
        final Station secondStation = saveLine.getStations().get(1);

        // when
        lineRepository.deleteStationByLineIdAndStationId(saveLine.getId(), secondStation.getId());
        final Line findLine = lineRepository.findById(lineId);

        // then
        assertThat(findLine.getSections()).hasSize(2);
        assertThat(findLine.getStations()).flatMap(Station::getName)
                .containsExactly("서울역", "광화문역", "민트역");
    }
}
