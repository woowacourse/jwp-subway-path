package subway.domain;

import org.junit.jupiter.api.Test;
import subway.domain.strategy.AddSectionStrategy;
import subway.domain.strategy.UpStrategy;

import java.util.ArrayList;
import java.util.List;

class LineTest {


    @Test
    void dd() {
        Station station = new Station(1L, "신림");
        Station station2 = new Station(2L, "봉천");
        Section section = new Section(station, station2, new Distance(3), 2);
        Line line = new Line(1L, "2호선", "초록", new ArrayList<>(List.of(section)));

        AddSectionStrategy addSectionStrategy = line.readyToSave(new Station(1L, "신림"), new Station(3L, "서울대"), new UpStrategy(), 2);
        System.out.println(addSectionStrategy.getClass());
    }
}