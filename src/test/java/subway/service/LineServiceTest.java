package subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.StubLineDao;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponseWithStations;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @Mock
    private SubwayMapService subwayMapService;

    private StubLineDao stubLineDao;
    private LineService lineService;

    @BeforeEach
    void setUp() {
        stubLineDao = new StubLineDao();
        lineService = new LineService(stubLineDao, subwayMapService);
    }

    @DisplayName("노선을 저장한다.")
    @Test
    void saveLine() {
        final Long lineId = lineService.saveLine(new LineRequest("2호선", "초록색")).getId();
        final Line result = stubLineDao.findById(lineId);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(lineId),
                () -> assertThat(result.getName()).isEqualTo("2호선"),
                () -> assertThat(result.getColor()).isEqualTo("초록색")
        );
    }

    @DisplayName("모든 노선들의 역들을 조회한다.")
    @Test
    void findLineResponses() {
        final List<LineResponseWithStations> given = List.of(
                new LineResponseWithStations(1L, "2호선", "초록색", List.of(
                        new Station(1L, "강남역")
                ))
        );
        given(subwayMapService.getLineResponsesWithStations()).willReturn(given);
        final List<LineResponseWithStations> result = lineService.findLineResponses();
        assertThat(result).isEqualTo(given);
    }

    @DisplayName("노선의 아이디로 노선의 역들을 조회한다.")
    @Test
    void findLineResponseById() {
        final LineResponseWithStations given = new LineResponseWithStations(1L, "1호선", "파란색", List.of(
                new Station(1L, "사당역")
        ));
        given(subwayMapService.getLineResponseWithStations(anyLong())).willReturn(given);
        final LineResponseWithStations result = lineService.findLineResponseById(1L);
        assertThat(result).isEqualTo(given);
    }

    @DisplayName("노선을 업데이트한다.")
    @Test
    void updateLine() {
        final Long lineId = stubLineDao.insert(new Line("2호선", "초록색")).getId();
        lineService.updateLine(lineId, new LineRequest("1호선", "파란색"));
        final Line result = stubLineDao.findById(lineId);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(lineId),
                () -> assertThat(result.getName()).isEqualTo("1호선"),
                () -> assertThat(result.getColor()).isEqualTo("파란색")
        );
    }

    @DisplayName("노선을 삭제한다.")
    @Test
    void deleteLineById() {
        final Long lineId = stubLineDao.insert(new Line("1호선", "파란색")).getId();
        lineService.deleteLineById(lineId);
        final Line result = stubLineDao.findById(lineId);
        assertThat(result).isNull();
    }
}
