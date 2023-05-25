package subway.line.domain;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StationsTest {

  @Test
  @DisplayName("isLinked() : 두 역이 이어져있는지 확인할 수 있다.")
  void test_isLinked() throws Exception {
    //given
    final Stations stations1 = new Stations(
        new Station("A"),
        new Station("B"),
        3
    );

    final Stations stations2 = new Stations(
        new Station("B"),
        new Station("C"),
        4
    );

    //when & then
    assertTrue(stations1.isLinked(stations2));
  }
}
