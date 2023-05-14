package subway.station.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StationTest {

  @Test
  void instance() {
    //given
    //when
    //then
    Assertions.assertDoesNotThrow(() -> new Station("잠실역"));
  }

}
