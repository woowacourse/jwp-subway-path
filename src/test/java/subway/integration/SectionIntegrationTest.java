package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.adapter.in.web.section.dto.AddStationToLineRequest;
import subway.application.port.in.line.dto.response.LineQueryResponse;
import subway.application.port.in.station.dto.response.StationQueryResponse;
import subway.fixture.LineFixture.이호선;
import subway.fixture.StationFixture.삼성역;
import subway.fixture.StationFixture.역삼역;
import subway.fixture.StationFixture.잠실역;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class SectionIntegrationTest extends IntegrationTest {

    @Test
    void 노선에_역_등록_시나리오_테스트() {
        // 역을 등록한다.
        StationQueryResponse stationResponse1 = addStation(역삼역.REQUEST);
        StationQueryResponse stationResponse2 = addStation(삼성역.REQUEST);
        StationQueryResponse stationResponse3 = addStation(잠실역.REQUEST);

        // 노선을 등록한다.
        LineQueryResponse lineResponse = addLine(이호선.REQUEST);

        // 노선에 역을 등록한다.
        AddStationToLineRequest addStationToLineRequest1 = new AddStationToLineRequest(stationResponse1.getId(),
                stationResponse3.getId(), 5);
        AddStationToLineRequest addStationToLineRequest2 = new AddStationToLineRequest(stationResponse2.getId(),
                stationResponse3.getId(), 2);

        addSection(addStationToLineRequest1, lineResponse.getId());
        addSection(addStationToLineRequest2, lineResponse.getId());

        // 노선을 조회한다.
        LineQueryResponse resultResponse = findLine(lineResponse.getId());

        assertThat(resultResponse.getStations())
                .usingRecursiveComparison()
                .isEqualTo(List.of(stationResponse1, stationResponse2, stationResponse3));
    }

    @Test
    void 노선에서_역_제거_시나리오_테스트() {
        // 역을 등록한다.
        StationQueryResponse stationResponse1 = addStation(역삼역.REQUEST);
        StationQueryResponse stationResponse2 = addStation(삼성역.REQUEST);
        StationQueryResponse stationResponse3 = addStation(잠실역.REQUEST);

        // 노선을 등록한다.
        LineQueryResponse lineQueryResponse = addLine(이호선.REQUEST);

        // 노선에 역을 등록한다.
        AddStationToLineRequest addStationToLineRequest1 = new AddStationToLineRequest(stationResponse1.getId(),
                stationResponse3.getId(), 5);
        AddStationToLineRequest addStationToLineRequest2 = new AddStationToLineRequest(stationResponse2.getId(),
                stationResponse3.getId(), 2);

        addSection(addStationToLineRequest1, lineQueryResponse.getId());
        addSection(addStationToLineRequest2, lineQueryResponse.getId());

        // 노선에 역을 제거한다.
        removeStationFromLine(lineQueryResponse.getId(), stationResponse2.getId());

        // 노선을 조회한다.
        LineQueryResponse resultResponse = findLine(lineQueryResponse.getId());

        assertThat(resultResponse.getStations())
                .usingRecursiveComparison()
                .isEqualTo(List.of(stationResponse1, stationResponse3));
    }

    @Test
    void 노선에_역이_두개일_떄_역_제거_시나리오_테스트() {
        // 역을 등록한다.
        StationQueryResponse stationResponse1 = addStation(역삼역.REQUEST);
        StationQueryResponse stationResponse2 = addStation(삼성역.REQUEST);

        // 노선을 등록한다.
        LineQueryResponse lineQueryResponse = addLine(이호선.REQUEST);

        // 노선에 역을 등록한다.
        AddStationToLineRequest addStationToLineRequest = new AddStationToLineRequest(stationResponse1.getId(),
                stationResponse2.getId(), 5);

        addSection(addStationToLineRequest, lineQueryResponse.getId());

        // 노선에 역을 제거한다.
        removeStationFromLine(lineQueryResponse.getId(), stationResponse2.getId());

        // 노선을 조회한다.
        LineQueryResponse resultResponse = findLine(lineQueryResponse.getId());

        assertThat(resultResponse.getStations()).isEmpty();
    }
}
