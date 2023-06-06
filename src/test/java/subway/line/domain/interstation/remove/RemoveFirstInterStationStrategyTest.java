package subway.line.domain.interstation.remove;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.line.domain.interstation.InterStation;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("처음에 역을 제거하는 전략 테스트")
class RemoveFirstInterStationStrategyTest {

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
    class 처음에_제거가_가능한지_확인하는_기능_테스트 {

        @Test
        void 처음에_제거되는_경우() {
            //given
            RemoveInterStationStrategy removeInterStationStrategy = new RemoveFirstInterStationStrategy();

            //when
            boolean result = removeInterStationStrategy.isSatisfied(interStations, 1L);

            //then
            assertTrue(result);
        }

        @Test
        void 처음이_아닌_경우() {
            //given
            RemoveInterStationStrategy removeInterStationStrategy = new RemoveFirstInterStationStrategy();

            //when
            boolean result = removeInterStationStrategy.isSatisfied(interStations, 2L);

            //then
            assertFalse(result);
        }
    }

    @Nested
    class 처음에_잘_제거된다 {

        @Test
        void 처음에_제거되는_경우() {
            //given
            RemoveInterStationStrategy removeInterStationStrategy = new RemoveFirstInterStationStrategy();

            //when
            removeInterStationStrategy.removeInterStation(interStations, 1L);

            //then
            assertThat(interStations).hasSize(2);
        }
    }
}
