package subway.domain;

import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.domain.line.LineMap;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class LineMapTest {

    private Section section1 = new Section(new Station("잠실역"), new Station("아현역"), 3L);
    private Section section2 = new Section(new Station("신촌역"), new Station("잠실역"), 3L);

    @Test
    void 역_구간_정보들을_이용하여_상행_종점부터_하행_종점까지_역의_정보를_순서대로_가져온다() {
        final Sections sections = new Sections(List.of(section1, section2));
        final LineMap lineMap = new LineMap(sections);

        final List<Station> orderedStations = lineMap.getOrderedStations();

        final SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(orderedStations.size()).isEqualTo(3);

        softAssertions.assertThat(orderedStations.get(0).getName()).isEqualTo("신촌역");
        softAssertions.assertThat(orderedStations.get(1).getName()).isEqualTo("잠실역");
        softAssertions.assertThat(orderedStations.get(2).getName()).isEqualTo("아현역");
        softAssertions.assertAll();
    }
}
