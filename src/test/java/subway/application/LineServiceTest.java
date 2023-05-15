package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.PathRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class LineServiceTest {

    @Autowired
    private LineService lineService;
    @Autowired
    private StationDao stationDao;

    @DisplayName("두 노선에 겹쳐진 역 제거 시 두 노선 모두 반영된다")
    @Test
    void deleteStation() {
        //given
        final LineResponse line1 = lineService.saveLine(new LineRequest("1호선", "red"));
        final LineResponse line2 = lineService.saveLine(new LineRequest("2호선", "blue"));

        final Station station1 = stationDao.insert(new Station("해운대역"));
        final Station station2 = stationDao.insert(new Station("서면역"));
        final Station station3 = stationDao.insert(new Station("다대포역"));

        lineService.addPathToLine(line1.getId(), new PathRequest(station1.getId(),
                station2.getId(),
                3));
        lineService.addPathToLine(line2.getId(), new PathRequest(station2.getId(),
                station3.getId(),
                3));

        //when
        lineService.deletePathByStationId(station2.getId());

        //then
        assertThat(lineService.findLineResponses())
                .map(LineResponse::getPaths)
                .allMatch(List::isEmpty);
    }
}
