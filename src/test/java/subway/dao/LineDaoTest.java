package subway.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.LineRepository;
import subway.line.domain.section.application.SectionService;
import subway.line.domain.station.application.StationRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
class LineDaoTest {
    @Autowired
    LineRepository lineRepository;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    SectionService sectionService;

    @Test
    @DisplayName("어떤 노선의 상행 종점역을 찾을 수 있다.")
    void head() {
        // given
        final var line = lineRepository.makeLine("1호선", "blue");
        final var stationS = stationRepository.insert("송탄");

        lineRepository.updateHeadStation(line, stationS);

        // when
        final var updatedLine = lineRepository.findById(line.getId());
        assertThat(updatedLine.getHead()).isEqualTo(stationS);
    }

    @Test
    @DisplayName("어떤 노선에 상행 종점역이 등록되어 있다면, 노선 조회시 해당 역 정보가 함께 들어있다.")
    void findHead() {
        // given
        final var line = lineRepository.makeLine("1호선", "blue");
        final var stationS = stationRepository.insert("송탄");
        lineRepository.updateHeadStation(line, stationS);

        // when
        final var foundLine = lineRepository.findById(line.getId());
        assertThat(foundLine.getHead()).isEqualTo(stationS);
    }

    @Test
    @DisplayName("어떤 노선에 아직 상행 종점역이 등록되지 않은 상태라면, 노선 조회시 역 정보는 null로 지정되어 있다.")
    void notFoundHead() {
        // given
        final var line = lineRepository.makeLine("1호선", "blue");

        // when
        assertThat(line.getHead()).isNull();
    }
}