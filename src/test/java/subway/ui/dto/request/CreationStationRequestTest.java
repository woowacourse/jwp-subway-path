package subway.ui.dto.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
class CreationStationRequestTest extends ValidationTest {

    private final String name = "잠실역";

    @Test
    void 제대로_된_데이터_형식이_들어오면_성공한다() {
        final CreationStationRequest request = CreationStationRequest.from(name);

        final Set<ConstraintViolation<CreationStationRequest>> constraintViolations = validator.validate(request);

        assertThat(constraintViolations.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 역_이름이_없거나_공백이면_예외를_반환한다(final String inputName) {
        final CreationStationRequest request = CreationStationRequest.from(inputName);

        final Set<ConstraintViolation<CreationStationRequest>> constraintViolations = validator.validate(request);

        assertThat(constraintViolations.isEmpty()).isFalse();
    }
}
