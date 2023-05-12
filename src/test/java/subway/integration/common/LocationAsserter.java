package subway.integration.common;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@SuppressWarnings("NonAsciiCharacters")
public class LocationAsserter {

    public static void location_헤더를_검증한다(final ExtractableResponse<Response> response, final Long id) {
        final String location = response.header("location");
        assertThat(location.substring(location.lastIndexOf("/") + 1))
                .isEqualTo(id.toString());
    }
}
