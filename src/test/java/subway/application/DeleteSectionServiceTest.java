package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.line.Line;
import subway.domain.section.Direction;
import subway.domain.section.Distance;
import subway.domain.section.Section;
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
class DeleteSectionServiceTest {

    StationRepository stationRepository;
    LineRepository lineRepository;
    SectionRepository sectionRepository;
    DeleteSectionService deleteSectionService;

    Line line;
    Station upStation;
    Station middleStation;
    Station downStation;

    @BeforeEach
    void setUp(@Autowired JdbcTemplate jdbcTemplate, @Autowired DataSource dataSource) {
        final StationDao stationDao = new StationDao(jdbcTemplate, dataSource);
        final SectionDao sectionDao = new SectionDao(jdbcTemplate, dataSource);
        final LineDao lineDao = new LineDao(jdbcTemplate, dataSource);

        stationRepository = new StationRepository(stationDao, sectionDao);
        lineRepository = new LineRepository(lineDao, sectionDao);
        sectionRepository = new SectionRepository(sectionDao, stationDao);
        deleteSectionService = new DeleteSectionService(stationRepository, lineRepository, sectionRepository);

        final Line persistLine = lineRepository.insert(Line.of("12호선", "bg-red-500"));
        upStation = stationRepository.insert(Station.from("12역"));
        middleStation = stationRepository.insert(Station.from("23역"));
        downStation = stationRepository.insert(Station.from("34역"));

        persistLine.createSection(upStation, middleStation, Distance.from(5), Direction.DOWN);
        persistLine.createSection(middleStation, downStation, Distance.from(5), Direction.DOWN);
        sectionRepository.insert(persistLine);
        line = sectionRepository.findAllByLine(persistLine);
    }

    @Test
    void removeSection_메소드는_중간_역을_지정하면_지정한_역을_삭제하고_남은_두_역을_연결한다() {
        deleteSectionService.removeSection(middleStation.getId(), line.getId());

        final Line persistLine = lineRepository.findById(this.line.getId()).get();
        final Map<Station, Section> actual = sectionRepository.findAllByLine(persistLine).getSections().sections();

        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual.get(upStation).findDistanceByStation(downStation).distance()).isEqualTo(10),
                () -> assertThat(actual.get(upStation).findDirectionByStation(downStation)).isEqualTo(Direction.DOWN),
                () -> assertThat(actual.get(downStation).findDistanceByStation(upStation).distance()).isEqualTo(10),
                () -> assertThat(actual.get(downStation).findDirectionByStation(upStation)).isEqualTo(Direction.UP)
        );
    }

    @Test
    void removeSection_메소드는_중간_역이_아닌_경우_종점_역을_삭제한다() {
        deleteSectionService.removeSection(downStation.getId(), line.getId());

        final Line persistLine = lineRepository.findById(this.line.getId()).get();
        final Map<Station, Section> actual = sectionRepository.findAllByLine(persistLine).getSections().sections();

        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual.get(upStation).findDistanceByStation(middleStation).distance()).isEqualTo(5),
                () -> assertThat(actual.get(upStation).findDirectionByStation(middleStation)).isEqualTo(Direction.DOWN),
                () -> assertThat(actual.get(middleStation).findDistanceByStation(upStation).distance()).isEqualTo(5),
                () -> assertThat(actual.get(middleStation).findDirectionByStation(upStation)).isEqualTo(Direction.UP)
        );
    }

    @Test
    void removeSection_메소드는_역이_두_개만_존재할_때_역을_삭제하면_구간_전체를_삭제한다() {
        deleteSectionService.removeSection(downStation.getId(), line.getId());

        deleteSectionService.removeSection(middleStation.getId(), line.getId());

        final Line persistLine = lineRepository.findById(this.line.getId()).get();
        final Map<Station, Section> actual = sectionRepository.findAllByLine(persistLine).getSections().sections();

        assertThat(actual).isEmpty();
    }

    @Test
    void removeSection_메소드는_구간이_존재하지_않는_lineId를_전달하면_예외가_발생한다() {
        final Line persistLine = lineRepository.insert(Line.of("23호선", "bg-red-500"));

        assertThatThrownBy(() -> deleteSectionService.removeSection(middleStation.getId(), persistLine.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 역은 구간이 존재하지 않습니다.");
    }

    @Test
    void removeSection_메소드는_존재하지_않는_lineId를_전달하면_예외가_발생한다() {
        assertThatThrownBy(() -> deleteSectionService.removeSection(middleStation.getId(), -999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 노선입니다.");
    }

    @Test
    void removeSection_메소드는_존재하지_않는_stationId를_전달하면_예외가_발생한다() {
        assertThatThrownBy(() -> deleteSectionService.removeSection(-999L, line.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 역입니다.");
    }

    @Test
    void removeSection_메소드는_해당_노선에_등록하지_않은_stationId를_전달하면_예외가_발생한다() {
        final Station targetStation = stationRepository.insert(Station.from("45역"));

        assertThatThrownBy(() -> deleteSectionService.removeSection(targetStation.getId(), line.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 역은 노선에 등록되어 있지 않습니다.");
    }
}
