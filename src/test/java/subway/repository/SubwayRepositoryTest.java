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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.controller.exception.BusinessException;
import subway.domain.Line;
import subway.domain.Station;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SubwayRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SubwayRepository subwayRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM line");
        jdbcTemplate.execute("DELETE FROM station");
    }

    @Test
    void 이름으로_노선을_찾을_수_있다() {
        // given
        final String name = "8호선";
        subwayRepository.registerLine(new Line(name, "분홍색"));

        // when
        final Line line = subwayRepository.findLineByName(name);

        // then
        assertAll(
                () -> assertThat(line.getName()).isEqualTo("8호선"),
                () -> assertThat(line.getColor()).isEqualTo("분홍색")
        );
    }

    @Test
    void 존재하지_않는_노선의_이름을_찾으면_예외가_발생한다() {
        // given
        assertThatThrownBy(() -> subwayRepository.findLineByName("상상역"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("해당 이름을 가진 노선이 존재하지 않습니다.");
    }

    @Test
    void 존재하지_않는_노선의_id를_찾으면_예외가_발생한다() {
        // given
        assertThatThrownBy(() -> subwayRepository.findLineById(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("노선 정보가 잘못되었습니다.");
    }

    @Test
    void 노선을_등록할_수_있다() {
        // when
        subwayRepository.registerLine(new Line("8호선", "분홍색"));

        // then
        final Line line = subwayRepository.findLineByName("8호선");
        assertAll(
                () -> assertThat(line.getName()).isEqualTo("8호선"),
                () -> assertThat(line.getColor()).isEqualTo("분홍색")
        );
    }

    @Test
    void 저장된_역들을_가져올_수_있다() {
        // given
        subwayRepository.registerStation(new Station("잠실역"));
        subwayRepository.registerStation(new Station("석촌역"));
        subwayRepository.registerStation(new Station("송파역"));

        // when
        final List<Station> stations = subwayRepository.findStations();

        // then
        assertThat(stations).hasSize(3);
    }

    @Test
    void id로_노선을_조회할_수_있다() {
        // given
        final Long id = subwayRepository.registerLine(new Line("8호선", "분홍색"));

        // when
        final Line line = subwayRepository.findLineById(id);

        // then
        assertAll(
                () -> assertThat(line.getName()).isEqualTo("8호선"),
                () -> assertThat(line.getColor()).isEqualTo("분홍색")
        );
    }

    @Test
    void 이름으로_역을_조회할_수_있다() {
        // given
        final String name = "잠실역";
        subwayRepository.registerStation(new Station(name));

        // when
        final Station station = subwayRepository.findStationByName(name);

        // then
        assertThat(station.getName()).isEqualTo("잠실역");
    }

    @Test
    void 존재하지_않는_역을_조회하면_예외가_발생한다() {
        // expect
        assertThatThrownBy(() -> subwayRepository.findStationByName("터틀역"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("해당 이름을 가진 역이 존재하지 않습니다.");
    }
}
