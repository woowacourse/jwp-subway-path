package subway.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Path;
import subway.domain.Station;
import subway.exception.ArrivalSameWithDepartureException;

@SpringBootTest
@Transactional
class PathServiceTest {

    @Autowired
    private PathService pathService;

    @Nested
    class ShortestPath {
        @Test
        @DisplayName("최단 경로를 계산할 수 있다.")
        void calculateShortestPath() {
            //given

            Optional<Path> shortestPath = pathService.getShortestPath(new Station("강남"), new Station("역삼"));
            //when
            //then
            Assertions.assertThat(shortestPath).isNotEmpty();
        }

        @Test
        @DisplayName("경로가 존재하지 않는 두 역의 최단 경로를 구한다.")
        void invalidPath() {
            //given
            Optional<Path> shortestPath = pathService.getShortestPath(new Station("강남"), new Station("해운대"));
            //when
            //then
            Assertions.assertThat(shortestPath).isEmpty();
        }

        @Test
        @DisplayName("같은 역을 출발지와 도착지로 두면 예외를 던진다.")
        void sameStation() {
            //given
            Station station = new Station("잠실");
            //when
            //then
            assertThatThrownBy(() -> pathService.getShortestPath(station, new Station("잠실")))
                    .isInstanceOf(ArrivalSameWithDepartureException.class);
        }

    }
}