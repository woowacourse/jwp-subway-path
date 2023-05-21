package subway.line.domain;

import org.junit.jupiter.api.Test;
import subway.section.domain.Direction;
import subway.section.domain.Section;
import subway.section.domain.Sections;

import java.util.HashSet;
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
        subway.addLine("1호선", "파랑", 0L);
        final Set<Line> lines = subway.getLines();
        
        // then
        assertThat(lines).contains(new Line("1호선", "파랑", 0L));
    }
    
    @Test
    void 노선을_삭제한다() {
        // given
        final Subway subway = new Subway();
        
        // when
        subway.addLine("1호선", "파랑", 0L);
        subway.addLine("2호선", "초록", 0L);
        subway.removeLine("1호선");
        final Set<Line> lines = subway.getLines();
        
        // then
        assertThat(lines).contains(new Line("2호선", "초록", 0L));
    }
    
    @Test
    void 역_최초_등록시_존재하지_않는_노선을_가리키면_예외_발생() {
        // given
        final Subway subway = new Subway();
        subway.addLine("1호선", "파랑", 0L);
        subway.addLine("2호선", "초록", 0L);
        
        // expect
        assertThatIllegalArgumentException()
                .isThrownBy(() -> subway.initAddStation("3호선", "강남역", "역삼역", 3L));
    }
    
    @Test
    void 역_추가시_존재하지_않는_노선을_가리키면_예외_발생() {
        // given
        final Subway subway = new Subway();
        subway.addLine("1호선", "파랑", 0L);
        subway.initAddStation("1호선", "강남역", "선릉역", 3L);
        
        // expect
        assertThatIllegalArgumentException()
                .isThrownBy(() -> subway.addStation("2호선", "강남역", Direction.RIGHT, "역삼역", 2L));
    }
    
    @Test
    void 역_삭제시_존재하지_않는_노선을_가리키면_예외_발생() {
        // given
        final Subway subway = new Subway();
        subway.addLine("1호선", "파랑", 0L);
        subway.initAddStation("1호선", "강남역", "선릉역", 3L);
        
        // expect
        assertThatIllegalArgumentException()
                .isThrownBy(() -> subway.removeStation("2호선", "강남역"));
    }
    
    @Test
    void 노선_추가시_이미_존재하는_노선의_이름이면_예외_발생() {
        // given
        final Subway subway = new Subway();
        subway.addLine("1호선", "파랑", 0L);
        subway.addLine("2호선", "초록", 0L);
        
        // expect
        assertThatIllegalArgumentException()
                .isThrownBy(() -> subway.addLine("2호선", "노랑", 0L));
    }
    
    @Test
    void 노선_추가시_이미_존재하는_노선의_색상이면_예외_발생() {
        // given
        final Subway subway = new Subway();
        subway.addLine("1호선", "파랑", 0L);
        subway.addLine("2호선", "초록", 0L);
        
        // expect
        assertThatIllegalArgumentException()
                .isThrownBy(() -> subway.addLine("3호선", "초록", 0L));
    }
    
    @Test
    void 노선_삭제시_존재하지_않는_노선을_가리키면_예외_발생() {
        // given
        final Subway subway = new Subway();
        subway.addLine("1호선", "파랑", 0L);
        subway.addLine("2호선", "초록", 0L);
        
        // expect
        assertThatIllegalArgumentException()
                .isThrownBy(() -> subway.removeLine("3호선"));
    }
    
    @Test
    void stationId로_모든_노선에_있는_해당_역을_삭제한다() {
        // given
        final String first = "잠실역";
        final String second = "가양역";
        final String third = "화정역";
        final String fourth = "종합운동장";
        final String fifth = "선릉역";
        
        final int distance1 = 3;
        final int distance2 = 2;
        final int distance3 = 6;
        final int distance4 = 7;
        final Section firstSection = new Section(first, second, distance1, "1호선");
        final Section secondSection = new Section(second, third, distance2, "1호선");
        final Section thirdSection = new Section(third, fourth, distance3, "1호선");
        final Section fourthSection = new Section(fourth, fifth, distance4, "1호선");
        
        final Set<Section> initSections1 = new HashSet<>(Set.of(firstSection, secondSection));
        final Set<Section> initSections2 = new HashSet<>(Set.of(thirdSection, fourthSection));
        final Line line1 = new Line("1호선", "파랑", initSections1);
        final Line line2 = new Line("2호선", "초록", initSections2);
        final Subway subway = new Subway(Set.of(line1, line2));
        
        // when
        final Set<Line> modifiedLines = subway.removeStationOnAllLine("화정역");
        
        // then
        assertThat(modifiedLines).contains(line1, line2);
    }
}
