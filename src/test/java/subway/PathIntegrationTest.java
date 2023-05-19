package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import subway.dto.InitialSectionCreateRequest;
import subway.dto.SectionCreateRequest;

import static subway.steps.LineSteps.*;
import static subway.steps.StationSteps.*;

public class PathIntegrationTest extends IntegrationTest {

    @Test
    void 최단_경로를_조회할_수_있다() {
        final long station1Id = 역_생성하고_아이디_반환(역_고속터미널);
        final long station2Id = 역_생성하고_아이디_반환(역_사평역);
        final long newStationId = 역_생성하고_아이디_반환(역_새역);

        final long lineId = 노선_생성하고_아이디_반환(노선_9호선);

        노선에_최초의_역_2개_추가_요청(lineId,
                new InitialSectionCreateRequest(
                        lineId, station1Id, station2Id, 3
                ));

        final ExtractableResponse<Response> response = 존재하는_노선에_역_1개_추가_요청(
                lineId,
                new SectionCreateRequest(
                        station1Id, newStationId, 1
                ));
    }
}
