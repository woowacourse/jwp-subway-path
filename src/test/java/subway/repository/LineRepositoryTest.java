package subway.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.exception.DuplicateException;
import subway.exception.ErrorMessage;
import subway.exception.NotFoundException;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Sql("/truncate.sql")
@JdbcTest
class LineRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private LineRepository lineRepository;
    private StationRepository stationRepository;
    private SectionRepository sectionRepository;

    @BeforeEach
    void setUp() {
        StationDao stationDao = new StationDao(jdbcTemplate);
        SectionDao sectionDao = new SectionDao(jdbcTemplate);
        LineDao lineDao = new LineDao(jdbcTemplate);
        stationRepository = new StationRepository(stationDao);
        sectionRepository = new SectionRepository(sectionDao, stationDao);
        lineRepository = new LineRepository(lineDao, sectionDao, stationDao);
    }

    @Test
    void 노선을_입력받아_저장한다() {
        // given
        Line 노선 = Line.createWithoutId("2호선", List.of());

        // when
        Long 노선_ID = lineRepository.save(노선);
        Line 찾은_노선 = lineRepository.findById(노선_ID);

        // then
        assertThat(노선_ID).isEqualTo(찾은_노선.getId());
    }

    @Test
    void 존재하는_노선을_저장하면_예외가_발생한다() {
        // given
        Line 노선 = Line.createWithoutId("2호선", List.of());
        lineRepository.save(노선);

        // then
        assertThatThrownBy(() -> lineRepository.save(노선))
                .isInstanceOf(DuplicateException.class)
                .hasMessage(ErrorMessage.DUPLICATE_LINE.getErrorMessage());
    }

    @Test
    void 존재하지_않는_노선_ID로_노선을_조회하면_예외가_발생한다() {
        // given
        Long 존재하지_않는_노선_ID = Long.MAX_VALUE;

        // then
        assertThatThrownBy(() -> lineRepository.findById(존재하지_않는_노선_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorMessage.NOT_FOUND_LINE.getErrorMessage());
    }

    @Test
    void 모든_노선을_조회한다() {
        // given
        Long 노선_ID = lineRepository.save(Line.createWithoutId("2호선", List.of()));

        Station 시작역 = Station.createWithoutId("잠실역");
        Station 도착역 = Station.createWithoutId("잠실새내역");
        Long 시작역_ID = stationRepository.save(시작역);
        Long 도착역_ID = stationRepository.save(도착역);

        Station ID가_있는_시작역 = new Station(시작역_ID, 시작역.getName());
        Station ID가_있는_도착역 = new Station(도착역_ID, 도착역.getName());

        Section 구간 = Section.of(ID가_있는_시작역, ID가_있는_도착역, 20);
        sectionRepository.save(노선_ID, 구간);
        lineRepository.save(Line.createWithoutId("3호선", List.of()));

        // when
        List<Line> 노선들 = lineRepository.findAll().getLines();

        // then
        assertAll(
                () -> assertThat(노선들).hasSize(2),
                () -> assertThat(노선들).extracting(Line::getName).containsExactly("2호선", "3호선"),
                () -> assertThat(노선들).extracting(Line::getStations)
                        .containsExactly(List.of(ID가_있는_시작역, ID가_있는_도착역), List.of())
        );
    }

    @Test
    void 노선_ID를_입력받아_노선을_반환한다() {
        // given
        Long 노선_ID = lineRepository.save(Line.createWithoutId("2호선", List.of()));

        Station 시작역 = Station.createWithoutId("잠실역");
        Station 도착역 = Station.createWithoutId("잠실새내역");
        Long 시작역_ID = stationRepository.save(시작역);
        Long 도착역_ID = stationRepository.save(도착역);

        Station ID가_있는_시작역 = new Station(시작역_ID, 시작역.getName());
        Station ID가_있는_도착역 = new Station(도착역_ID, 도착역.getName());

        Section 구간 = Section.of(ID가_있는_시작역, ID가_있는_도착역, 20);
        sectionRepository.save(노선_ID, 구간);

        // when
        Line 찾은_노선 = lineRepository.findById(노선_ID);

        // then
        assertAll(
                () -> assertThat(찾은_노선.getId()).isEqualTo(노선_ID),
                () -> assertThat(찾은_노선.getSectionsByList()).extracting(Section::getUpStation)
                        .containsExactly(ID가_있는_시작역),
                () -> assertThat(찾은_노선.getSectionsByList()).extracting(Section::getDownStation)
                        .containsExactly(ID가_있는_도착역)
        );
    }

    @Test
    void 존재하지_않는_노선_ID로_노선을_조회하는_경우_예외가_발생한다() {
        // given
        Long 존재하지_않는_ID = Long.MAX_VALUE;

        // then
        assertThatThrownBy(() -> lineRepository.findById(존재하지_않는_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorMessage.NOT_FOUND_LINE.getErrorMessage());
    }
}
