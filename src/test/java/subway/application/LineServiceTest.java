package subway.application;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.LineDao;
import subway.domain.Line;
import subway.dto.LineRequest;
import subway.dto.LineResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @InjectMocks
    private LineService lineService;

    @Mock
    private LineDao lineDao;

    @Test
    void saveRequest를_받아서_호선을_저장한다() {
        // given
        LineRequest request = new LineRequest("2호선", "초록");
        when(lineDao.insert(request.toEntity())).thenReturn(1L);

        // when, then
        assertThat(lineService.saveLine(request)).isEqualTo(1L);
    }

    @Test
    void id_를_받아_해당_호선을_조회한다() {
        // given
        Long id = 1L;
        final Line response = Line.of(id, "2호선", "초록");
        doReturn(response).when(lineDao).findById(id);

        // when
        final LineResponse result = lineService.findLineResponseById(id);

        // then
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(LineResponse.of(response));
    }

    @Test
    void 전체_호선을_조회한다() {
        // given
        final Line line1 = Line.of(1L, "1호선", "파랑");
        final Line line2 = Line.of(2L, "2호선", "초록");
        doReturn(List.of(line1, line2)).when(lineDao).findAll();

        // when
        final List<LineResponse> allStationResponses = lineService.findLineResponses();

        // then
        assertThat(allStationResponses)
                .usingRecursiveComparison()
                .isEqualTo(List.of(
                        LineResponse.of(line1),
                        LineResponse.of(line2)
                ));
    }

    @Test
    void id_saveRequest를_받아_호선을_업데이트_한다() {
        // given
        final Long id = 1L;
        final LineRequest request = new LineRequest("2호선", "검정");

        // when
        lineService.updateLine(id, request);

        // then
        verify(lineDao, times(1)).updateById(id, request.toEntity());
    }

    @Test
    void id를_받아_호선을_삭제한다() {
        // given
        final Long id = 1L;

        // when
        lineService.deleteLineById(id);

        // then
        verify(lineDao, times(1)).deleteById(id);
    }
}
