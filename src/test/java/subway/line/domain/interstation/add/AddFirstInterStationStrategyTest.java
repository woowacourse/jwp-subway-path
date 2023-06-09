package subway.line.domain.interstation.add;

import static org.assertj.core.api.Assertions.assertThat;

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
@DisplayName("처음에 역을 추가하는 전략 테스트")
class AddFirstInterStationStrategyTest {

    private List<InterStation> interStations;

    @BeforeEach
    void setUp() {
        interStations = new ArrayList<>(List.of(
                new InterStation(1L, 2L, 10L),
                new InterStation(2L, 3L, 10L),
                new InterStation(3L, 4L, 10L)
        ));
    }

    @Nested
    class 처음에_추가되는게_맞는지_검사하는_기능_테스트 {

        @Test
        void 처음에_추가되는_경우() {
            // given
            Long upStationId = null;
            final Long downStationId = 1L;
            final long newStationId = 5L;
            AddInterStationStrategy addInterStationStrategy = new AddFirstInterStationStrategy();

            // when
            boolean result = addInterStationStrategy.isSatisfied(interStations, upStationId, downStationId,
                    newStationId);

            // then
            assertThat(result).isTrue();
        }

        @Test
        void 사이클이_생기는_경우() {
            // given
            Long upStationId = null;
            final Long downStationId = 1L;
            final long newStationId = 2L;
            AddInterStationStrategy addInterStationStrategy = new AddFirstInterStationStrategy();

            // when
            boolean result = addInterStationStrategy.isSatisfied(interStations, upStationId, downStationId,
                    newStationId);

            // then
            assertThat(result).isFalse();
        }

        @Test
        void 처음에_추가되지_않는_경우() {
            // given
            Long upStationId = null;
            final Long downStationId = 2L;
            final long newStationId = 5L;
            AddInterStationStrategy addInterStationStrategy = new AddFirstInterStationStrategy();

            // when
            boolean result = addInterStationStrategy.isSatisfied(interStations, upStationId, downStationId,
                    newStationId);

            // then
            assertThat(result).isFalse();
        }

        @Test
        void 상행선이_정해져_있는_경우() {
            // given
            final Long upStationId = 1L;
            final Long downStationId = 1L;
            final long newStationId = 5L;
            AddInterStationStrategy addInterStationStrategy = new AddFirstInterStationStrategy();

            // when
            boolean result = addInterStationStrategy.isSatisfied(interStations, upStationId, downStationId,
                    newStationId);

            // then
            assertThat(result).isFalse();
        }
    }

    @Nested
    class 역을_추가하는_경우_기능_테스트 {

        @Test
        void 정상적으로_역이_추가된다() {
            // given
            Long upStationId = null;
            final Long downStationId = 1L;
            final long newStationId = 5L;
            final long distance = 10L;
            AddInterStationStrategy addInterStationStrategy = new AddFirstInterStationStrategy();

            // when
            addInterStationStrategy.addInterStation(interStations, upStationId, downStationId, newStationId, distance);

            // then
            assertThat(interStations).containsExactly(
                    new InterStation(5L, 1L, 10L),
                    new InterStation(1L, 2L, 10L),
                    new InterStation(2L, 3L, 10L),
                    new InterStation(3L, 4L, 10L)
            );
        }
    }
}
