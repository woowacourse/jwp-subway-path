package subway.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Station;
import subway.ui.dto.LineRequest;
import subway.ui.dto.LineResponse;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LineServiceTest {

    @Mock
    StationDao stationDao;
    @Mock
    LineDao lineDao;
    @InjectMocks
    LineService lineService;

    @Test
    void 존재하는_두_역을_통해_노선을_생성한다() {
        given(stationDao.findById(1L)).willReturn(Optional.of(new Station(1L, "천호")));
        given(stationDao.findById(2L)).willReturn(Optional.of(new Station(2L, "잠실")));
        given(lineDao.insert(new Line(null, "8호선", "pink"))).willReturn(new Line(1L, "8호선", "pink"));
        LineRequest lineRequest = new LineRequest("8호선", "pink", 1L, 2L, 10);

        LineResponse lineResponse = lineService.saveLine(lineRequest);

        assertThat(lineResponse.getId()).isEqualTo(1L);
        assertThat(lineResponse.getName()).isEqualTo("8호선");
        assertThat(lineResponse.getColor()).isEqualTo("pink");
    }

    @Test
    void 등록되지_않은_역으로_노선을_생성하면_예외가_발생한다() {
        given(stationDao.findById(1L)).willReturn(Optional.of(new Station(1L, "천호")));
        given(stationDao.findById(2L)).willReturn(Optional.empty());
        LineRequest lineRequest = new LineRequest("8호선", "pink", 1L, 2L, 10);

        assertThatThrownBy(() -> lineService.saveLine(lineRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않는 역이 있습니다.");
    }

    @Test
    void 이미_등록된_노선을_삭제할_수_있다() {
        assertDoesNotThrow(() -> lineService.deleteLineById(1L));
    }
}
