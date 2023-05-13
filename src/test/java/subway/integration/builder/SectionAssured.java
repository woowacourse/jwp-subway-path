package subway.integration.builder;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.application.request.CreateSectionRequest;
import subway.application.response.SectionResponse;
import subway.integration.support.RestAssuredFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class SectionAssured {

    private SectionAssured() {
    }

    public static CreateSectionRequest 구간_요청(
            final String upStationName,
            final String downStationName,
            final Long lineId,
            final Integer distance
    ) {
        return new CreateSectionRequest(upStationName, downStationName, lineId, distance);
    }

    public static SectionRequestBuilder request() {
        return new SectionRequestBuilder();
    }

    public static class SectionRequestBuilder {

        private ExtractableResponse<Response> response;

        public SectionRequestBuilder 구간을_등록한다(final CreateSectionRequest request) {
            response = RestAssuredFixture.post("/v2/sections", request);
            return this;
        }

        public SectionRequestBuilder 구간을_조회한다(final Long sectionId) {
            response = RestAssuredFixture.get("/v2/sections/" + sectionId);
            return this;
        }

        public SectionResponseBuilder response() {
            return new SectionResponseBuilder(response);
        }
    }

    public static class SectionResponseBuilder {

        private ExtractableResponse<Response> response;

        public SectionResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public <T> T toBody(Class<T> cls) {
            return response.as(cls);
        }

        public void 구간이_조회된다(
                final Long sectionId,
                final Long upStationId,
                final Long downStationId
        ) {
            final SectionResponse response = toBody(SectionResponse.class);

            assertAll(
                    () -> assertThat(response.getId()).isEqualTo(sectionId),
                    () -> assertThat(response.getUpStation().getId()).isEqualTo(upStationId),
                    () -> assertThat(response.getDownStation().getId()).isEqualTo(downStationId)
            );
        }
    }
}
