package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LineTest {

    /**
     * 기존 : A -> B
     * 추가 : B -> C
     * 결과 : A -> B -> C
     */
    @Test
    @DisplayName("add() : Line의 맨 끝에 새로운 섹션을 추가할 수 있다.")
    void test_add_last() throws Exception {
        //given
        final Stations stations1 = new Stations(new Station("A"), new Station("B"), 5);
        final Stations stations2 = new Stations(new Station("B"), new Station("C"), 4);

        Section 출발_섹션 = new Section(stations1);
        final Line line = new Line("2호선");

        line.add(출발_섹션);

        final Section newSection = new Section(stations2);

        //when
        line.add(newSection);

        //then
        assertAll(
                () -> assertEquals(line.getStarter(), 출발_섹션),
                () -> assertEquals(line.getStarter().getTo(), newSection)
        );
    }

    /**
     * 기존 : `A` -> B -> C -> D -> E
     * 추가 : D -> F
     * 결론 : `A` -> B -> C -> D -> F -> E
     */
    @Test
    @DisplayName("add() : Line의 중간에 새로운 섹션을 추가할 수 있다.")
    void test_add_intermediate() throws Exception {
        //given
        final Stations stations1 = new Stations(new Station("A"), new Station("B"), 5);
        final Stations stations2 = new Stations(new Station("B"), new Station("C"), 4);
        final Stations stations3 = new Stations(new Station("C"), new Station("D"), 3);
        final Stations stations4 = new Stations(new Station("D"), new Station("E"), 3);

        Section 헤드_섹션 = new Section(stations1);

        final Line line = new Line("2호선");

        line.add(헤드_섹션);

        final Section section2 = new Section(stations2);
        line.add(section2);

        final Section section3 = new Section(stations3);
        line.add(section3);

        final Section section4 = new Section(stations4);
        line.add(section4);

        final Station current = new Station("D");
        final Station next = new Station("F");

        final Stations 새로운_stations = new Stations(current, next, 2);

        final Section 새로운_섹션 = new Section(새로운_stations);

        //when
        line.add(새로운_섹션);

        //then
        assertEquals(section3.getTo(), 새로운_섹션);
    }

    /**
     * 기존 : A -> B -> C
     * 추가 : Z -> A
     * 결론 : Z -> A -> B -> C
     */
    @Test
    @DisplayName("add() : 새롭게 추가된 섹션이 Line의 새로운 Starter가 될 수 있다.")
    void test_add_newStarter() throws Exception {
        //given
        final Stations stations1 = new Stations(new Station("A"), new Station("B"), 5);
        final Stations stations2 = new Stations(new Station("B"), new Station("C"), 4);
        final Stations stations3 = new Stations(new Station("Z"), new Station("A"), 4);

        Section 스타터_바뀔_예정_섹션 = new Section(stations1);
        final Line line = new Line("2호선");

        line.add(스타터_바뀔_예정_섹션);

        final Section section2 = new Section(stations2);
        line.add(section2);

        final Section 새로운_섹션 = new Section(stations3);

        //when
        line.add(새로운_섹션);

        //then
        assertAll(
                () -> assertEquals(line.getStarter(), 새로운_섹션),
                () -> assertEquals(새로운_섹션.getTo(), 스타터_바뀔_예정_섹션)
        );
    }

    @Test
    @DisplayName("add() : Line에 새롭게 추가되지 못하는 섹션을 넣을 경우 IllegalArgumentException이 발생할 수 있다.")
    void test_add_IllegalArgumentException() throws Exception {
        //given
        final Stations stations1 = new Stations(new Station("A"), new Station("B"), 5);
        final Stations stations2 = new Stations(new Station("B"), new Station("C"), 4);
        final Stations stations3 = new Stations(new Station("B"), new Station("A"), 4);

        Section 스타터_바뀔_예정_섹션 = new Section(stations1);
        final Line line = new Line("2호선");

        line.add(스타터_바뀔_예정_섹션);

        final Section section2 = new Section(stations2);
        line.add(section2);

        final Section 새로운_섹션 = new Section(stations3);

        //when & then
        assertThatThrownBy(() -> line.add(새로운_섹션))
                .isInstanceOf(IllegalArgumentException.class);
    }


    /**
     * 기존 : A -> B -> C -> D
     * 삭제 : B
     * 결론 : A -> C -> D
     */
    @Test
    @DisplayName("delete() : Line 중간에 있는 Section을 삭제할 수 있다.")
    void test_delete() throws Exception {
        //given
        final Stations stations1 = new Stations(new Station("A"), new Station("B"), 5);
        final Stations stations2 = new Stations(new Station("B"), new Station("C"), 4);
        final Stations stations3 = new Stations(new Station("C"), new Station("D"), 3);

        Section starter = new Section(stations1);
        final Line line = new Line("2호선");

        line.add(starter);

        final Section section2 = new Section(stations2);
        line.add(section2);

        final Section section3 = new Section(stations3);
        line.add(section3);

        //when
        line.deleteTargetStation(new Station("B"));

        //then
        assertAll(
                () -> assertEquals(starter.getTo(), section3),
                () -> assertNull(section2.getTo()),
                () -> assertEquals(9, starter.getStations().getDistance()),
                () -> assertTrue(starter.getStations().getNext()
                                        .isSame(section2.getStations().getNext()))
        );
    }

    /**
     * 기존 : A -> B -> C -> D
     * 삭제 : A
     * 결론 : B -> C -> D
     */
    @Test
    @DisplayName("delete() : Line의 starter를 삭제할 수 있다.")
    void test_delete_starter() throws Exception {
        //given
        final Stations stations1 = new Stations(new Station("A"), new Station("B"), 5);
        final Stations stations2 = new Stations(new Station("B"), new Station("C"), 4);
        final Stations stations3 = new Stations(new Station("C"), new Station("D"), 3);

        Section starter = new Section(stations1);
        final Line line = new Line("2호선");

        line.add(starter);

        final Section section2 = new Section(stations2);
        line.add(section2);

        final Section section3 = new Section(stations3);
        line.add(section3);

        //when
        line.deleteTargetStation(new Station("A"));

        //then
        assertAll(
                () -> assertEquals(section2.getTo(), section3),
                () -> assertEquals(line.getStarter(), section2),
                () -> assertNull(starter.getTo())
        );
    }

    /**
     * 기존 : A -> B -> C -> D
     * 삭제 : D
     * 결과 : A -> B -> C
     */
    @Test
    @DisplayName("delete() : Line의 마지막 역을 삭제할 수 있다.")
    void test_delete_last_station() throws Exception {
        //given
        final Stations stations1 = new Stations(new Station("A"), new Station("B"), 5);
        final Stations stations2 = new Stations(new Station("B"), new Station("C"), 4);
        final Stations stations3 = new Stations(new Station("C"), new Station("D"), 3);

        Section starter = new Section(stations1);
        final Line line = new Line("2호선");

        line.add(starter);

        final Section section2 = new Section(stations2);
        line.add(section2);

        final Section section3 = new Section(stations3);
        line.add(section3);

        //when
        line.deleteTargetStation(new Station("D"));

        //then
        assertNull(section2.getTo());
    }

    /**
     * 기존 : A -> B
     * 삭제 : B
     * 결과 : Line 삭제
     */
    @Test
    @DisplayName("delete() : Line 에 Section 이 하나밖에 없을 때, 역을 삭제할 경우 Line이 사라진다.")
    void test_delete_line() throws Exception {
        //given
        final Stations stations = new Stations(new Station("A"), new Station("B"), 5);
        final Line line = new Line("2호선");

        line.add(new Section(stations));

        //when
        line.deleteTargetStation(new Station("B"));

        //then
        assertNull(line.getStarter());
    }
}
