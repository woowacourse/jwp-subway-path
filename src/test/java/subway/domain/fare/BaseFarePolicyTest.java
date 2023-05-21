package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.domain.core.Section;
import subway.domain.path.Path;
import subway.domain.path.SectionEdge;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class BaseFarePolicyTest {

    final FarePolicy baseFarePolicy = new BaseFarePolicy();

    @Test
    void 기본운임은_입력받은_운임에_1250원을_더한값을_반환한다() {
        // given
        final Path path = mock(Path.class);
        final Passenger passenger = new Passenger(20);

        // when
        final int result = baseFarePolicy.calculate(path, passenger, 0);

        // then
        assertThat(result).isEqualTo(1250);
    }

    @Test
    void 기본운임은_입력받은_운임에_1250원과_노선운임_중_가장높은_값을_더하여_반환한다() {
        // given
        final Path path = mock(Path.class);
        given(path.getSectionEdges()).willReturn(List.of(
                generateSectionEdgeStub(100),
                generateSectionEdgeStub(300),
                generateSectionEdgeStub(100)
        ));
        final Passenger passenger = new Passenger(20);

        // when
        final int result = baseFarePolicy.calculate(path, passenger, 0);

        // then
        assertThat(result).isEqualTo(1550);
    }

    private SectionEdge generateSectionEdgeStub(final int surcharge) {
        return new SectionEdge() {
            @Override
            public Section toSection() {
                return null;
            }

            @Override
            public int getSurcharge() {
                return surcharge;
            }

            @Override
            public long getLineId() {
                return 0;
            }
        };
    }
}
