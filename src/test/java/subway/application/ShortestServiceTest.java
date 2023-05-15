package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.LineRequest;
import subway.dto.PathRequest;
import subway.dto.ShortestResponse;
import subway.dto.StationRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
class ShortestServiceTest {

    @Autowired
    private LineService lineService;
    @Autowired
    private StationService stationService;
    @Autowired
    private ShortestService shortestService;

    @Nested
    @DisplayName("최단 경로 찾기 테스트")
    class findShortest {

        /**
         * 광안 <-5-> 전포 <-5-> 노포
         * 광안 <-2-> 부산 <-1-> 해운대 <-1-> 노포
         * expect: 4
         */
        @DisplayName("여러 경로 중 최단 경로를 구할 수 있다")
        @Test
        void findShortest() {
            Long lineId1 = lineService.saveLine(new LineRequest("1호선", "red")).getId();
            Long lineId2 = lineService.saveLine(new LineRequest("2호선", "blue")).getId();

            Long source = stationService.saveStation(new StationRequest("광안역")).getId();
            Long destination = stationService.saveStation(new StationRequest("노포역")).getId();

            Long stationId2 = stationService.saveStation(new StationRequest("전포역")).getId();
            Long stationId3 = stationService.saveStation(new StationRequest("부산역")).getId();
            Long stationId4 = stationService.saveStation(new StationRequest("해운대역")).getId();


            lineService.addPathToLine(lineId1, new PathRequest(source, stationId2, 5));
            lineService.addPathToLine(lineId1, new PathRequest(stationId2, destination, 5));

            lineService.addPathToLine(lineId2, new PathRequest(source, stationId3, 2));
            lineService.addPathToLine(lineId2, new PathRequest(stationId3, stationId4, 1));
            lineService.addPathToLine(lineId2, new PathRequest(stationId4, destination, 1));

            //when
            final ShortestResponse shortest = shortestService.findShortest(source, destination);

            //then
            assertAll(
                    () -> assertThat(shortest.getTotalDistance()).isEqualTo(4),
                    () -> assertThat(shortest.getPaths()).hasSize(3));
        }

        @DisplayName("존재하지 않는 역으로 최단 경로를 조회하면 예외가 발생한다")
        @Test
        void findShortest_fail() {
            assertThatThrownBy(() -> shortestService.findShortest(1L, 2L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 역입니다.");
        }

        @DisplayName("존재하지 않는 경로로 최단 경로를 조회하면 예외가 발생한다")
        @Test
        void findShortest_fail2() {
            //given
            lineService.saveLine(new LineRequest("1호선", "red"));
            lineService.saveLine(new LineRequest("2호선", "blue"));

            Long source = stationService.saveStation(new StationRequest("광안역")).getId();
            Long destination = stationService.saveStation(new StationRequest("노포역")).getId();

            Long stationId1 = stationService.saveStation(new StationRequest("전포역")).getId();
            Long stationId2 = stationService.saveStation(new StationRequest("부산역")).getId();

            lineService.addPathToLine(1L, new PathRequest(source, stationId1, 5));
            lineService.addPathToLine(2L, new PathRequest(destination, stationId2, 5));

            //when, then
            assertThatThrownBy(() -> shortestService.findShortest(source, destination))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("경로가 존재하지 않습니다.");
        }
    }
}
