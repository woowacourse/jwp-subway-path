package subway.line.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.section.domain.Section;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
class LineTest {
    @ParameterizedTest(name = "{displayName} : extraCharge = {0}")
    @ValueSource(longs = {0, 1, 1000})
    void 노선_정상_생성(final Long extraCharge) {
        // expect
        assertThatNoException()
                .isThrownBy(() -> new Line("1호선", "파랑", extraCharge));
    }
    
    @ParameterizedTest(name = "{displayName} : name = {0}")
    @NullAndEmptySource
    void 노선_이름이_Null_or_Empty인_경우_예외_발생(final String name) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Line(name, "파랑", 0L));
    }
    
    @ParameterizedTest(name = "{displayName} : color = {0}")
    @NullAndEmptySource
    void 노선_색상이_Null_or_Empty인_경우_예외_발생(final String color) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Line("1호선", color, 0L));
    }
    
    @Test
    void 노선_추가_요금이_null인_경우_예외_발생() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Line("1호선", "파랑", null, null));
    }
    
    @Test
    void 노선_추가_요금이_음수인_경우_예외_발생() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Line("1호선", "파랑", -1L, null));
    }
    
    @ParameterizedTest(name = "{displayName} : stationName = {0}, expectResult = {1}")
    @CsvSource(value = {"잠실역,true", "청라역,false"})
    void 해당_역이_포함되어있는지_확인(final String stationName, final boolean expectResult) {
        // given
        final String first = "잠실역";
        final String second = "가양역";
        final String third = "화정역";
        
        final int distance1 = 3;
        final int distance2 = 2;
        final Section firstSection = new Section(first, second, distance1);
        final Section secondSection = new Section(second, third, distance2);
        
        final Set<Section> initSections1 = new HashSet<>(Set.of(firstSection, secondSection));
        final Line line = new Line("1호선", "파랑", initSections1);
        
        // when
        final boolean isContainsStation = line.isContainsStation(stationName);
        
        // then
        assertThat(isContainsStation).isEqualTo(expectResult);
    }
}
