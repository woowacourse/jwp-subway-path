package subway.ui.dto.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
class CreationSectionRequestTest extends ValidationTest {

    private final Long upStationId = 1L;
    private final Long downStationId = 2L;
    private final int distance = 10;

    @Test
    void 제대로_된_데이터_형식이_들어오면_성공한다() {
        final CreationSectionRequest request = CreationSectionRequest.of(upStationId, downStationId, distance);

        final Set<ConstraintViolation<CreationSectionRequest>> constraintViolations = validator.validate(request);

        assertThat(constraintViolations.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @NullSource
    void 위_방향_지하철_ID가_없으면_에러를_반환한다(final Long inputUpStationId) {
        final CreationSectionRequest request = CreationSectionRequest.of(inputUpStationId, downStationId, distance);

        final Set<ConstraintViolation<CreationSectionRequest>> constraintViolations = validator.validate(request);

        assertThat(constraintViolations.isEmpty()).isFalse();
    }

    @ParameterizedTest
    @NullSource
    void 아래_방향_지하철_ID가_없으면_에러를_반환한다(final Long inputDownStationId) {
        final CreationSectionRequest request = CreationSectionRequest.of(upStationId, inputDownStationId, distance);

        final Set<ConstraintViolation<CreationSectionRequest>> constraintViolations = validator.validate(request);

        assertThat(constraintViolations.isEmpty()).isFalse();
    }

    @ParameterizedTest
    @NullSource
    void 두_역_사이의_거리가_없으면_에러를_반환한다(final Integer inputDistance) {
        final CreationSectionRequest request = CreationSectionRequest.of(upStationId, downStationId, inputDistance);

        final Set<ConstraintViolation<CreationSectionRequest>> constraintViolations = validator.validate(request);

        assertThat(constraintViolations.isEmpty()).isFalse();
    }
}
