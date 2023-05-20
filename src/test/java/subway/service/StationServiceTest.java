package subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.controller.exception.StationNotFoundException;
import subway.controller.exception.BusinessException;
import subway.domain.Line;
import subway.domain.Station;
import subway.service.dto.SectionDto;
import subway.service.dto.StationDto;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class StationServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private StationService stationService;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM line");
    }

    @Test
    void 노선에_역을_등록할_수_있다() {
        // given
        final Long lineId = lineRepository.registerLine(new Line("8호선", "분홍색"));

        // when
        stationService.register(new SectionDto(lineId, "잠실역", "석촌역", 10));

        // then
        final List<Station> stations = stationRepository.findStations();
        assertThat(stations).contains(new Station("잠실역"), new Station("석촌역"));
    }

    @Test
    void 존재하지_않는_노선에_등록할_경우_예외가_발생한다() {
        // given
        final SectionDto sectionDto = new SectionDto(1000000L, "잠실역", "석촌역", 10);

        // expect
        assertThatThrownBy(() -> stationService.register(sectionDto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("노선 정보가 잘못되었습니다.");
    }


    @Test
    void 노선에_역을_제거할_수_있다() {
        // given
        final Long lineId = lineRepository.registerLine(new Line("8호선", "분홍색"));
        stationService.register(new SectionDto(lineId, "잠실역", "석촌역", 10));
        stationService.register(new SectionDto(lineId, "석촌역", "송파역", 10));

        // when
        stationService.delete(new StationDto(lineId, "석촌역"));

        // then
        final List<Station> stations = stationRepository.findStations();
        assertThat(stations).contains(new Station("잠실역"), new Station("송파역"));
    }

    @Test
    void 존재하지_않는_역을_제거하면_예외가_발생한다() {
        // given
        final Long id = lineRepository.registerLine(new Line("8호선", "분홍색"));

        // expect
        assertThatThrownBy(() -> stationService.delete(new StationDto(id, "터틀역")))
                .isInstanceOf(StationNotFoundException.class)
                .hasMessageContaining("존재하지 않는 역을 삭제할 수 없습니다.");
    }
}
