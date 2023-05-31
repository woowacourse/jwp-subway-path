package subway.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import subway.application.SubwayService;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dto.AddStationRequest;
import subway.exception.StationNotFoundException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/schema.sql")
public class SubwayServiceStationIntegrationTest extends StationIntegrationTest {

    @Autowired
    StationDao stationDao;

    @Autowired
    LineDao lineDao;

    @Autowired
    SectionDao sectionDao;

    @Autowired
    SubwayService subwayService;

    /**
     * 저장되어 있는 노선 정보
     * 2호선
     * JAMSIL_TO_JAMSILNARU (섹션) : JAMSIL_STATION --(5)--> JAMSIL_NARU_STATION
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    void 역을_추가하는_도중_에러가_발생하면_DB에_저장되지_않는다() {
        long invalidId = 0L;
        AddStationRequest addStationRequest = new AddStationRequest("홍대입구", lineId, upstreamId, invalidId, 2);

        assertSoftly(softly -> {
            softly.assertThatThrownBy(() -> subwayService.addStation(addStationRequest))
                    .isInstanceOf(StationNotFoundException.class);
            softly.assertThat(subwayService.findAllLines().toString()).doesNotContain("홍대입구");
        });
    }

    @Test
    void 역을_추가하면_정상적으로_DB에_저장된다() {
        AddStationRequest addStationRequest = new AddStationRequest("홍대입구", lineId, upstreamId, downstreamId, 2);
        subwayService.addStation(addStationRequest);

        assertThat(subwayService.findAllLines().toString()).contains("홍대입구");
    }
}
