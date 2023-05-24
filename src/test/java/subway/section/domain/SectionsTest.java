package subway.section.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.station.domain.Station;

class SectionsTest {

  private static final Station 잠실나루역 = new Station(3L, "잠실나루역");
  private static final Station 잠실역 = new Station(1L, "잠실역");
  private static final Station 잠실새내역 = new Station(2L, "잠실새내역");

    @DisplayName("두개의 역을 이용하여 노선을 초기화 한다.")
    @Test
    void initializeLine() {
      //given
      final Sections sections = Sections.empty();

      //when
      sections.initializeSections(Section.of(잠실새내역, 잠실역, 3));

      //then
      assertAll(
          () -> assertThat(sections.getSections()).hasSize(1),
          () -> assertThat(sections.getSections().get(0).getUpStation()).isEqualTo(잠실새내역),
          () -> assertThat(sections.getSections().get(0).getDownStation()).isEqualTo(잠실역),
          () -> assertThat(sections.getSections().get(0).getDistance()).isEqualTo(3)
      );
    }

  @DisplayName("이미 초기화 된 노선을 초기화하면 예외 처리")
  @Test
  void initializeLineWithFail() {
    //given
    final Sections sections = Sections.empty();
    sections.initializeSections(Section.of(잠실새내역, 잠실역, 3));

    //when
    //then
    Assertions.assertThatThrownBy(() -> sections.initializeSections(Section.of(잠실역, 잠실새내역, 4)));
  }

    @DisplayName("노선의 제일 앞에 역을 추가한다")
    @Test
    void addStationStartOfLine() {
        //given
        final Sections sections = Sections.empty();
        sections.initializeSections(Section.of(잠실역, 잠실나루역, 3));

        //when
        sections.addSection(Section.of(잠실새내역, 잠실역, 4));

        //then
        assertAll(
            () -> assertThat(sections.getSections()).hasSize(2),
            () -> assertThat(sections.getSections().get(0).getUpStation()).isEqualTo(잠실새내역),
            () -> assertThat(sections.getSections().get(0).getDownStation()).isEqualTo(잠실역),
            () -> assertThat(sections.getSections().get(0).getDistance()).isEqualTo(4)
        );
    }

    @DisplayName("노선의 제일 뒤에 역을 추가한다")
    @Test
    void addStationEndOfLine() {
        //given
        final Sections sections = Sections.empty();
        sections.initializeSections(Section.of(잠실새내역, 잠실역, 3));

        //when
        sections.addSection(Section.of(잠실역, 잠실나루역, 4));

        //then
        assertAll(
            () -> assertThat(sections.getSections()).hasSize(2),
            () -> assertThat(sections.getSections().get(1).getUpStation()).isEqualTo(잠실역),
            () -> assertThat(sections.getSections().get(1).getDownStation()).isEqualTo(잠실나루역),
            () -> assertThat(sections.getSections().get(1).getDistance()).isEqualTo(4)
        );
    }

    @DisplayName("노선의 중간에 역을 추가한다 (나누어질 구간의 왼쪽으로 들어갈 떄)")
    @Test
    void addStationMiddleOfLineUpToDown() {
        //given
        final Sections sections = Sections.empty();
        sections.initializeSections(Section.of(잠실새내역, 잠실나루역, 3));

        //when
        sections.addSection(Section.of(잠실새내역, 잠실역, 2));

        //then
        assertAll(
            () -> assertThat(sections.getSections()).hasSize(2),
            () -> assertThat(sections.getSections().get(0).getUpStation()).isEqualTo(잠실새내역),
            () -> assertThat(sections.getSections().get(0).getDownStation()).isEqualTo(잠실역),
            () -> assertThat(sections.getSections().get(0).getDistance()).isEqualTo(2),
            () -> assertThat(sections.getSections().get(1).getUpStation()).isEqualTo(잠실역),
            () -> assertThat(sections.getSections().get(1).getDownStation()).isEqualTo(잠실나루역),
            () -> assertThat(sections.getSections().get(1).getDistance()).isEqualTo(1)
        );
    }

    @DisplayName("노선의 중간에 역을 추가한다 (나누어질 구간의 오른쪽으로 들어갈 떄)")
    @Test
    void addStationMiddleOfLineDownToUp() {
        //given
        final Sections sections = Sections.empty();
        sections.initializeSections(Section.of(잠실새내역, 잠실나루역, 3));

        //when
        sections.addSection(Section.of(잠실역, 잠실나루역, 2));

        //then
        assertAll(
            () -> assertThat(sections.getSections()).hasSize(2),
            () -> assertThat(sections.getSections().get(0).getUpStation()).isEqualTo(잠실새내역),
            () -> assertThat(sections.getSections().get(0).getDownStation()).isEqualTo(잠실역),
            () -> assertThat(sections.getSections().get(0).getDistance()).isEqualTo(1),
            () -> assertThat(sections.getSections().get(1).getUpStation()).isEqualTo(잠실역),
            () -> assertThat(sections.getSections().get(1).getDownStation()).isEqualTo(잠실나루역),
            () -> assertThat(sections.getSections().get(1).getDistance()).isEqualTo(2)
        );
    }

  @DisplayName("노선의 중간에 역을 추가할 때 넣을 거리가 더 큰 경우 예외처리")
  @Test
  void addStationWithLonger() {
    //given
    final Sections sections = Sections.empty();
    sections.initializeSections(Section.of(잠실새내역, 잠실나루역, 3));

    //when
    //then
    Assertions.assertThatThrownBy(() -> sections.addSection(Section.of(잠실역, 잠실나루역, 4)))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("노선이 초기화 되지 않고 역을 추가하는 경우 예외처리")
  @Test
  void addStationWithoutInitialize() {
    //given
    final Sections sections = Sections.empty();

    //when
    //then
    Assertions.assertThatThrownBy(() -> sections.addSection(Section.of(잠실역, 잠실나루역, 4)))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("추가하는 역들이 이미 구간을 이루고 있는 경우 예외처리")
  @Test
  void addExistedStations() {
    //given
    final Sections sections = Sections.empty();
    sections.initializeSections(Section.of(잠실새내역, 잠실나루역, 3));

    //when
    //then
    Assertions.assertThatThrownBy(() -> sections.addSection(Section.of(잠실새내역, 잠실나루역, 4)))
        .isInstanceOf(IllegalArgumentException.class);
  }

    @DisplayName("노선의 제일 앞에 있는 역을 제거한다.")
    @Test
    void removeStationStartOfLine() {
        //given
        final Sections sections = Sections.empty();
        sections.initializeSections(Section.of(잠실새내역, 잠실역, 3));
        sections.addSection(Section.of(잠실역, 잠실나루역, 2));

        //when
        sections.removeStation(잠실새내역);

        //then
        assertAll(
            () -> assertThat(sections.getSections()).hasSize(1),
            () -> assertThat(sections.getSections().get(0).getUpStation()).isEqualTo(잠실역),
            () -> assertThat(sections.getSections().get(0).getDownStation()).isEqualTo(잠실나루역),
            () -> assertThat(sections.getSections().get(0).getDistance()).isEqualTo(2)
        );
    }

    @DisplayName("노선의 제일 뒤에 있는 역을 제거한다.")
    @Test
    void removeStationEndOfLine() {
      //given
      final Sections sections = Sections.empty();
      sections.initializeSections(Section.of(잠실새내역, 잠실역, 3));
      sections.addSection(Section.of(잠실역, 잠실나루역, 2));

      //when
      sections.removeStation(잠실나루역);

      //then
      assertAll(
          () -> assertThat(sections.getSections()).hasSize(1),
          () -> assertThat(sections.getSections().get(0).getUpStation()).isEqualTo(잠실새내역),
          () -> assertThat(sections.getSections().get(0).getDownStation()).isEqualTo(잠실역),
          () -> assertThat(sections.getSections().get(0).getDistance()).isEqualTo(3)
      );
    }

    @DisplayName("노선의 중간에서 역을 제거한다.")
    @Test
    void removeStation() {
        //given
      final Sections sections = Sections.empty();
      sections.initializeSections(Section.of(잠실새내역, 잠실역, 3));
      sections.addSection(Section.of(잠실역, 잠실나루역, 2));

        //when
        sections.removeStation(잠실역);

        //then
        assertAll(
            () -> assertThat(sections.getSections()).hasSize(1),
            () -> assertThat(sections.getSections().get(0).getUpStation()).isEqualTo(잠실새내역),
            () -> assertThat(sections.getSections().get(0).getDownStation()).isEqualTo(잠실나루역),
            () -> assertThat(sections.getSections().get(0).getDistance()).isEqualTo(5)
        );
    }
}
