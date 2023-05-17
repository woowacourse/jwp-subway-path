package subway.path.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.line.domain.Line;
import subway.section.domain.Section;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@SuppressWarnings("NonAsciiCharacters")
class PathTest {
    @ParameterizedTest(name = "{displayName} : partialDistance = {0}, resultFee = {1}")
    @CsvSource(value = {"4,1250", "5,1350", "9,1350", "10,1450", "44,2050", "45,2150", "52,2150", "53,2250", "60,2250", "61,2350"})
    void 최단_경로와_최단_거리와_요금을_구한다(final Long distanceMinusSix, final Long resultFee) {
        // given
        final long distance = 6L + distanceMinusSix;
        
        String first = "잠실역";
        String second = "가양역";
        String third = "화정역";
        String fourth = "종합운동장";
        String fifth = "선릉역";
        
        long distance1 = distanceMinusSix;
        long distance2 = 2L;
        long distance3 = 6L;
        long distance4 = 7L;
        Section firstSection = new Section(first, second, distance1);
        Section secondSection = new Section(second, third, distance2);
        Section thirdSection = new Section(third, fourth, distance3);
        Section fourthSection = new Section(fourth, fifth, distance4);
        
        Set<Section> initSections = Set.of(firstSection, secondSection, thirdSection, fourthSection);
        
        final Line line1 = new Line("1호선", "파랑", initSections);
        
        first = "청라역";
        second = "검암역";
        third = "화정역";
        fourth = "마곡나루역";
        fifth = "김포공항역";
        
        distance1 = 5L;
        distance2 = 6L;
        distance3 = 2L;
        distance4 = 2L;
        firstSection = new Section(first, second, distance1);
        secondSection = new Section(second, third, distance2);
        thirdSection = new Section(third, fourth, distance3);
        fourthSection = new Section(fourth, fifth, distance4);
        
        initSections = Set.of(firstSection, secondSection, thirdSection, fourthSection);
        
        final Line line2 = new Line("1호선", "파랑", initSections);
        final Path path = new Path(Set.of(line1, line2));
        
        // when
        final ShortestPathResult result = path.getShortestPath("김포공항역", "잠실역");
        
        // then
        assertThat(result.getShortestPath()).containsExactly("김포공항역", "마곡나루역", "화정역", "가양역", "잠실역");
        assertThat(result.getShortestDistance()).isEqualTo(distance);
        assertThat(result.getFee()).isEqualTo(resultFee);
    }
    
    @ParameterizedTest(name = "{displayName} : startStationName = {0}, endStationName = {1}")
    @CsvSource(value = {"소사역,잠실역", "잠실역,소사역"})
    void startStation을_없는_역으로_지정하면_예외_발생(final String startStationName, final String endStationName) {
        // given
        String first = "잠실역";
        String second = "가양역";
        String third = "화정역";
        String fourth = "종합운동장";
        String fifth = "선릉역";
        
        long distance1 = 2L;
        long distance2 = 3L;
        long distance3 = 6L;
        long distance4 = 7L;
        Section firstSection = new Section(first, second, distance1);
        Section secondSection = new Section(second, third, distance2);
        Section thirdSection = new Section(third, fourth, distance3);
        Section fourthSection = new Section(fourth, fifth, distance4);
        
        Set<Section> initSections = Set.of(firstSection, secondSection, thirdSection, fourthSection);
        
        final Line line1 = new Line("1호선", "파랑", initSections);
        
        first = "청라역";
        second = "검암역";
        third = "화정역";
        fourth = "마곡나루역";
        fifth = "김포공항역";
        
        distance1 = 5L;
        distance2 = 6L;
        distance3 = 7L;
        distance4 = 8L;
        firstSection = new Section(first, second, distance1);
        secondSection = new Section(second, third, distance2);
        thirdSection = new Section(third, fourth, distance3);
        fourthSection = new Section(fourth, fifth, distance4);
        
        initSections = Set.of(firstSection, secondSection, thirdSection, fourthSection);
        
        final Line line2 = new Line("1호선", "파랑", initSections);
        final Path path = new Path(Set.of(line1, line2));
        
        // expect
        assertThatIllegalArgumentException()
                .isThrownBy(() -> path.getShortestPath(startStationName, endStationName));
    }
}
