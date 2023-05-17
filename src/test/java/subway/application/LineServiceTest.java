package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.application.dto.CreationLineDto;
import subway.application.dto.ReadLineDto;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.repository.LineRepository;
import subway.persistence.repository.SectionRepository;

@JdbcTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings({"NonAsciiCharacters", "SpellCheckingInspection"})
class LineServiceTest {

    LineRepository lineRepository;
    SectionRepository sectionRepository;
    LineService lineService;

    @BeforeEach
    void setUp(@Autowired JdbcTemplate jdbcTemplate, @Autowired DataSource dataSource) {
        final LineDao lineDao = new LineDao(jdbcTemplate, dataSource);
        final SectionDao sectionDao = new SectionDao(jdbcTemplate, dataSource);
        final StationDao stationDao = new StationDao(jdbcTemplate, dataSource);

        lineRepository = new LineRepository(lineDao, sectionDao);
        sectionRepository = new SectionRepository(sectionDao, stationDao);

        lineService = new LineService(lineRepository, sectionRepository);
    }

    @Test
    void saveLine_메소드는_line을_저장하고_저장한_데이터를_반환한다() {
        final CreationLineDto actual = lineService.saveLine("12호선", "bg-red-500");

        assertAll(
                () -> assertThat(actual.getId()).isPositive(),
                () -> assertThat(actual.getName()).isEqualTo("12호선"),
                () -> assertThat(actual.getColor()).isEqualTo("bg-red-500")
        );
    }

    @Test
    void saveLine_메소드는_지정한_노선_이름이_이미_존재하는_경우_예외가_발생한다() {
        lineService.saveLine("12호선", "bg-red-500");

        assertThatThrownBy(() -> lineService.saveLine("12호선", "bg-red-500"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지정한 노선의 이름은 이미 존재하는 이름입니다.");
    }

    @Test
    void findAll_메소드는_모든_노선을_반환한다() {
        lineService.saveLine("12호선", "bg-red-500");

        final List<ReadLineDto> actual = lineService.findAllLine();

        assertThat(actual).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    void findLineById_메소드는_저장되어_있는_id를_전달하면_해당_line을_반환한다() {
        final CreationLineDto creationLineDto = lineService.saveLine("12호선", "bg-red-500");

        final ReadLineDto actual = lineService.findLineById(creationLineDto.getId());

        assertAll(
                () -> assertThat(actual.getId()).isPositive(),
                () -> assertThat(actual.getName()).isEqualTo(creationLineDto.getName()),
                () -> assertThat(actual.getColor()).isEqualTo(creationLineDto.getColor())
        );
    }
    
    @Test
    void findLineById_메소드는_없는_id를_전달하면_예외가_발생한다() {
        assertThatThrownBy(() -> lineService.findLineById(-999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 노선입니다.");
    }

    @Test
    void deleteLineById_메소드는_id를_전달하면_해당_id를_가진_line을_삭제한다() {
        assertDoesNotThrow(() -> lineService.deleteLineById(1L));
    }
}
