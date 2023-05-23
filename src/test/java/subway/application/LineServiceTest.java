package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.PathRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
class LineServiceTest {

    @Autowired
    private LineService lineService;
    @Autowired
    private StationDao stationDao;

    @DisplayName("노선에서 경로를 제거하면 재배열 된다.")
    @Test
    void deleteStation() {
        //given
        final LineResponse line = lineService.saveLine(new LineRequest("1호선", "red"));

        final Station station1 = stationDao.insert(new Station("해운대역"));
        final Station station2 = stationDao.insert(new Station("서면역"));
        final Station station3 = stationDao.insert(new Station("다대포역"));

        lineService.addPathToLine(line.getId(), new PathRequest(station1.getId(),
                station2.getId(),
                3));
        lineService.addPathToLine(line.getId(), new PathRequest(station2.getId(),
                station3.getId(),
                3));

        //when
        lineService.deletePath(line.getId(), station2.getId());

        //then
        final LineResponse lineResponse = lineService.findLineById(line.getId());
        assertAll(
                () -> assertThat(lineResponse.getPaths()).hasSize(1),
                () -> assertThat(lineResponse.getPaths().get(0).getDistance()).isEqualTo(6));
    }

    @DisplayName("추가 요금의 노선을 저장할 수 있다")
    @Test
    void saveLine() {
        //given
        lineService.saveLine(new LineRequest("1호선", "blue", 500));

        //when
        final Line line = lineService.findAllLines().get(0);

        //then
        assertThat(line.getAdditionalFare()).isEqualTo(500);
    }
}
