package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.PathDao;
import subway.dao.StationDao;
import subway.domain.FareStrategy;
import subway.domain.Station;
import subway.domain.path.Path;
import subway.domain.path.Paths;
import subway.dto.ShortestResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShortestServiceTest {

    @InjectMocks
    private ShortestService shortestService;
    @Mock
    private PathDao pathDao;
    @Mock
    private StationDao stationDao;
    @Mock
    private FareStrategy fareStrategy;

    @Nested
    @DisplayName("최단 경로 찾기 테스트")
    class FindShortest {

        /**
         * 광안 <-5-> 전포 <-5-> 노포
         * 광안 <-2-> 부산 <-1-> 해운대 <-1-> 노포
         * expect: 4
         */
        @DisplayName("여러 경로 중 최단 경로를 구할 수 있다")
        @Test
        void findShortest() {
            //given
            final Station source = new Station("광안");
            final Station target = new Station("노포");

            final Station station1 = new Station("전포");
            final Station station2 = new Station("부산");
            final Station station3 = new Station("해운대");

            final Path path1 = new Path(source, station1, 5);
            final Path path2 = new Path(station1, target, 5);

            final Path path3 = new Path(source, station2, 2);
            final Path path4 = new Path(station2, station3, 1);
            final Path path5 = new Path(station3, target, 1);
            
            final Paths paths = new Paths(List.of(path1, path2, path3, path4, path5));
            when(pathDao.findAll()).thenReturn(paths);
            when(stationDao.findById(any()))
                    .thenReturn(source)
                    .thenReturn(target);
            when(fareStrategy.calculate(anyLong())).thenReturn(1250);

            //when
            final ShortestResponse shortest = shortestService.findShortest(source.getId(), target.getId());

            //then
            assertAll(
                    () -> assertThat(shortest.getTotalDistance()).isEqualTo(4),
                    () -> assertThat(shortest.getPaths()).hasSize(3),
                    () -> assertThat(shortest.getTotalCost()).isEqualTo(1250));
        }

        @DisplayName("존재하지 않는 경로로 최단 경로를 조회하면 예외가 발생한다")
        @Test
        void findShortest_fail() {
            //given
            final Station source = new Station("광안");
            final Station target = new Station("노포");

            final Station station1 = new Station("전포");
            final Station station2 = new Station("부산");

            final Path path1 = new Path(source, station1, 5);
            final Path path2 = new Path(target, station2, 5);

            final Paths paths = new Paths(List.of(path1, path2));
            when(pathDao.findAll()).thenReturn(paths);
            when(stationDao.findById(any()))
                    .thenReturn(source)
                    .thenReturn(target);

            //when, then
            assertThatThrownBy(() -> shortestService.findShortest(source.getId(), target.getId()))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("경로가 존재하지 않습니다.");
        }
    }
}
