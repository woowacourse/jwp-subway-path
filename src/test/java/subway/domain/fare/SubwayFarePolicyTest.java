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
class SubwayFarePolicyTest {

    private FarePolicy farePolicy = new SubwayFarePolicy(List.of(
            new BaseFarePolicy(),
            new DistanceFarePolicy(),
            new AgeDiscountFarePolicy()
    ));

    @Test
    void 최종_운임을_계산한다() {
        // given
        final Path path = mock(Path.class);
        given(path.calculateTotalDistance()).willReturn(35);
        given(path.getSectionEdges()).willReturn(List.of(
                generateSectionEdgeStub(100),
                generateSectionEdgeStub(500),
                generateSectionEdgeStub(100)
        ));
        final Passenger passenger = new Passenger(17);

        // when
        final int result = farePolicy.calculate(path, passenger, 0);

        // then
        assertThat(result).isEqualTo(1520);
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
