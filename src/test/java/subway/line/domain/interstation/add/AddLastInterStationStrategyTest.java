package subway.line.domain.interstation.add;

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
@DisplayName("마지막에 역을 추가하는 전략 테스트")
class AddLastInterStationStrategyTest {

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
    class 마지막에_추가되는게_맞는지_확인하는_기능_테스트 {

        @Test
        void 마지막에_추가되는_경우() {
            //given
            final Long upStationId = 4L;
            Long downStationId = null;
            final long newStationId = 5L;
            AddInterStationStrategy addInterStationStrategy = new AddLastInterStationStrategy();

            //when
            boolean result = addInterStationStrategy.isSatisfied(interStations, upStationId, downStationId,
                    newStationId);

            //then
            assertTrue(result);
        }

        @Test
        void 사이클이_생기는_경우는_안된다() {
            //given
            final Long upStationId = 4L;
            Long downStationId = null;
            final long newStationId = 3L;
            AddInterStationStrategy addInterStationStrategy = new AddLastInterStationStrategy();

            //when
            boolean result = addInterStationStrategy.isSatisfied(interStations, upStationId, downStationId,
                    newStationId);

            //then
            assertFalse(result);
        }

        @Test
        void 중간에_추가되는_경우는_안된다() {
            //given
            final Long upStationId = 3L;
            final Long downStationId = 4L;
            final long newStationId = 5L;
            AddInterStationStrategy addInterStationStrategy = new AddLastInterStationStrategy();

            //when
            boolean result = addInterStationStrategy.isSatisfied(interStations, upStationId, downStationId,
                    newStationId);

            //then
            assertFalse(result);
        }

        @Test
        void 하행선이_정해져_있는_경우는_안된다() {
            //given
            final Long upStationId = 4L;
            final Long downStationId = 5L;
            final long newStationId = 6L;
            AddInterStationStrategy addInterStationStrategy = new AddLastInterStationStrategy();

            //when
            boolean result = addInterStationStrategy.isSatisfied(interStations, upStationId, downStationId,
                    newStationId);

            //then
            assertFalse(result);
        }
    }

    @Nested
    class 마지막에_역이_추가된다 {

        @Test
        void 정상적으로_추가된다() {
            //given
            final Long upStationId = 4L;
            Long downStationId = null;
            final long newStationId = 5L;
            final long distance = 10L;
            AddInterStationStrategy addInterStationStrategy = new AddLastInterStationStrategy();

            //when
            addInterStationStrategy.addInterStation(interStations, upStationId, downStationId, newStationId, distance);

            //then
            assertThat(interStations).containsExactly(
                    new InterStation(1L, 2L, 10L),
                    new InterStation(2L, 3L, 10L),
                    new InterStation(3L, 4L, 10L),
                    new InterStation(4L, 5L, 10L));
        }
    }
}
