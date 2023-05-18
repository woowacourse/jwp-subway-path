package subway.line.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.line.dao.LineDao;
import subway.domain.line.dto.LineRequest;
import subway.domain.line.dto.LineResponse;
import subway.domain.line.entity.LineEntity;
import subway.domain.line.service.LineService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LineServiceTest {

    @Mock
    private LineDao lineDao;
    @InjectMocks
    private LineService lineService;


    @Test
    void 모든_노선_정보_조회_테스트() {
        // when
        lineService.findAllLine();

        // then
        verify(lineDao).findAll();
    }

    @Test
    void 단일_노선_정보_조회_테스트() {
        // when
        lineService.findLineById(any());

        // then
        verify(lineDao).findById(any());
    }

    @Test
    void 노선_정보_삭제_테스트() {
        // when
        lineService.deleteLineById(any());

        // then
        verify(lineDao).deleteById(any());
    }

    @Test
    void 노선_정보_수정_테스트() {
        // given
        Long lineId = 1L;
        LineRequest line = new LineRequest("2호선", "초록색");

        // when
        lineService.updateLine(lineId, line);

        // then
        verify(lineDao).update(any(LineEntity.class));
    }

    @Test
    void 노선_정보를_추가_테스트() {
        //given
        LineRequest lineRequest = new LineRequest("2호선", "초록색");
        given(lineDao.findByName(lineRequest.getName())).willReturn(Optional.empty());
        LineEntity lineEntity = new LineEntity(lineRequest.getName(), lineRequest.getColor());
        given(lineDao.insert(lineEntity)).willReturn(lineEntity);
        //when
        LineEntity savelineEntity = lineService.saveLine(lineRequest);
        LineResponse lineResponse = LineResponse.of(savelineEntity);

        //then
        verify(lineDao).insert(any());
        Assertions.assertThat(lineResponse).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(lineEntity);
    }

    @Test
    void 노선_정보를_추가할_때_중복된_이름의_노선이_존재하면_예외_발생() {
        //given
        LineRequest lineRequest = new LineRequest("2호선", "초록색");
        given(lineDao.findByName(lineRequest.getName())).willReturn(Optional.of(new LineEntity(lineRequest.getName(), lineRequest.getColor())));

        //then
        assertThatThrownBy(() -> lineService.saveLine(lineRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선 이름이 이미 존재합니다. 유일한 노선 이름을 사용해주세요.");
    }
}
