package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class SectionTest {

    @Test
    @DisplayName("addNext : 다음 섹션을 등록할 수 있다.")
    void test_addNext() throws Exception {
        //given
        final Station station1 = new Station("A");
        final Station station2 = new Station("B");
        final Station station3 = new Station("C");
        final Station station4 = new Station("D");

        final Stations stations1 = new Stations(station1, station2, 2);
        final Stations stations2 = new Stations(station2, station3, 2);
        final Stations stations3 = new Stations(station3, station4, 2);

        Section 시작_섹션 = new Section(stations1);
        System.out.println("시작_섹션 = " + 시작_섹션.getTo());
        final Section section2 = new Section(stations2);
        시작_섹션.addNext(section2);

        final Section section3 = new Section(stations3);
        시작_섹션.addNext(section3);

        final Station current = new Station("C");
        final Station next = new Station("E");

        final Stations 새로운_stations = new Stations(current, next, 6);

        final Section 새로운_섹션 = new Section(새로운_stations);

        //when
        시작_섹션.addNext(새로운_섹션);

        while (시작_섹션 != null) {
            System.out.println("=====================");
            System.out.println(시작_섹션);

            시작_섹션 = 시작_섹션.getTo();
        }
    }
}
