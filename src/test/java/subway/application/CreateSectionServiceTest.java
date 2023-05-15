package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.application.dto.CreateSectionDto;
import subway.application.dto.ReadStationDto;
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
class CreateSectionServiceTest {

    StationRepository stationRepository;
    LineRepository lineRepository;
    SectionRepository sectionRepository;
    CreateSectionService createSectionService;

    Line line;
    Station upStation;
    Station downStation;

    @BeforeEach
    void setUp(@Autowired JdbcTemplate jdbcTemplate, @Autowired DataSource dataSource) {
        final StationDao stationDao = new StationDao(jdbcTemplate, dataSource);
        final SectionDao sectionDao = new SectionDao(jdbcTemplate, dataSource);
        final LineDao lineDao = new LineDao(jdbcTemplate, dataSource);

        stationRepository = new StationRepository(stationDao, sectionDao);
        lineRepository = new LineRepository(lineDao, sectionDao);
        sectionRepository = new SectionRepository(sectionDao, stationDao);
        createSectionService = new CreateSectionService(stationRepository, lineRepository, sectionRepository);

        line = lineRepository.insert(Line.of("12호선", "bg-red-500"));
        upStation = stationRepository.insert(Station.from("12역"));
        downStation = stationRepository.insert(Station.from("23역"));
    }

    @Nested
    class 초기_상태_노선_구간_등록_테스트 {

        @Test
        void addSection_메소드는_유효한_값을_전달하면_section을_생성한다() {
            final CreateSectionDto createSectionDto = createSectionService.addSection(
                    line.getId(), upStation.getId(), downStation.getId(), Direction.DOWN, 5);

            final List<ReadStationDto> actual = createSectionDto.getStationDtos();

            assertAll(
                    () -> assertThat(actual).hasSize(2),
                    () -> assertThat(actual.get(0).getName()).isEqualTo("12역"),
                    () -> assertThat(actual.get(1).getName()).isEqualTo("23역")
            );
        }

        @Test
        void addSection_메소드는_존재하지_않는_stationId를_전달하면_예외가_발생한다() {
            assertThatThrownBy(() -> createSectionService.addSection(line.getId(), -999L, -9999L, Direction.DOWN, 5))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 역입니다.");
        }

        @Test
        void addSection_메소드는_존재하지_않는_lineId를_전달하면_예외가_발생한다() {
            assertThatThrownBy(() -> createSectionService.addSection(-999L, upStation.getId(),
                    downStation.getId(), Direction.DOWN, 5))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 노선입니다.");
        }
    }
    
    @Nested
    class 구간_등록_테스트 {
        
        Station targetStation;
        
        @BeforeEach
        void setUp() {
            line.createSection(upStation, downStation, Distance.from(5), Direction.DOWN);
            sectionRepository.insert(line);
            targetStation = stationRepository.insert(Station.from("34역"));
        }
        
        @Test
        void addSection_메소드는_기존_구간이_존재하는_경우_중간에_section을_생성한다() {
            line.createSection(upStation, targetStation, Distance.from(1), Direction.DOWN);
            sectionRepository.insert(line);

            final Line persistLine = lineRepository.findById(CreateSectionServiceTest.this.line.getId()).get();
            final Line line = sectionRepository.findAllByLine(persistLine);
            final Map<Station, Section> actual = line.getSections().sections();

            assertAll(
                    () -> assertThat(actual.get(upStation).findDistanceByStation(targetStation).distance()).isEqualTo(1),
                    () -> assertThat(actual.get(upStation).findDirectionByStation(targetStation)).isEqualTo(Direction.DOWN),
                    () -> assertThat(actual.get(targetStation).findDistanceByStation(upStation).distance()).isEqualTo(1),
                    () -> assertThat(actual.get(targetStation).findDirectionByStation(upStation)).isEqualTo(Direction.UP),
                    () -> assertThat(actual.get(targetStation).findDistanceByStation(downStation).distance()).isEqualTo(4),
                    () -> assertThat(actual.get(targetStation).findDirectionByStation(downStation)).isEqualTo(Direction.DOWN),
                    () -> assertThat(actual.get(downStation).findDistanceByStation(targetStation).distance()).isEqualTo(4),
                    () -> assertThat(actual.get(downStation).findDirectionByStation(targetStation)).isEqualTo(Direction.UP)
            );
        }

        @Test
        void addSection_메소드는_기존_구간이_존재하지_않는_경우_종점에_section을_추가한다() {
            line.createSection(upStation, targetStation, Distance.from(1), Direction.UP);
            sectionRepository.insert(line);

            final Line persistLine = lineRepository.findById(CreateSectionServiceTest.this.line.getId()).get();
            final Line line = sectionRepository.findAllByLine(persistLine);
            final Map<Station, Section> actual = line.getSections().sections();

            assertAll(
                    () -> assertThat(actual.get(upStation).findDistanceByStation(targetStation).distance()).isEqualTo(1),
                    () -> assertThat(actual.get(upStation).findDirectionByStation(targetStation)).isEqualTo(Direction.UP),
                    () -> assertThat(actual.get(targetStation).findDistanceByStation(upStation).distance()).isEqualTo(1),
                    () -> assertThat(actual.get(targetStation).findDirectionByStation(upStation)).isEqualTo(Direction.DOWN)
            );
        }

        @Test
        void addSection_메소드는_노선에_등록되지_않은_stationId를_기준_역으로_전달하면_예외가_발생한다() {
            assertThatThrownBy(() -> createSectionService.addSection(line.getId(), targetStation.getId(), upStation.getId(), Direction.UP, 1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("지정한 기준 역은 등록되어 있지 않은 역입니다.");
        }

        @Test
        void addSection_메소드는_이미_노선에_등록된_stationId를_추가할_역으로_전달하면_예외가_발생한다() {
            assertThatThrownBy(() -> createSectionService.addSection(line.getId(), upStation.getId(), downStation.getId(), Direction.DOWN, 1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이미 등록된 역입니다.");
        }

        @Test
        void addSection_메소드는_기존_역과_등록할_역이_같으면_예외가_발생한다() {
            assertThatThrownBy(() -> createSectionService.addSection(line.getId(), targetStation.getId(),
                    targetStation.getId(), Direction.DOWN, 1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("노선에 등록할 역은 동일한 역을 지정할 수 없습니다.");
        }

        @Test
        void addSection_메소드는_중간에_section을_추가할_때_구간_사이에_다른_역이_있다면_예외가_발생한다() {
            assertThatThrownBy(() -> line.createSection(upStation, targetStation, Distance.from(100), Direction.DOWN))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("등록되는 구간 중간에 다른 역이 존재합니다.");
        }
    }
}
