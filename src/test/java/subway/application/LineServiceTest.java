package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import subway.dao.LineDao;
import subway.dao.PathDao;
import subway.dao.entity.PathEntity;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.LineRequest;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {
    @InjectMocks
    private LineService lineService;

    @Mock
    private LineDao lineDao;

    @Mock
    private PathDao pathDao;


    @DisplayName("노선을 추가할 수 있다")
    @Test
    void saveLine() throws SQLException {
        //given
        when(lineDao.insert(any()))
                .thenReturn(new Line(1L, "1호선", "red"));

        //when
        lineService.saveLine(new LineRequest("1호선", "red"));

        //then
        verify(lineDao, times(1)).insert(any());
    }

    @DisplayName("노선을 추가할 때 이름이 중복되면 예외가 발생한다")
    @Test
    void saveLineException() throws SQLException {
        //given
        when(lineDao.insert(any()))
                .thenThrow(new DataIntegrityViolationException("노선 이름은 중복될 수 없습니다."));

        //then
        assertThatThrownBy(() -> lineService.saveLine(new LineRequest("1호선", "red")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선 이름은 중복될 수 없습니다.");
    }

    @DisplayName("해당 노선의 모든 역들을 가져올 수 있다")
    @Test
    void findAllStations() {
        //given
        when(lineDao.findById(anyLong()))
                .thenReturn(Optional.of(new Line("1호선", "red")));
        when(pathDao.findStationsByLineId(anyLong()))
                .thenReturn(List.of(new PathEntity(1L, "부산역", null, null),
                        new PathEntity(2L, "서면역", 1L, 10),
                        new PathEntity(3L, "해운대역", 2L, 10)));

        //when
        final List<Station> result = lineService.findAllStations(1L);

        //then
        assertThat(result).map(Station::getName)
                .containsExactly("부산역", "서면역", "해운대역");
    }

    @DisplayName("빈 노선의 역을 조회하면 빈 리스트가 반환된다")
    @Test
    void findAllStationsOfEmptyLine() {
        //given
        when(lineDao.findById(anyLong()))
                .thenReturn(Optional.of(new Line("1호선", "red")));
        when(pathDao.findStationsByLineId(anyLong()))
                .thenReturn(Collections.emptyList());

        //when
        final List<Station> result = lineService.findAllStations(1L);

        //then
        assertThat(result).isEmpty();
    }
}