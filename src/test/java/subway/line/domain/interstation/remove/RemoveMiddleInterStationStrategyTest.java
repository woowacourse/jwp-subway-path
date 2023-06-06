package subway.line.domain.interstation.remove;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.line.domain.interstation.InterStation;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("중간에 역을 제거하는 전략 테스트")
class RemoveMiddleInterStationStrategyTest {

    private List<InterStation> interStations;

    @BeforeEach
    void setUp() {
        interStations = new ArrayList<>(List.of(
                new InterStation(1L, 2L, 10L),
                new InterStation(2L, 3L, 10L),
                new InterStation(3L, 4L, 10L))
        );
    }

    @Nested
    class 중간에_제거가_가능한지_확인하는_기능_테스트 {

        @ParameterizedTest
        @CsvSource(value = {"1, false", "2, true", "3, true", "4, false", "5, false", "6, false"})
        void 제거할_수_있는지_검사한다(long input, boolean expected) {
            //given
            RemoveInterStationStrategy removeInterStationStrategy = new RemoveMiddleInterStationStrategy();

            //when
            boolean result = removeInterStationStrategy.isSatisfied(interStations, input);

            //then
            assertThat(result).isEqualTo(expected);
        }
    }

    @Nested
    class 중간에_잘_제거된다 {

        @Test
        void 중간에_제거되는_경우() {
            //given
            RemoveInterStationStrategy removeInterStationStrategy = new RemoveMiddleInterStationStrategy();

            //when
            removeInterStationStrategy.removeInterStation(interStations, 2L);

            //then
            assertThat(interStations).containsExactly(
                    new InterStation(1L, 3L, 20L),
                    new InterStation(3L, 4L, 10L)
            );
        }
    }
}
