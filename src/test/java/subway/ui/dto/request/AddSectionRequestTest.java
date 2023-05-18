package subway.ui.dto.request;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
class AddSectionRequestTest extends ValidationTest {

    private final Long upStationId = 1L;
    private final Long downStationId = 2L;
    private final int distance = 10;

    @Test
    void 제대로_된_데이터_형식이_들어오면_성공한다() {
        final AddSectionRequest request = AddSectionRequest.of(upStationId, downStationId, distance);

        final Set<ConstraintViolation<AddSectionRequest>> constraintViolations = validator.validate(request);

        assertThat(constraintViolations.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @NullSource
    void 위_방향_지하철_ID가_없으면_에러를_반환한다(final Long inputUpStationId) {
        final AddSectionRequest request = AddSectionRequest.of(inputUpStationId, downStationId, distance);

        final Set<ConstraintViolation<AddSectionRequest>> constraintViolations = validator.validate(request);

        assertThat(constraintViolations.isEmpty()).isFalse();
    }

    @ParameterizedTest
    @NullSource
    void 아래_방향_지하철_ID가_없으면_에러를_반환한다(final Long inputDownStationId) {
        final AddSectionRequest request = AddSectionRequest.of(upStationId, inputDownStationId, distance);

        final Set<ConstraintViolation<AddSectionRequest>> constraintViolations = validator.validate(request);

        assertThat(constraintViolations.isEmpty()).isFalse();
    }

    @ParameterizedTest
    @NullSource
    void 두_역_사이의_거리가_없으면_에러를_반환한다(final Integer inputDistance) {
        final AddSectionRequest request = AddSectionRequest.of(upStationId, downStationId, inputDistance);

        final Set<ConstraintViolation<AddSectionRequest>> constraintViolations = validator.validate(request);

        assertThat(constraintViolations.isEmpty()).isFalse();
    }
}
