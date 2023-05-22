package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class LinesTest {

    private final Station STATION1 = new Station("잠실새내");
    private final Station STATION2 = new Station("잠실");
    private final Station STATION3 = new Station("잠실나루");
    private final Distance DISTANCE1 = new Distance(10);
    private final Distance DISTANCE2 = new Distance(15);
    private final Section SECTION1 = new Section(STATION1, STATION2, DISTANCE1);
    private final Section SECTION2 = new Section(STATION2, STATION3, DISTANCE2);
    private final List<Section> SECTION_LIST = List.of(SECTION1, SECTION2);
    private final Sections SECTIONS = new Sections(SECTION_LIST);
    private final Line LINE = new Line(1L, new LineName("2호선"), new LineColor("초록"), SECTIONS);
    private final List<Line> LINES = List.of(LINE);

    @DisplayName("생성한다")
    @Test
    void 생성한다() {
        assertDoesNotThrow(() -> new Lines(LINES));
    }

    @DisplayName("라인을 ID로 지운다")
    @Test
    void 라인을_지운다() {
        //given
        Lines lines = new Lines(LINES);
        //when
        Lines updateLines = lines.deleteById(1L);
        //then
        assertThat(updateLines.getLines()).isEmpty();
    }

    @DisplayName("중복 역을 검증한다")
    @Test
    void 중복_역을_검증한다() {
        //given
        Lines lines = new Lines(LINES);
        //then
        assertThatThrownBy(() -> lines.validateNotDuplicatedStation(STATION1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("중복 노선을 검증한다")
    @Test
    void 중복_노선을_검증한다() {
        //given
        Lines lines = new Lines(LINES);
        //then
        assertThatThrownBy(() -> lines.validateNotDuplicatedLine(LINE))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
