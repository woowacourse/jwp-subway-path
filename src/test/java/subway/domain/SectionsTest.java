package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.path.TestDomainData;

import static org.assertj.core.api.Assertions.assertThat;

class SectionsTest {

    @Test
    @DisplayName("거리 합 계산 테스트")
    void getTotalDistance_test() {
        // given
        final TestDomainData data = TestDomainData.create();
        final Sections sections = new Sections(data.getSections());

        // when
        final Distance totalDistance = sections.getTotalDistance();

        // then
        assertThat(totalDistance).isEqualTo(new Distance(22));
    }
}
