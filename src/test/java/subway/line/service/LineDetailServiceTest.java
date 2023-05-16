package subway.line.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.lineDetail.dao.LineDetailDao;
import subway.domain.lineDetail.domain.LineDetail;
import subway.domain.lineDetail.dto.LineDetailRequest;
import subway.domain.lineDetail.dto.LineDetailResponse;
import subway.domain.lineDetail.service.LineDetailService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LineDetailServiceTest {

    @Mock
    private LineDetailDao lineDetailDao;
    @InjectMocks
    private LineDetailService lineDetailService;


    @Test
    void 모든_노선_정보_조회_테스트() {
        // when
        lineDetailService.findAllLine();

        // then
        verify(lineDetailDao).findAll();
    }

    @Test
    void 단일_노선_정보_조회_테스트() {
        // when
        lineDetailService.findLineById(any());

        // then
        verify(lineDetailDao).findById(any());
    }

    @Test
    void 노선_정보_삭제_테스트() {
        // when
        lineDetailService.deleteLineById(any());

        // then
        verify(lineDetailDao).deleteById(any());
    }

    @Test
    void 노선_정보_수정_테스트() {
        // given
        Long lineId = 1L;
        LineDetailRequest line = new LineDetailRequest("2호선", "초록색");

        // when
        lineDetailService.updateLine(lineId, line);

        // then
        verify(lineDetailDao).update(any(LineDetail.class));
    }

    @Test
    void 노선_정보를_추가_테스트() {
        //given
        LineDetailRequest lineDetailRequest = new LineDetailRequest("2호선", "초록색");
        given(lineDetailDao.findByName(lineDetailRequest.getName())).willReturn(Optional.empty());
        LineDetail lineDetail = new LineDetail(lineDetailRequest.getName(), lineDetailRequest.getColor());
        given(lineDetailDao.insert(lineDetail)).willReturn(lineDetail);
        //when
        LineDetailResponse lineDetailResponse = lineDetailService.saveLine(lineDetailRequest);

        //then
        verify(lineDetailDao).insert(any());
        Assertions.assertThat(lineDetailResponse).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(lineDetail);
    }

    @Test
    void 노선_정보를_추가할_때_중복된_이름의_노선이_존재하면_예외_발생() {
        //given
        LineDetailRequest lineDetailRequest = new LineDetailRequest("2호선", "초록색");
        given(lineDetailDao.findByName(lineDetailRequest.getName())).willReturn(Optional.of(new LineDetail(lineDetailRequest.getName(), lineDetailRequest.getColor())));

        //then
        assertThatThrownBy(() -> lineDetailService.saveLine(lineDetailRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선 이름이 이미 존재합니다. 유일한 노선 이름을 사용해주세요.");
    }
}
