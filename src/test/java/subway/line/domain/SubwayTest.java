package subway.line.domain;

import org.junit.jupiter.api.Test;
import subway.section.domain.Direction;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

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
        subway.addLine("1호선", "파랑");
        subway.addLine("2호선", "초록");
        
        // expect
        assertThatIllegalArgumentException()
                .isThrownBy(() -> subway.initAddStation("3호선", "강남역", "역삼역", 3L));
    }
    
    @Test
    void 역_추가시_존재하지_않는_노선을_가리키면_예외_발생() {
        // given
        final Subway subway = new Subway();
        subway.addLine("1호선", "파랑");
        subway.initAddStation("1호선", "강남역", "선릉역", 3L);
        
        // expect
        assertThatIllegalArgumentException()
                .isThrownBy(() -> subway.addStation("2호선", "강남역", Direction.RIGHT, "역삼역", 2L));
    }
    
    @Test
    void 역_삭제시_존재하지_않는_노선을_가리키면_예외_발생() {
        // given
        final Subway subway = new Subway();
        subway.addLine("1호선", "파랑");
        subway.initAddStation("1호선", "강남역", "선릉역", 3L);
        
        // expect
        assertThatIllegalArgumentException()
                .isThrownBy(() -> subway.removeStation("2호선", "강남역"));
    }
    
    @Test
    void 노선_추가시_이미_존재하는_노선의_이름이면_예외_발생() {
        // given
        final Subway subway = new Subway();
        subway.addLine("1호선", "파랑");
        subway.addLine("2호선", "초록");
        
        // expect
        assertThatIllegalArgumentException()
                .isThrownBy(() -> subway.addLine("2호선", "노랑"));
    }
    
    @Test
    void 노선_추가시_이미_존재하는_노선의_색상이면_예외_발생() {
        // given
        final Subway subway = new Subway();
        subway.addLine("1호선", "파랑");
        subway.addLine("2호선", "초록");
        
        // expect
        assertThatIllegalArgumentException()
                .isThrownBy(() -> subway.addLine("3호선", "초록"));
    }
    
    @Test
    void 노선_삭제시_존재하지_않는_노선을_가리키면_예외_발생() {
        // given
        final Subway subway = new Subway();
        subway.addLine("1호선", "파랑");
        subway.addLine("2호선", "초록");
        
        // expect
        assertThatIllegalArgumentException()
                .isThrownBy(() -> subway.removeLine("3호선"));
    }
}
