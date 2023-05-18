package subway.section.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import subway.station.domain.Station;

class SectionsSorterTest {

  private static final Station 잠실나루역 = new Station(3L, "잠실나루역");
  private static final Station 잠실역 = new Station(1L, "잠실역");
  private static final Station 잠실새내역 = new Station(2L, "잠실새내역");

  @Test
  void use() {
    //given
    final List<Section> sections = List.of(
        Section.of(잠실새내역, 잠실역, 5),
        Section.of(잠실역, 잠실나루역, 4)
    );

    //when
    //then
    Assertions.assertDoesNotThrow(() -> SectionsSorter.use(sections));
  }

  @Test
  void getSortedSections() {
    //given
    final Section firstSection = Section.of(잠실새내역, 잠실역, 5);
    final Section secondSection = Section.of(잠실역, 잠실나루역, 4);
    final List<Section> sections = List.of(secondSection, firstSection);
    final SectionsSorter sectionsSorter = SectionsSorter.use(sections);

    //when
    final List<Section> sortedSections = sectionsSorter.getSortedSections();

    //then
    assertThat(sortedSections.get(0))
        .usingRecursiveComparison()
        .isEqualTo(firstSection);

    assertThat(sortedSections.get(1))
        .usingRecursiveComparison()
        .isEqualTo(secondSection);
  }
}
