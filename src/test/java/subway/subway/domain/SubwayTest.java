package subway.subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import subway.line.domain.Line;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
class SubwayTest {
    @Test
    void 지하철_객체_생성() {
        // given
        final Subway subway = new Subway();
        
        // when
        final Set<Line> lines = subway.getLines();
        
        // then
        assertThat(lines).isNotNull();
    }
    
    @Test
    void 노선을_추가한다() {
        // given
        final Subway subway = new Subway();
        
        // when
        subway.addLine("1호선", "파랑");
        final Set<Line> lines = subway.getLines();
        
        // then
        assertThat(lines).contains(new Line("1호선", "파랑"));
    }
    
    @Test
    void 노선을_삭제한다() {
        // given
        final Subway subway = new Subway();
        
        // when
        subway.addLine("1호선", "파랑");
        subway.addLine("2호선", "초록");
        subway.removeLine("1호선");
        final Set<Line> lines = subway.getLines();
        
        // then
        assertThat(lines).contains(new Line("2호선", "초록"));
    }
    
    @Test
    void 역_최초_등록시_존재하지_않는_노선을_가리키면_예외_발생() {
        // given
        final Subway subway = new Subway();
        
        // when
        subway.addLine("1호선", "파랑");
        subway.addLine("2호선", "초록");
        assertThatIllegalArgumentException()
                .isThrownBy(() -> subway.initAddStation("3호선", "강남역", "역삼역", 3L));
    }
}
