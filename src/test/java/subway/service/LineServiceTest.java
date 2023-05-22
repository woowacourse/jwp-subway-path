package subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.Line;
import subway.domain.Subway;
import subway.service.dto.LineDto;
import subway.service.dto.SectionDto;
import subway.dto.response.LineResponse;
import subway.repository.LineRepository;
import subway.repository.SubwayRepository;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class LineServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private LineService lineService;

    @Autowired
    private StationService stationService;

    @Autowired
    private SubwayRepository subwayRepository;

    @Autowired
    private LineRepository lineRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM line");
    }

    @Test
    void 노선을_등록할_수_있다() {
        // given
        final LineDto lineDto = new LineDto("8호선", "분홍색");

        // when
        lineService.register(lineDto);

        // then
        final Subway subway = subwayRepository.findSubway();
        assertThat(subway.getLines()).contains(new Line("8호선", "분홍색"));
    }

    @Test
    void 존재하는_노선_이름으로_등록시_예외가_발생한다() {
        // given
        lineRepository.registerLine(new Line("8호선", "분홍색"));

        // expect
        assertThatThrownBy(() -> lineService.register(new LineDto("8호선", "분홍색")))
                .isInstanceOf(DuplicateKeyException.class)
                .hasMessageContaining("해당 이름의 노선이 이미 존재합니다.");
    }

    @Test
    void id로_노선의_역들을_조회할_수_있다() {
        // given
        final Long lineId = lineService.register(new LineDto("8호선", "분홍색"));
        stationService.register(new SectionDto(lineId, "잠실역", "석촌역", 10));

        // when
        final LineResponse lineResponse = lineService.read(lineId);

        // then
        assertAll(
                () -> assertThat(lineResponse.getName()).isEqualTo("8호선"),
                () -> assertThat(lineResponse.getColor()).isEqualTo("분홍색"),
                () -> assertThat(lineResponse.getStations()).hasSize(2)
        );
    }

    @Test
    void 존재하지_않는_노선_조회시_예외가_발생한다() {
        // given
        final Long lineId = lineService.register(new LineDto("8호선", "분홍색"));
        stationService.register(new SectionDto(lineId, "잠실역", "석촌역", 10));

        // expect
        assertThatThrownBy(() -> lineService.read(100000L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("노선 정보가 잘못되었습니다.");
    }

    @Test
    void 모든_노선을_조회할_수_있다() {
        // given
        final Long eightLineId = lineService.register(new LineDto("8호선", "분홍색"));
        final Long twoLineId = lineService.register(new LineDto("2호선", "초록색"));

        stationService.register(new SectionDto(eightLineId, "잠실역", "석촌역", 10));
        stationService.register(new SectionDto(twoLineId, "잠실역", "신천역", 10));

        // when
        final List<LineResponse> lineResponses = lineService.readAll();

        // then
        assertAll(
                () -> assertThat(lineResponses).hasSize(2),
                () -> assertThat(lineResponses.get(0).getName()).contains("8호선"),
                () -> assertThat(lineResponses.get(0).getStations()).containsExactly("잠실역", "석촌역"),
                () -> assertThat(lineResponses.get(0).getColor()).contains("분홍색"),
                () -> assertThat(lineResponses.get(1).getName()).contains("2호선"),
                () -> assertThat(lineResponses.get(1).getStations()).containsExactly("잠실역", "신천역"),
                () -> assertThat(lineResponses.get(1).getColor()).contains("초록색")
        );
    }
}
