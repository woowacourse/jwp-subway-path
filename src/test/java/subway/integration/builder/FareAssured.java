package subway.integration.builder;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.application.request.UpdateLineExpenseRequest;
import subway.application.response.LineExpenseResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.integration.support.RestAssuredFixture.*;

public class FareAssured {

    public static UpdateLineExpenseRequest 노선_추가_비용_정책_수정_데이터(
            final String 추가_비용,
            final Integer 거리당
    ) {
        return new UpdateLineExpenseRequest(추가_비용, 거리당);
    }

    public static LineExpenseResponse 노선_추가_비용_정책_응답_데이터(
            final Long 노선_추가_비용_정책_식별자값,
            final String 추가_비용,
            final Integer 거리당,
            final Long 노선_식별자값
    ) {
        return new LineExpenseResponse(노선_추가_비용_정책_식별자값, 추가_비용, 거리당, 노선_식별자값);
    }

    public static LineExpenseRequestBuilder 클라이언트_요청() {
        return new LineExpenseRequestBuilder();
    }

    public static class LineExpenseRequestBuilder {

        private ExtractableResponse<Response> response;

        public LineExpenseRequestBuilder() {
        }

        public LineExpenseRequestBuilder 노선_추가_비용_정책을_조회한다(final Long 노선_식별자값) {
            response = get("/lines/" + 노선_식별자값 + "/expense");
            return this;
        }

        public LineExpenseRequestBuilder 노선_추가_비용_정책을_수정한다(
                final Long 노선_식별자값,
                final UpdateLineExpenseRequest 노선_추가_비용_정책_수정_데이터
        ) {
            response = patch("/lines/" + 노선_식별자값 + "/expense", 노선_추가_비용_정책_수정_데이터);
            return this;
        }

        public LineExpenseResponseBuilder 서버_응답_검증() {
            return new LineExpenseResponseBuilder(response);
        }
    }

    public static class LineExpenseResponseBuilder {
        private ExtractableResponse<Response> response;

        public LineExpenseResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public <T> T 서버_응답_추출(Class<T> cls) {
            return response.as(cls);
        }

        public LineExpenseResponseBuilder 노선_추가_비용_정책_조회_검증(final LineExpenseResponse 노선_추가_비용_정책_응답) {
            final LineExpenseResponse response = 서버_응답_추출(LineExpenseResponse.class);

            assertAll(
                    () -> assertThat(노선_추가_비용_정책_응답.getPerExpense()).isEqualTo(response.getPerExpense()),
                    () -> assertThat(노선_추가_비용_정책_응답.getPerDistance()).isEqualTo(response.getPerDistance()),
                    () -> assertThat(노선_추가_비용_정책_응답.getLineId()).isEqualTo(response.getLineId())
            );

            return this;
        }
    }
}
