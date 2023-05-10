package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SectionTest {

    /**
     * 기존 : A-B-C-D-E
     * 추가 : D-F
     * 결과 : CanNotInsertSectionException
     */
    @Test
    @DisplayName("addNext() : 중간에 끼어드는 섹션의 길이가 현재 연결된 섹션의 길이보다 크면 CanNotInsertSectionException가 발생한다.")
    void test_addNext_CanNotInsertSectionException() throws Exception {
        //given
        final Stations stations1 = new Stations(new Station("A"), new Station("B"), 5);
        final Stations stations2 = new Stations(new Station("B"), new Station("C"), 4);
        final Stations stations3 = new Stations(new Station("C"), new Station("D"), 3);
        final Stations stations4 = new Stations(new Station("D"), new Station("E"), 3);

        Section 시작_섹션 = new Section(stations1);
        final Section section2 = new Section(stations2);
        시작_섹션.addNext(section2);

        final Section section3 = new Section(stations3);
        시작_섹션.addNext(section3);

        final Section section4 = new Section(stations4);
        시작_섹션.addNext(section4);

        final Station current = new Station("D");
        final Station next = new Station("F");

        final Stations 새로운_stations = new Stations(current, next, 5);

        final Section 새로운_섹션 = new Section(새로운_stations);

        //when & then
        assertThatThrownBy(() -> 시작_섹션.addNext(새로운_섹션))
                .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * 기존 : A-B-C-D-E
     * 추가 : D-F
     * 결과 : A-B-C-D-F-E
     */
    @Test
    @DisplayName("addNext() : 중간에 끼어드는 섹션의 길이가 현재 연결된 섹션의 길이보다 작으면 섹션을 추가할 수 있다.")
    void test_addNext() throws Exception {
        //given
        final Stations stations1 = new Stations(new Station("A"), new Station("B"), 5);
        final Stations stations2 = new Stations(new Station("B"), new Station("C"), 4);
        final Stations stations3 = new Stations(new Station("C"), new Station("D"), 3);
        final Stations stations4 = new Stations(new Station("D"), new Station("E"), 3);

        Section 시작_섹션 = new Section(stations1);
        final Section section2 = new Section(stations2);
        시작_섹션.addNext(section2);

        final Section section3 = new Section(stations3);
        시작_섹션.addNext(section3);

        final Section section4 = new Section(stations4);
        시작_섹션.addNext(section4);

        final Station current = new Station("D");
        final Station next = new Station("F");

        final Stations 새로운_stations = new Stations(current, next, 2);

        final Section 새로운_섹션 = new Section(새로운_stations);

        //when
        시작_섹션.addNext(새로운_섹션);

        //then
        assertAll(
                () -> assertEquals(section3.getTo(), 새로운_섹션),
                () -> assertEquals(새로운_섹션.getTo(), section4),
                () -> assertEquals(1, 새로운_섹션.getTo().getStations().getDistance()
                )
        );
    }

    /**
     * 기존 : A-B-C-D-E
     * 추가 : E-F
     * 결과 : A-B-C-D-E-F
     */
    @Test
    @DisplayName("addNext() : 새로운 섹션을 맨 마지막에 추가할 수 있다.")
    void test_addNext_last() throws Exception {
        //given
        final Stations stations1 = new Stations(new Station("A"), new Station("B"), 5);
        final Stations stations2 = new Stations(new Station("B"), new Station("C"), 4);
        final Stations stations3 = new Stations(new Station("C"), new Station("D"), 3);
        final Stations stations4 = new Stations(new Station("D"), new Station("E"), 3);

        Section 시작_섹션 = new Section(stations1);
        final Section section2 = new Section(stations2);
        시작_섹션.addNext(section2);

        final Section section3 = new Section(stations3);
        시작_섹션.addNext(section3);

        final Section section4 = new Section(stations4);
        시작_섹션.addNext(section4);

        final Station current = new Station("E");
        final Station next = new Station("F");

        final Stations 새로운_stations = new Stations(current, next, 2);

        final Section 새로운_섹션 = new Section(새로운_stations);

        //when
        시작_섹션.addNext(새로운_섹션);

        //then
        assertAll(
                () -> assertEquals(section4.getTo(), 새로운_섹션)
        );
    }
}
