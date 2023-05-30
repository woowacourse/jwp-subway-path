package subway.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class SubwayTest {

    @Test
    void 지하철_노선도에_등록된_모든_역을_반환한다() {
        // given
        final Subway subway = new Subway(List.of(getSections1(), getSections2()));

        // when
        final List<Station> stations = subway.getStations();

        // then
        final List<Station> expected = List.of(new Station("잠실역"), new Station("아현역"), new Station("신촌역"));
        assertThat(expected).usingRecursiveComparison()
                .ignoringCollectionOrder()
                .ignoringExpectedNullFields()
                .isEqualTo(stations);
    }

    Sections getSections1() {
        final Station 잠실역 = new Station("잠실역");
        final Station 아현역 = new Station("아현역");

        final Section section = new Section(잠실역, 아현역, 5L);

        return new Sections(List.of(section));
    }

    Sections getSections2() {
        final Station 잠실역 = new Station("잠실역");
        final Station 신촌역 = new Station("신촌역");

        final Section section = new Section(잠실역, 신촌역, 3L);

        return new Sections(List.of(section));
    }
}