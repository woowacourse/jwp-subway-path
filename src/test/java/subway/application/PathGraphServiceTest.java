package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.application.dto.ShortestPathInfoDto;
import subway.domain.fare.DistanceFarePolicy;
import subway.domain.line.Line;
import subway.domain.path.ShortestPathCalculator;
import subway.domain.section.Direction;
import subway.domain.section.Distance;
import subway.domain.station.Station;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.repository.LineRepository;
import subway.persistence.repository.SectionRepository;
import subway.persistence.repository.StationRepository;

@JdbcTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings({"NonAsciiCharacters", "SpellCheckingInspection"})
class PathGraphServiceTest {

    StationRepository stationRepository;
    LineRepository lineRepository;
    SectionRepository sectionRepository;
    PathService pathService;

    @BeforeEach
    void setUp(@Autowired JdbcTemplate jdbcTemplate, @Autowired DataSource dataSource) {
        final StationDao stationDao = new StationDao(jdbcTemplate, dataSource);
        final SectionDao sectionDao = new SectionDao(jdbcTemplate, dataSource);
        final LineDao lineDao = new LineDao(jdbcTemplate, dataSource);
        final ShortestPathCalculator calculator = new JGraphtShortestPathCalculator();
        final DistanceFarePolicy farePolicy = new DistanceFarePolicy();

        stationRepository = new StationRepository(stationDao, sectionDao);
        lineRepository = new LineRepository(lineDao, sectionDao);
        sectionRepository = new SectionRepository(sectionDao, stationDao);
        pathService = new PathService(lineRepository, sectionRepository, stationRepository, calculator, farePolicy);
    }

    @Test
    void findShortestPathInfo_메소드는_출발_역과_도착_역을_전달하면_최단_경로를_반환한다() {
        final Station first = stationRepository.insert(Station.from("첫번째역"));
        final Station second = stationRepository.insert(Station.from("두번째역"));
        final Station third = stationRepository.insert(Station.from("세번째역"));
        final Station fourth = stationRepository.insert(Station.from("네번째역"));
        final Station fifth = stationRepository.insert(Station.from("다섯번째역"));
        final Station sixth = stationRepository.insert(Station.from("여섯번째역"));
        final Station seventh = stationRepository.insert(Station.from("일곱번째역"));
        final Station eighth = stationRepository.insert(Station.from("여덟번째역"));
        final Station nineth = stationRepository.insert(Station.from("아홉번째역"));

        final Line firstLine = lineRepository.insert(Line.of("일호선", "bg-red-500"));

        firstLine.createSection(first, second, Distance.from(80), Direction.DOWN);
        firstLine.createSection(second, third, Distance.from(80), Direction.DOWN);
        firstLine.createSection(third, fourth, Distance.from(80), Direction.DOWN);
        firstLine.createSection(fourth, fifth, Distance.from(80), Direction.DOWN);
        firstLine.createSection(fifth, nineth, Distance.from(80), Direction.DOWN);

        sectionRepository.insert(firstLine);

        final Line secondLine = lineRepository.insert(Line.of("이호선", "bg-blue-500"));

        secondLine.createSection(second, sixth, Distance.from(1), Direction.DOWN);
        secondLine.createSection(sixth, seventh, Distance.from(1), Direction.DOWN);
        secondLine.createSection(seventh, eighth, Distance.from(1), Direction.DOWN);
        secondLine.createSection(eighth, fifth, Distance.from(1), Direction.DOWN);

        sectionRepository.insert(secondLine);

        final ShortestPathInfoDto actual = pathService.findShortestPathInfo(first.getId(), nineth.getId());

        assertAll(
                () -> assertThat(actual.getPathDtos()).hasSize(3),
                () -> assertThat(actual.getPathDtos().get(0).getStations()).hasSize(2),
                () -> assertThat(actual.getPathDtos().get(0).getPathDistance()).isEqualTo(80),
                () -> assertThat(actual.getPathDtos().get(1).getStations()).hasSize(5),
                () -> assertThat(actual.getPathDtos().get(1).getPathDistance()).isEqualTo(4),
                () -> assertThat(actual.getPathDtos().get(2).getStations()).hasSize(2),
                () -> assertThat(actual.getPathDtos().get(2).getPathDistance()).isEqualTo(80),
                () -> assertThat(actual.getTotalDistance()).isEqualTo(164),
                () -> assertThat(actual.getFare()).isEqualTo(3550)
        );
    }

    @Test
    void findShortestPathInfo_메소드는_동일한_출발_역과_도착_역의_id를_전달하면_예외가_발생한다() {
        assertThatThrownBy(() -> pathService.findShortestPathInfo(1L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("출발 역과 도착 역이 동일할 수 없습니다.");
    }

    @Test
    void findShortestPathInfo_메소드는_존재하지_않는_역의_id를_전달하면_예외가_발생한다() {
        assertThatThrownBy(() -> pathService.findShortestPathInfo(-999L, -998L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 역입니다.");
    }
}
