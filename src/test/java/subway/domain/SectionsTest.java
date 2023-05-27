package subway.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionsTest {

    @Test
    void 노선에_새로운_경로를_추가할_수_있다() {
        ///given
        final Sections sections = new Sections(List.of(new Section(new Station("충무로"), new Station("동대입구"), 10L), new Section(new Station("동대입구"), new Station("약수"), 10L)));

        ///when
        final Sections newSections = sections.buildNewSections(new Section(new Station("약수"), new Station("금호"), 10L));

        ///then
        assertThat(newSections.copySections().size()).isEqualTo(3);
    }

    @Test
    void 노선은_한_줄로_이어져야_한다() {
        ///given
        final Sections sections = new Sections(List.of(new Section(new Station("충무로"), new Station("동대입구"), 10L), new Section(new Station("동대입구"), new Station("약수"), 10L)));

        ///when,then
        assertThatThrownBy(
                () -> sections.buildNewSections(new Section(new Station("경복궁"), new Station("종로3가"), 10L))
        ).isInstanceOf(IllegalArgumentException.class).hasMessage("이어지지 않는 경로를 추가할 수 없습니다");
    }

    @Test
    void 노선_상행기준_사이_역이_등록되면_기존_경로가_재배치된다() {
        ///given
        final Sections sections = new Sections(List.of(new Section(new Station("충무로"), new Station("동대입구"), 10L), new Section(new Station("동대입구"), new Station("약수"), 10L)));

        ///when
        final Sections newSections = sections.buildNewSections(new Section(new Station("충무로"), new Station("대한극장"), 5L));
        final Section newSection = newSections.copySections()
                .stream()
                .filter(section -> section.equals(new Section(new Station("대한극장"), new Station("동대입구"), 5L)))
                .findFirst().get();

        ///then
        assertThat(newSection).isNotNull();
    }

    @Test
    void 노선_상행기준_사이_역이_등록되면_기존_하행의_거리_정보가_재배치된다() {
        ///given
        final Sections sections = new Sections(List.of(new Section(new Station("충무로"), new Station("동대입구"), 10L), new Section(new Station("동대입구"), new Station("약수"), 10L)));

        ///when
        final Sections newSections = sections.buildNewSections(new Section(new Station("충무로"), new Station("대한극장"), 5L));
        final Section newSection = newSections.copySections()
                .stream()
                .filter(section -> section.equals(new Section(new Station("대한극장"), new Station("동대입구"), 5L)))
                .findFirst().get();

        ///then
        assertThat(newSection.getDistance()).isSameAs(5L);
    }

    @Test
    void 노선_하행기준_사이_역이_등록되면_기존_경로가_재배치된다() {
        ///given
        final Sections sections = new Sections(List.of(new Section(new Station("충무로"), new Station("동대입구"), 10L), new Section(new Station("동대입구"), new Station("약수"), 10L)));

        ///when
        final Sections newSections = sections.buildNewSections(new Section(new Station("대한극장"), new Station("약수"), 5L));
        final Section newSection = newSections.copySections()
                .stream()
                .filter(section -> section.equals(new Section(new Station("동대입구"), new Station("대한극장"), 5L)))
                .findFirst().get();

        ///then
        assertThat(newSection).isNotNull();
    }

    @Test
    void 노선_하행기준_사이_역이_등록되면_기존_하행의_거리_정보가_재배치된다() {
        ///given
        final Sections sections = new Sections(List.of(new Section(new Station("충무로"), new Station("동대입구"), 10L), new Section(new Station("동대입구"), new Station("약수"), 10L)));

        ///when
        final Sections newSections = sections.buildNewSections(new Section(new Station("대한극장"), new Station("약수"), 5L));
        final Section newSection = newSections.copySections()
                .stream()
                .filter(section -> section.equals(new Section(new Station("동대입구"), new Station("대한극장"), 5L)))
                .findFirst().get();

        ///then
        assertThat(newSection.getDistance()).isSameAs(5L);
    }

    @ParameterizedTest
    @ValueSource(longs = {10, 11})
    void 사이에_추가하려는_역의_거리가_기존_경로보다_짧지_않으면_예외가_발생한다(Long newDistance) {
        ///given
        final Sections sections = new Sections(List.of(new Section(new Station("충무로"), new Station("동대입구"), 10L), new Section(new Station("동대입구"), new Station("약수"), 10L)));

        ///when,then
        assertThatThrownBy(
                () -> sections.buildNewSections(new Section(new Station("대한극장"), new Station("약수"), newDistance))
        ).isInstanceOf(IllegalArgumentException.class).hasMessage("새로운 경로 거리는 기존 경로보다 짧아야 합니다");
    }

    @Test
    void 이미_존재하는_역을_등록할_수_없다() {
        ///given
        final Sections sections = new Sections(List.of(new Section(new Station("충무로"), new Station("동대입구"), 10L), new Section(new Station("동대입구"), new Station("약수"), 10L)));

        ///when,then
        assertThatThrownBy(
                () -> sections.buildNewSections(new Section(new Station("충무로"), new Station("동대입구"), 10L))
        ).isInstanceOf(IllegalArgumentException.class).hasMessage("이미 존재하는 경로를 추가할 수 없습니다");
    }

    @Test
    void 삭제하려는_역이_존재하지_않으면_예외가_발생한다() {
        ///given
        final Sections sections = new Sections(List.of(new Section(new Station("충무로"), new Station("동대입구"), 10L), new Section(new Station("동대입구"), new Station("약수"), 10L)));
        final Station station = new Station("종로3가");

        ///when,then
        assertThatThrownBy(
                () -> sections.deleteSection(station)
        ).isInstanceOf(IllegalArgumentException.class).hasMessage("이미 존재하는 역만 삭제할 수 있습니다");
    }

    @Test
    void 노선에_등록된_역이_2개인_경우_하나의_역을_제거하면_두_역이_모두_제거된다() {
        ///given
        final Sections sections = new Sections(List.of(new Section(new Station("충무로"), new Station("동대입구"), 10L)));
        final Station station = new Station("충무로");

        ///when
        Sections deletedSections = sections.deleteSection(station);

        ///then
        assertThat(deletedSections.copySections().size()).isZero();
    }

    @Test
    void 삭제하려는_역이_상행역_종점이면_최상단_경로_하나가_삭제된다() {
        ///given
        final Sections sections = new Sections(List.of(new Section(new Station("충무로"), new Station("동대입구"), 10L), new Section(new Station("동대입구"), new Station("약수"), 10L)));
        final Station targetStation = new Station("충무로");
        final Sections deleted = new Sections(List.of(new Section(new Station("동대입구"), new Station("약수"), 10L)));

        ///when,then
        assertThat(sections.deleteSection(targetStation).copySections().size()).isEqualTo(1);
    }

    @Test
    void 삭제하려는_역이_하행역_종점이면_최상단_경로_하나가_삭제된다() {
        ///given
        final Sections sections = new Sections(List.of(new Section(new Station("충무로"), new Station("동대입구"), 10L), new Section(new Station("동대입구"), new Station("약수"), 10L)));
        final Station targetStation = new Station("약수");
        final Sections deleted = new Sections(List.of(new Section(new Station("충무로"), new Station("동대입구"), 10L)));

        ///when,then
        assertThat(sections.deleteSection(targetStation).copySections().size()).isEqualTo(1);
    }

    @Test
    void 삭제하려는_역이_두_역_사이에_존재하면_두_역이_이어지며_경로가_재배치된다() {
        ///given
        final Sections sections = new Sections(List.of(new Section(new Station("충무로"), new Station("동대입구"), 10L), new Section(new Station("동대입구"), new Station("약수"), 10L)));
        final Station targetStation = new Station("동대입구");
        final Sections deleted = new Sections(List.of(new Section(new Station("충무로"), new Station("약수"), 20L)));

        ///when,then
        assertThat(sections.deleteSection(targetStation).copySections().get(0).getDistance()).isEqualTo(20);

    }

    @Test
    void 이어진_경로_순서대로_정렬해_반환한다() {
        ///given
        final Sections sections = new Sections(List.of(new Section(new Station("동대입구"), new Station("약수"), 10L), new Section(new Station("충무로"), new Station("동대입구"), 10L), new Section(new Station("을지로3가"), new Station("충무로"), 10L)));

        ///when
        final List<Station> route = sections.sortStations();

        ///then
        assertThat(route.get(0)).isEqualTo(new Station("을지로3가"));
        assertThat(route.get(1)).isEqualTo(new Station("충무로"));
        assertThat(route.get(2)).isEqualTo(new Station("동대입구"));
    }
}