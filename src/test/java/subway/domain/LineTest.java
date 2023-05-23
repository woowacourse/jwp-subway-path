package subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.section.Sections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static subway.integration.IntegrationFixture.*;

class LineTest {

    private Line line;
    private Section section;

    @BeforeEach
    void setUp() {
        section = new Section(1L, STATION_A, STATION_B, DISTANCE5);
        line = LINE1.addSection(section);
    }

    @DisplayName("호선에 역이 존재하지 않을 경우 두 역이 동시에 등록한다.")
    @Test
    void addInitialSection() {
        final Line newLine = LINE2.addSection(section);
        final Section newSection = newLine.getSections().getSections().get(0);

        assertThat(newSection.getBeforeStation()).isEqualTo(section.getBeforeStation());
        assertThat(newSection.getNextStation()).isEqualTo(section.getNextStation());
    }

    @DisplayName("호선에 역이 존재할 경우 상행 종점에 역을 추가한다.")
    @Test
    void addHeadSection() {
        final Section newSection = new Section(2L, STATION_C, STATION_A, DISTANCE7);
        final Line newLine = line.addSection(newSection);

        assertThat(newLine.getSections().getSections())
                .extracting(Section::getBeforeStation, Section::getNextStation, Section::getDistance)
                .containsExactly(
                        tuple(STATION_C, STATION_A, DISTANCE7),
                        tuple(STATION_A, STATION_B, DISTANCE5)
                );
    }

    @DisplayName("호선에 역이 존재할 경우 중간에 역을 추가한다.")
    @Test
    void addCentralSection() {
        final Section newSection = new Section(2L, STATION_A, STATION_C, DISTANCE3);
        final Line newLine = line.addSection(newSection);

        assertThat(newLine.getSections().getSections())
                .extracting(Section::getBeforeStation, Section::getNextStation, Section::getDistance)
                .containsExactly(
                        tuple(STATION_A, STATION_C, DISTANCE3),
                        tuple(STATION_C, STATION_B, DISTANCE2)
                );
    }

    @DisplayName("호선에 역이 존재할 경우 하행 종점에 역을 추가한다.")
    @Test
    void addTailSection() {
        final Section newSection = new Section(2L, STATION_B, STATION_C, DISTANCE7);
        final Line newLine = line.addSection(newSection);

        assertThat(newLine.getSections().getSections())
                .extracting(Section::getBeforeStation, Section::getNextStation, Section::getDistance)
                .containsExactly(
                        tuple(STATION_A, STATION_B, DISTANCE5),
                        tuple(STATION_B, STATION_C, DISTANCE7)
                );
    }

    @DisplayName("호선에 이미 존재하는 역을 추가할 경우 예외가 발생한다.")
    @Test
    void addDuplicateSection() {
        final Section newSection = new Section(2L, STATION_B, STATION_A, DISTANCE3);

        assertThrows(IllegalArgumentException.class,
                () -> line.addSection(newSection),
                "이미 존재하는 역입니다.");
    }

    @DisplayName("호선에 등록하려는 역과 기준이 되는 역이 동일할 경우 예외가 발생한다.")
    @Test
    void addSameStationSection() {
        final Section newSection = new Section(2L, STATION_C, STATION_C, DISTANCE3);

        assertThrows(IllegalArgumentException.class,
                () -> line.addSection(newSection),
                "이전 역과 다음 역은 동일할 수 없습니다.");
    }

    @DisplayName("역이 두개만 있을 때 하나를 제거하는 경우 등록된 두 역 모두 사라진다.")
    @Test
    void removeWhenLineHas2Station() {
        final Line removedLine = line.removeStation(STATION_B);
        final Sections sections = removedLine.getSections();

        assertThat(sections.isEmpty()).isTrue();
    }
}
