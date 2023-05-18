package subway.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import subway.application.SubwayService;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dto.PathRequest;
import subway.dto.PathResponse;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.when;
import static subway.SubwayFixture.*;

@SpringBootTest
class SubWayServiceTest {

    @Autowired
    private SubwayService subwayService;

    @DisplayName("두 역(서울역, 종로3가)간 가장 짧은 구간을 계산한다")
    @Test
    void findShortestPathBetweenSeoulyoekAndJongroSAMGA() {
        PathRequest request = new PathRequest(SEOULYEOK.getId(), JONGROSAMGA.getId());
        PathResponse response = subwayService.findShortestPath(request);

        assertSoftly(softly -> {
                    softly.assertThat(response.getSections()).hasSize(8);
                    softly.assertThat(response.getWholeDistance()).isEqualTo(90);
                    softly.assertThat(response.getFare()).isEqualTo(2550);
                }
        );
    }

    @DisplayName("두 역(잠실, 건대)간 가장 짧은 구간을 계산한다")
    @Test
    void findShortestPathBetweenJamsilAndGundae() {
        PathRequest request = new PathRequest(JAMSIL.getId(), GUNDAE.getId());
        PathResponse response = subwayService.findShortestPath(request);

        assertSoftly(softly -> {
                    softly.assertThat(response.getSections()).hasSize(4);
                    softly.assertThat(response.getWholeDistance()).isEqualTo(20);
                    softly.assertThat(response.getFare()).isEqualTo(1450);
                }
        );
    }

    @DisplayName("두 역(잠실, 동작)간 가장 짧은 구간을 계산한다")
    @Test
    void findShortestPathBetweenJamsilAndDongJak() {
        PathRequest request = new PathRequest(JAMSIL.getId(), DONGJAK.getId());
        PathResponse response = subwayService.findShortestPath(request);

        assertSoftly(softly -> {
                    softly.assertThat(response.getSections()).hasSize(6);
                    softly.assertThat(response.getWholeDistance()).isEqualTo(30);
                    softly.assertThat(response.getFare()).isEqualTo(1650);
                }
        );
    }

    @DisplayName("두 역(잠실, 사당)간 가장 짧은 구간을 계산한다")
    @Test
    void findShortestPathBetweenJamsilAndSadang() {
        PathRequest request = new PathRequest(JAMSIL.getId(), SADANG.getId());
        PathResponse response = subwayService.findShortestPath(request);

        assertSoftly(softly -> {
                    softly.assertThat(response.getSections()).hasSize(8);
                    softly.assertThat(response.getWholeDistance()).isEqualTo(40);
                    softly.assertThat(response.getFare()).isEqualTo(1850);
                }
        );
    }
}
