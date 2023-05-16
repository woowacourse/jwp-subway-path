package subway.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.Station;
import subway.dto.SectionDto;
import subway.dto.StationDto;
import subway.repository.SubwayRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class StationServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private StationService stationService;

    @Autowired
    private SubwayRepository subwayRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM line");
    }

    @Test
    void 노선에_역을_등록할_수_있다() {
        // given
        final Long lineId = subwayRepository.registerLine("8호선", "분홍색");

        // when
        stationService.register(new SectionDto(lineId, "잠실역", "석촌역", 10));

        // then
        final List<Station> stations = subwayRepository.findStations();
        assertThat(stations).contains(new Station("잠실역"), new Station("석촌역"));
    }

    @Test
    void 노선에_역을_제거할_수_있다() {
        // given
        final Long lineId = subwayRepository.registerLine("8호선", "분홍색");
        stationService.register(new SectionDto(lineId, "잠실역", "석촌역", 10));
        stationService.register(new SectionDto(lineId, "석촌역", "송파역", 10));

        // when
        stationService.delete(new StationDto(lineId, "석촌역"));

        // then
        final List<Station> stations = subwayRepository.findStations();
        assertThat(stations).contains(new Station("잠실역"), new Station("송파역"));
    }
}
