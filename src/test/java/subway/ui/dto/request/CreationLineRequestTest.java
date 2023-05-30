package subway.ui.dto.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
class CreationLineRequestTest extends ValidationTest {

    private final String name = "2호선";
    private final String color = "bg-green-500";

    @Test
    void 제대로_된_데이터_형식이_들어오면_성공한다() {
        final CreationLineRequest request = CreationLineRequest.of(name, color);

        final Set<ConstraintViolation<CreationLineRequest>> constraintViolations = validator.validate(request);

        assertThat(constraintViolations.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 노선_이름이_없거나_공백이면_예외를_반환한다(final String inputName) {
        final CreationLineRequest request = CreationLineRequest.of(inputName, color);

        final Set<ConstraintViolation<CreationLineRequest>> constraintViolations = validator.validate(request);

        assertThat(constraintViolations.isEmpty()).isFalse();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 노선_색깔이_없거나_공백이면_예외를_반환한다(final String inputColor) {
        final CreationLineRequest request = CreationLineRequest.of(name, inputColor);

        final Set<ConstraintViolation<CreationLineRequest>> constraintViolations = validator.validate(request);

        assertThat(constraintViolations.isEmpty()).isFalse();
    }
}
