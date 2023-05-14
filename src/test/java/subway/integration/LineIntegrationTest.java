package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.integration.IntegrationTestFixture.노선_생성_요청;
import static subway.integration.IntegrationTestFixture.노선_정보;
import static subway.integration.IntegrationTestFixture.노선_조회_결과;
import static subway.integration.IntegrationTestFixture.단일_노선_조회_요청;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.dto.SaveResponse;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class LineIntegrationTest extends IntegrationTest {

    @Nested
    public class 노선을_추가할_때 {

        @Test
        void 정상적으로_추가된다() {
            ExtractableResponse<Response> response = 노선_생성_요청("2호선", "강남역", "역삼역", 10);

            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(response.header("Location")).isNotEmpty();
        }

        @Test
        void 이미_존재하는_노선의_이름을_추가하면_오류가_발생한다() {
            노선_생성_요청("2호선", "강남역", "역삼역", 10);

            ExtractableResponse<Response> response = 노선_생성_요청("2호선", "역삼역", "잠실역", 10);

            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    public class 단일_노선을_조회할_때 {

        @Test
        void 노선_ID를_받아_상행부터_하행까지의_역을_정렬하여_반환한다() {
            Long 저장된_노선_ID = 노선_생성_요청("2호선", "강남역", "역삼역", 4).as(SaveResponse.class).getId();

            ExtractableResponse<Response> response = 단일_노선_조회_요청(저장된_노선_ID);

            노선_조회_결과(response, 노선_정보("2호선", "강남역", "역삼역"));
        }
    }

}
