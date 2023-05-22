package subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import subway.section.domain.Section;
import subway.section.domain.Sections;
import subway.station.domain.Station;

class LineTest {

  @Test
  void instance() {
    Assertions.assertDoesNotThrow(() -> new Line("2호선역"));
  }

  @Test
  void add() {
    final Line line = new Line("2호선역");

    line.add(Section.of(new Station("잠실새내역"), new Station("잠실역"), 5));

    assertFalse(line.getSections().isEmpty());
  }
}
