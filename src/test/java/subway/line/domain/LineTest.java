package subway.line.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import subway.section.domain.Sections;

class LineTest {

  @Test
  void instance() {
    Assertions.assertDoesNotThrow(() -> new Line("2호선역", Sections.empty()));
  }
}
