package subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static subway.fixture.SectionFixture.SECTION_1;
import static subway.fixture.SectionFixture.SECTION_2;
import static subway.fixture.StationFixture.*;

public class SectionsRemoveTest {
    Sections sections;

    @BeforeEach
    void beforeEach() {
        sections = new Sections(new ArrayList<>(List.of(SECTION_1, SECTION_2)));
    }

    @DisplayName("구간이 하나밖에 없고 지울 Station 이 해당 구간에 포함되어 있으면 구간을 삭제한다")
    @Test
    void removeWhenOneSectionExists() {
        //given
        sections = new Sections(new ArrayList<>(List.of(SECTION_1)));

        //when
        sections.remove(STATION_1);

        //then
        assertThat(sections.getSections().isEmpty()).isTrue();
    }

    @DisplayName("상행 종점을 지운다")
    @Test
    void removeUpEnd() {
        //given, when, then
        assertThat(sections.remove(STATION_1)).isEqualTo(null);
        assertThat(sections.getSections().size()).isEqualTo(1);
    }

    @DisplayName("하행 종점을 지운다")
    @Test
    void removeDownEnd() {
        //given, when, then
        assertThat(sections.remove(STATION_3)).isEqualTo(null);
        assertThat(sections.getSections().size()).isEqualTo(1);
    }

    @DisplayName("중간 역을 지운다")
    @Test
    void removeInside() {
        //given, when, then
        assertThat(sections.remove(STATION_2))
                .isEqualTo(new Section(SECTION_1.getLine(),
                        SECTION_1.getPreStation(), SECTION_2.getStation(), SECTION_1.getDistance().add(SECTION_2.getDistance())));
        assertThat(sections.getSections().size()).isEqualTo(1);
    }
}
