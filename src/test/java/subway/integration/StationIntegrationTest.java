package subway.integration;


import static subway.integration.IntegrationTestFixture.비정상_요청이라는_응답인지_검증한다;
import static subway.integration.IntegrationTestFixture.역_생성_요청;
import static subway.integration.IntegrationTestFixture.정상_생성이라는_응답인지_검증한다;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class StationIntegrationTest extends IntegrationTest {

    @Nested
    public class 역을_생성할_때 {

        @Test
        void 정상적으로_추가된다() {
            ExtractableResponse<Response> response = 역_생성_요청("강남역");

            정상_생성이라는_응답인지_검증한다(response);
        }

        @Test
        void 이름이_없으면_예외가_발생한다() {
            ExtractableResponse<Response> response = 역_생성_요청("");

            비정상_요청이라는_응답인지_검증한다(response);
        }
    }
}
