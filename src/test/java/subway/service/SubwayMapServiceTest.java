package subway.service;

import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.section.SectionCreateRequest;
import subway.dto.station.StationResponse;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Transactional
@SpringBootTest
public class SubwayMapServiceTest {

    @Autowired
    private SubwayMapService subwayMapService;

    @Autowired
    private SectionService sectionService;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    @Test
    void 지하철_노선도를_조회한다() {
        lineRepository.save(new Line(2L, "2호선", "초록색"));
        stationRepository.save(new Station("잠실역"));
        stationRepository.save(new Station("아현역"));
        stationRepository.save(new Station("신촌역"));
        sectionService.insertSection(new SectionCreateRequest(2L, "잠실역", "아현역", 5L));
        sectionService.insertSection(new SectionCreateRequest(2L, "잠실역", "신촌역", 3L));

        final List<StationResponse> stationResponses = subwayMapService.showLineMap(2L).getStations();

        final SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(stationResponses.get(0).getName()).isEqualTo("잠실역");
        softAssertions.assertThat(stationResponses.get(1).getName()).isEqualTo("신촌역");
        softAssertions.assertThat(stationResponses.get(2).getName()).isEqualTo("아현역");
        softAssertions.assertAll();
    }
}
