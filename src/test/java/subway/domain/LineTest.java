package subway.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {

    @Test
    void 역에_새로운_경로를_추가할_수_있다() {
        ///given
        final Sections sections = new Sections(List.of(new Section(new Station("충무로"), new Station("동대입구"), 10L), new Section(new Station("동대입구"), new Station("약수"), 10L)));
        final Section section = new Section(new Station("약수"), new Station("금호"), 10L);
        final Line line = new Line("3호선", "orange", sections);

        ///when,then
        assertThat(line.addSection(section).getSections().copySections().size()).isEqualTo(3);
    }

    @Test
    void 존재하는_역을_삭제할_수_있다() {
        ///given
        final Sections sections = new Sections(List.of(new Section(new Station("충무로"), new Station("동대입구"), 10L), new Section(new Station("동대입구"), new Station("약수"), 10L)));
        final Line line = new Line("3호선", "orange", sections);
        final Station station = new Station("충무로");

        ///when
        final Line newLine = line.buildNewLineDeleted(station);

        ///then
        assertThat(newLine.getSections().copySections().size()).isEqualTo(1);
    }

    @Test
    void 호선_내의_역을_경로대로_반환할_수_있다() {
        ///given
        final Sections sections = new Sections(List.of(new Section(new Station("동대입구"), new Station("약수"), 10L), new Section(new Station("충무로"), new Station("동대입구"), 10L), new Section(new Station("을지로3가"), new Station("충무로"), 10L)));
        final Line line = new Line("3호선", "orange", sections);

        ///when
        final List<Station> route = line.getRoute();

        ///then
        assertThat(route.get(0)).isEqualTo(new Station("을지로3가"));
        assertThat(route.get(1)).isEqualTo(new Station("충무로"));
        assertThat(route.get(2)).isEqualTo(new Station("동대입구"));
    }

}