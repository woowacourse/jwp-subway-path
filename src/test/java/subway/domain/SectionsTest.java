package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@DisplayName("Sections 테스트")
class SectionsTest {

    private final Station 잠실나루 = Station.of(1L, "잠실나루");
    private final Station 잠실 = Station.of(2L, "잠실");
    private final Station 강변 = Station.of(3L, "강변");
    private final Line _2호선 = Line.of(1L, "2호선", "초록색");

    @Test
    @DisplayName("역마다 연결된 구간 정보를 관리한다.")
    void sectionsTest() {
        // given
        Section section1 = Section.of(1L, _2호선, 잠실나루, 잠실, 10);
        Section section2 = Section.of(2L, _2호선, 강변, 잠실나루, 5);

        List<Section> sections = new ArrayList<>(List.of(section1, section2));

        // then
        assertDoesNotThrow(() -> Sections.from(sections));
    }

    @Nested
    @DisplayName("신규 Section 생성 테스트")
    class AddSectionTests {

        @Nested
        @DisplayName("노선에 처음 역이 추가되는 경우")
        class AddInitSectionTests {

            @Test
            @DisplayName("노선에 처음 추가되는 구간을 생성할 수 있다.")
            void addFirstSectionTest() {
                // given
                Sections sections = Sections.from(new ArrayList<>());

                // when
                sections.addSection(_2호선, 잠실나루, 잠실, 10);

                // then
                assertThat(sections.countOfStations()).isEqualTo(2);
            }
        }

        @Nested
        @DisplayName("노선의 기존 역을 기준으로 추가되는 경우")
        class AddSectionByExistedStationTests {

            @Nested
            @DisplayName("상행 방향 역이 신규역으로 등록되는 경우")
            class AddSectionWithNewUpward {

                @Test
                @DisplayName("구간 사이에 역을 추가할 수 있다.")
                void addNewUpwardBetweenSectionCase() {
                    //given
                    Section section = Section.of(1L, _2호선, 강변, 잠실, 10);
                    Sections sections = Sections.from(new ArrayList<>(List.of(section)));

                    //when
                    sections.addSection(_2호선, 잠실나루, 잠실, 3);
                    List<Section> lineSections = sections.findLineSections(_2호선);

                    //then
                    assertThat(lineSections).contains(
                            Section.of(_2호선, 강변, 잠실나루, 7),
                            Section.of(_2호선, 잠실나루, 잠실, 3)
                    );
                }

                @Test
                @DisplayName("신규 역과의 거리가 기존 역들 간의 거리를 초과하면 예외처리한다.")
                void validateNewDistanceExceedingExistedDistance() {
                    //given
                    Section section = Section.of(1L, _2호선, 강변, 잠실, 10);
                    Sections sections = Sections.from(new ArrayList<>(List.of(section)));


                    //then
                    assertThatThrownBy(() -> sections.addSection(_2호선, 잠실나루, 잠실, 20))
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessageContaining(
                                    "[ERROR] 새로운 역과 기존 역 사이의 거리는 기존 구간의 거리 이상일 수 없습니다."
                            );
                }

                @Test
                @DisplayName("종착 구간 마지막에 역을 추가할 수 있다.")
                void addNewUpwardAtLastSectionCase() {
                    //given
                    Section section = Section.of(1L, _2호선, 잠실나루, 잠실, 3);
                    Sections sections = Sections.from(new ArrayList<>(List.of(section)));

                    //when
                    sections.addSection(_2호선, 강변, 잠실나루, 7);
                    List<Section> lineSections = sections.findLineSections(_2호선);

                    //then
                    assertThat(lineSections).contains(
                            Section.of(_2호선, 강변, 잠실나루, 7),
                            Section.of(_2호선, 잠실나루, 잠실, 3)
                    );
                }
            }

            @Nested
            @DisplayName("하행 방향 역이 신규역으로 등록되는 경우")
            class AddSectionWithNewDownward {

                @Test
                @DisplayName("구간 사이에 역을 추가할 수 있다.")
                void addNewDownwardBetweenSectionCase() {
                    //given
                    Section section = Section.of(1L, _2호선, 강변, 잠실, 10);
                    Sections sections = Sections.from(new ArrayList<>(List.of(section)));

                    //when
                    sections.addSection(_2호선, 강변, 잠실나루, 7);
                    List<Section> lineSections = sections.findLineSections(_2호선);

                    //then
                    assertThat(lineSections).contains(
                            Section.of(_2호선, 강변, 잠실나루, 7),
                            Section.of(_2호선, 잠실나루, 잠실, 3)
                    );
                }

                @Test
                @DisplayName("신규 역과의 거리가 기존 역들 간의 거리를 초과하면 예외처리한다.")
                void validateNewDistanceExceedingExistedDistance() {
                    //given
                    Section section = Section.of(1L, _2호선, 강변, 잠실, 10);
                    Sections sections = Sections.from(new ArrayList<>(List.of(section)));


                    //then
                    assertThatThrownBy(() -> sections.addSection(_2호선, 강변, 잠실나루, 20))
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessageContaining(
                                    "[ERROR] 새로운 역과 기존 역 사이의 거리는 기존 구간의 거리 이상일 수 없습니다."
                            );
                }

                @Test
                @DisplayName("종착 구간 마지막에 역을 추가할 수 있다.")
                void addNewDownwardAtLastSectionCase() {
                    //given
                    Section section = Section.of(1L, _2호선, 잠실나루, 잠실, 3);
                    Sections sections = Sections.from(new ArrayList<>(List.of(section)));

                    //when
                    sections.addSection(_2호선, 강변, 잠실나루, 7);
                    List<Section> lineSections = sections.findLineSections(_2호선);

                    //then
                    assertThat(lineSections).contains(
                            Section.of(_2호선, 강변, 잠실나루, 7),
                            Section.of(_2호선, 잠실나루, 잠실, 3)
                    );
                }
            }

            @Test
            @DisplayName("상행역과 하행역이 이미 노선에 등록되어 있는 경우 예외처리한다.")
            void validateDuplicatedSectionTest() {
                //given
                Section section = Section.of(1L, _2호선, 잠실나루, 잠실, 10);
                Sections sections = Sections.from(new ArrayList<>(List.of(section)));

                //then

                assertThatThrownBy(() -> sections.addSection(_2호선, 잠실나루, 잠실, 5))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("[ERROR] 상행, 하행 방향 역이 해당 노선에 이미 등록되어 있습니다.");
            }
        }
    }

    @Nested
    @DisplayName("기존 section 삭제 테스트")
    class RemoveSectionTests {

        @Test
        @DisplayName("삭제할 역이 종착역이 아닌 경우 삭제할 역의 상행 방향 역과 하행 방향 역이 이어진다.")
        void removeMiddleStationInLineTest() {
            //given
            Section section = Section.of(1L, _2호선, 강변, 잠실, 10);
            Sections sections = Sections.from(new ArrayList<>(List.of(section)));
            sections.addSection(_2호선, 잠실나루, 잠실, 3);

            //when
            sections.removeStationFromLine(_2호선, 잠실나루);
            List<Section> lineSections = sections.findLineSections(_2호선);

            //then
            assertThat(lineSections).containsExactly(
                    Section.of(_2호선, 강변, 잠실, 10)
            );
        }

        @Test
        @DisplayName("삭제할 역이 종착역인 경우 종착역만 삭제한다.")
        void removeEndStationInLineTest() {
            //given
            Section section = Section.of(1L, _2호선, 강변, 잠실, 10);
            Sections sections = Sections.from(new ArrayList<>(List.of(section)));
            sections.addSection(_2호선, 잠실나루, 잠실, 3);

            //when
            sections.removeStationFromLine(_2호선, 잠실);
            List<Section> lineSections = sections.findLineSections(_2호선);

            //then
            assertThat(lineSections).containsExactly(
                    Section.of(_2호선, 강변, 잠실나루, 7)
            );
        }

        @Test
        @DisplayName("노선에 역이 2개만 남았을 때 삭제하는 경우 모든 역을 삭제한다.")
        void removeAllStationsWhenLeftOnlyTwoTest() {
            //given
            Section section = Section.of(1L, _2호선, 잠실나루, 잠실, 3);
            Sections sections = Sections.from(new ArrayList<>(List.of(section)));

            //when
            sections.removeStationFromLine(_2호선, 잠실);
            List<Section> lineSections = sections.findLineSections(_2호선);

            //then
            assertThat(lineSections).isEmpty();
        }


        @Test
        @DisplayName("노선에 등록되지 않은 역을 제거하려 하는 경우 예외처리한다.")
        void validateDeleteUnregisteredStationTest() {
            //given
            Section section = Section.of(1L, _2호선, 강변, 잠실, 10);
            Sections sections = Sections.from(new ArrayList<>(List.of(section)));
            sections.addSection(_2호선, 잠실나루, 잠실, 3);

            Station 신사역 = Station.of(4L, "신사역");

            //then
            assertThatThrownBy(() -> sections.removeStationFromLine(_2호선, 신사역))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR] 노선에 등록되어 있지 않은 역입니다.");
        }

    }
}
