package subway.integration;


import static subway.integration.IntegrationTestFixture.구간_추가_요청;
import static subway.integration.IntegrationTestFixture.노선_생성_요청;
import static subway.integration.IntegrationTestFixture.반환값이_없는지_검증한다;
import static subway.integration.IntegrationTestFixture.비정상_요청이라는_응답인지_검증한다;
import static subway.integration.IntegrationTestFixture.역_삭제_요청;
import static subway.integration.IntegrationTestFixture.역_생성_요청;
import static subway.integration.IntegrationTestFixture.정상_생성이라는_응답인지_검증한다;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.dto.LineSaveResponse;
import subway.dto.StationAddResponse;

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

    @Nested
    public class 역을_삭제할_때 {

        @Test
        void 정상적으로_삭제된다() {
            Long 저장된_역_ID = 역_생성_요청("강남역").as(StationAddResponse.class).getId();

            ExtractableResponse<Response> response = 역_삭제_요청(저장된_역_ID);

            반환값이_없는지_검증한다(response);
        }

        @Test
        void 이미_구간에_추가되어_있는_역을_삭제하면_예외가_발생한다() {
            Long 저장된_노선_ID = 노선_생성_요청("2호선", "강남역", "역삼역", 10).as(LineSaveResponse.class).getId();
            Long 저장된_역_ID = 역_생성_요청("잠실역").as(StationAddResponse.class).getId();
            구간_추가_요청(저장된_노선_ID, "잠실역", "역삼역", 2);

            ExtractableResponse<Response> response = 역_삭제_요청(저장된_역_ID);

            비정상_요청이라는_응답인지_검증한다(response);
        }
    }
}
