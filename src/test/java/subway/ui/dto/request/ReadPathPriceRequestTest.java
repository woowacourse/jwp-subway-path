package subway.ui.dto.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
class ReadPathPriceRequestTest extends ValidationTest {

    private final Long sourceStationId = 1L;
    private final Long targetStationId = 2L;

    @Test
    void 제대로_된_데이터_형식이_들어오면_성공한다() {
        final ReadPathPriceRequest request = ReadPathPriceRequest.of(sourceStationId, targetStationId);

        final Set<ConstraintViolation<ReadPathPriceRequest>> constraintViolations = validator.validate(request);

        assertThat(constraintViolations.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @NullSource
    void 출발역이_없으면_예외를_반환한다(final Long inputSourceStationId) {
        final ReadPathPriceRequest request = ReadPathPriceRequest.of(inputSourceStationId, targetStationId);

        final Set<ConstraintViolation<ReadPathPriceRequest>> constraintViolations = validator.validate(request);

        assertThat(constraintViolations.isEmpty()).isFalse();
    }

    @ParameterizedTest
    @NullSource
    void 도착역이_없으면_예외를_반환한다(final Long inputTargetStationId) {
        final ReadPathPriceRequest request = ReadPathPriceRequest.of(sourceStationId, inputTargetStationId);

        final Set<ConstraintViolation<ReadPathPriceRequest>> constraintViolations = validator.validate(request);

        assertThat(constraintViolations.isEmpty()).isFalse();
    }
}
