package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.DistanceInvalidException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static subway.factory.SectionFactory.createSection;

class SectionTest {

    @Test
    @DisplayName("역이 존재하는지 확인한다.")
    void returns_exist_station() {
        // given
        Section section = createSection();

        // when
        boolean result = section.isExistStation(new Station("삼성역"));

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("요청한 역 사이의 거리가 현재 거리보다 같거나 길면 예외가 발생한다.")
    void throws_exception_when_invalid_distance() {
        // given
        Section section = createSection();
        Long requestDistance = 10L;

        // when & then
        assertThatThrownBy(() -> section.validateDistance(requestDistance))
                .isInstanceOf(DistanceInvalidException.class);
    }
}
