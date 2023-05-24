package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.dto.InitialSectionCreateRequest;
import subway.dto.SectionCreateRequest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.steps.LineSteps.*;
import static subway.steps.StationSteps.*;

public class PathIntegrationTest extends IntegrationTest {

    @Test
    void 존재하지_않는_역에_대해_최단_경로_조회를_요청하면_에외가_발생한다() {
        final ExtractableResponse<Response> response = 두_역_사이의_최단_경로_조회_요청(1L, 2L);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 연결되지_않은_두_역에_대해_최단_경로_조회를_요청하면_예외가_발생한다() {
        final long 노선_9호선_아이디 = 노선_생성하고_아이디_반환(노선_9호선);
        final long 고속터미널_아이디 = 역_생성하고_아이디_반환(역_고속터미널);
        final long 사평역_아이디 = 역_생성하고_아이디_반환(역_사평역);

        final long 노선_3호선_아이디 = 노선_생성하고_아이디_반환(노선_3호선);
        final long 교대역_아이디 = 역_생성하고_아이디_반환(역_교대역);
        final long 양재역_아이디 = 역_생성하고_아이디_반환(역_양재역);

        노선에_최초의_역_2개_추가_요청(
                new InitialSectionCreateRequest(
                        노선_9호선_아이디, 고속터미널_아이디, 사평역_아이디, 5
                )
        );

        노선에_최초의_역_2개_추가_요청(
                new InitialSectionCreateRequest(
                        노선_3호선_아이디, 교대역_아이디, 양재역_아이디, 5
                )
        );

        final ExtractableResponse<Response> response = 두_역_사이의_최단_경로_조회_요청(고속터미널_아이디, 양재역_아이디);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 환승_없이_최단_경로를_조회할_수_있다() {
        final long 고속터미널_아이디 = 역_생성하고_아이디_반환(역_고속터미널);
        final long 사평역_아이디 = 역_생성하고_아이디_반환(역_사평역);
        final long 새역_아이디 = 역_생성하고_아이디_반환(역_새역);
        final long 교대역_아이디 = 역_생성하고_아이디_반환(역_교대역);

        final long 노선_9호선_아이디 = 노선_생성하고_아이디_반환(노선_9호선);
        final long 노선_3호선_아이디 = 노선_생성하고_아이디_반환(노선_3호선);

        노선에_최초의_역_2개_추가_요청(
                new InitialSectionCreateRequest(
                        노선_9호선_아이디, 고속터미널_아이디, 사평역_아이디, 3
                ));

        존재하는_노선에_역_1개_추가_요청(
                노선_9호선_아이디,
                new SectionCreateRequest(
                        고속터미널_아이디, 새역_아이디, 2
                ));

        노선에_최초의_역_2개_추가_요청(
                new InitialSectionCreateRequest(
                        노선_3호선_아이디, 고속터미널_아이디, 교대역_아이디, 5
                ));

        final ExtractableResponse<Response> response = 두_역_사이의_최단_경로_조회_요청(고속터미널_아이디, 사평역_아이디);

        AssertionsForClassTypes.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class))
                        .isEqualTo(Arrays.asList(고속터미널_아이디, 새역_아이디, 사평역_아이디)),
                () -> assertThat(response.jsonPath().getLong("price"))
                        .isEqualTo(1_250)
        );
    }

    @Test
    void 환승경로를_포함해_최단_경로를_조회할_수_있다() {
        final long 고속터미널_아이디 = 역_생성하고_아이디_반환(역_고속터미널);
        final long 사평역_아이디 = 역_생성하고_아이디_반환(역_사평역);
        final long 새역_아이디 = 역_생성하고_아이디_반환(역_새역);
        final long 교대역_아이디 = 역_생성하고_아이디_반환(역_교대역);

        final long 노선_9호선_아이디 = 노선_생성하고_아이디_반환(노선_9호선);
        final long 노선_3호선_아이디 = 노선_생성하고_아이디_반환(노선_3호선);

        노선에_최초의_역_2개_추가_요청(
                new InitialSectionCreateRequest(
                        노선_9호선_아이디, 고속터미널_아이디, 사평역_아이디, 8
                ));

        존재하는_노선에_역_1개_추가_요청(
                노선_9호선_아이디,
                new SectionCreateRequest(
                        고속터미널_아이디, 새역_아이디, 3
                ));

        노선에_최초의_역_2개_추가_요청(
                new InitialSectionCreateRequest(
                        노선_3호선_아이디, 고속터미널_아이디, 교대역_아이디, 5
                ));

        final ExtractableResponse<Response> response = 두_역_사이의_최단_경로_조회_요청(교대역_아이디, 사평역_아이디);

        AssertionsForClassTypes.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class))
                        .isEqualTo(List.of(교대역_아이디, 고속터미널_아이디, 새역_아이디, 사평역_아이디)),
                () -> assertThat(response.jsonPath().getLong("price"))
                        .isEqualTo(1_350)
        );
    }

    @Test
    void 나이와_환승경로를_포함해_최단_경로를_조회할_수_있다() {
        final long 고속터미널_아이디 = 역_생성하고_아이디_반환(역_고속터미널);
        final long 사평역_아이디 = 역_생성하고_아이디_반환(역_사평역);
        final long 새역_아이디 = 역_생성하고_아이디_반환(역_새역);
        final long 교대역_아이디 = 역_생성하고_아이디_반환(역_교대역);

        final long 노선_9호선_아이디 = 노선_생성하고_아이디_반환(노선_9호선);
        final long 노선_3호선_아이디 = 노선_생성하고_아이디_반환(노선_3호선);

        노선에_최초의_역_2개_추가_요청(
                new InitialSectionCreateRequest(
                        노선_9호선_아이디, 고속터미널_아이디, 사평역_아이디, 8
                ));

        존재하는_노선에_역_1개_추가_요청(
                노선_9호선_아이디,
                new SectionCreateRequest(
                        고속터미널_아이디, 새역_아이디, 3
                ));

        노선에_최초의_역_2개_추가_요청(
                new InitialSectionCreateRequest(
                        노선_3호선_아이디, 고속터미널_아이디, 교대역_아이디, 5
                ));

        final ExtractableResponse<Response> response = 나이를_포함해_두_역_사이의_최단_경로_조회_요청(교대역_아이디, 사평역_아이디, 13);

        AssertionsForClassTypes.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class))
                        .isEqualTo(List.of(교대역_아이디, 고속터미널_아이디, 새역_아이디, 사평역_아이디)),
                () -> assertThat(response.jsonPath().getLong("price"))
                        .isEqualTo(800)
        );
    }
}
