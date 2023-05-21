package subway.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.exception.IllegalAddSectionException;
import subway.exception.IllegalRemoveSectionException;
import java.util.ArrayList;
import java.util.List;

class SectionsTest {

    private Sections sections;
    private final Station 신림역 = new Station(1L, "신림");
    private final Station 봉천역 = new Station(2L, "봉천");
    private final Station 낙성대역 = new Station(3L, "낙성대");
    private final Station 서울대입구역 = new Station(4L, "서울대입구");

    private final Line _2호선 = new Line(1L, "2호선", "green", 0);
    private final Line 신림선 = new Line(2L, "신림선", "청색", 0);

    private final Section _2호선_신림_봉천_거리5 = new Section(1L, _2호선, 신림역, 봉천역, new Distance(5));
    private final Section _2호선_봉천_낙성대_거리5 = new Section(2L, _2호선, 봉천역, 낙성대역, new Distance(5));
    private final Section _2호선_봉천_신림_거리5 = new Section(3L, _2호선, 봉천역, 신림역, new Distance(5));
    private final Section 신림선_신림_봉천_거리9 = new Section(4L, 신림선, 신림역, 봉천역, new Distance(9));
    private final Section _2호선_서울대입구_신림_거리10 = new Section(5L, _2호선, 서울대입구역, 신림역, new Distance(10));


    @BeforeEach
    void setUp() {
        this.sections = new Sections(new ArrayList<>());
    }

    @Test
    void 두_역을_동시에_등록한다() {
        // given
        sections.add(_2호선_신림_봉천_거리5);

        // expect
        assertThat(sections.getSections()).containsExactly(_2호선_신림_봉천_거리5);
    }

    @Test
    void 두_Section을_동시에_등록할_때_기존에_존재하면_예외가_발생한다() {
        assertThatThrownBy(() -> sections.addTwoSections(_2호선_신림_봉천_거리5, _2호선_신림_봉천_거리5))
                .isInstanceOf(IllegalAddSectionException.class);
    }

    @Test
    void 두_Section을_동시에_등록할_때_기존에_존재하는_Section과_방향이_반대면_예외를_발생한다() {
        assertThatThrownBy(() -> sections.addTwoSections(_2호선_신림_봉천_거리5, _2호선_봉천_신림_거리5))
                .isInstanceOf(IllegalAddSectionException.class);
    }

    @Test
    void 두_Section을_동시에_등록할_때_방향이_같으면_예외를_발생한다() {
        assertThatThrownBy(() -> sections.addTwoSections(_2호선_신림_봉천_거리5, 신림선_신림_봉천_거리9))
                .isInstanceOf(IllegalAddSectionException.class);
    }

    @Test
    void 하나의_역을_추가할_때_기존에_존재하면_예외가_발생한다() {
        // given
        sections.add(_2호선_신림_봉천_거리5);

        // expect
        assertThatThrownBy(() -> sections.add(_2호선_신림_봉천_거리5))
                .isInstanceOf(IllegalAddSectionException.class);

        assertThatThrownBy(() -> sections.add(신림선_신림_봉천_거리9))
                .isInstanceOf(IllegalAddSectionException.class);
    }

    @Test
    void 하나의_Section을_등록할_때_기존에_존재하는_Section과_방향만_반대면_예외를_발생한다() {
        // given
        sections.add(_2호선_신림_봉천_거리5);

        // expect
        assertThatThrownBy(() -> sections.add(_2호선_봉천_신림_거리5))
                .isInstanceOf(IllegalAddSectionException.class);
    }

    @Test
    void 하나의_Section을_등록할_때_존재하는_Section과_호선이_다르지만_역의_방향이_같으면_예외를_발생한다() {
        // given
        sections.add(_2호선_신림_봉천_거리5);

        // expect
        assertThatThrownBy(() -> sections.add(신림선_신림_봉천_거리9))
                .isInstanceOf(IllegalAddSectionException.class);
    }

    @Test
    void 특정_호선의_모든_역들을_순서대로_조회한다() {
        // given
        sections.add(_2호선_신림_봉천_거리5);

        // when
        List<Station> allStation = sections.allStations();

        // expect
        assertThat(allStation).containsExactly(신림역, 봉천역);
    }

    @Test
    void 비어있는_호선을_조회하면_아무_역이_없는_결과가_반환된다() {
        List<Station> stations = sections.allStations();

        assertThat(stations.isEmpty()).isTrue();
    }

    @Test
    void 비어있는_호선에_두_역을_추가한다() {
        assertThatThrownBy(() -> sections.addTwoSections(_2호선_신림_봉천_거리5, _2호선_봉천_낙성대_거리5))
                .isInstanceOf(IllegalAddSectionException.class);
    }

    @Test
    void 하나의_호선에_두_개의_Section이_있을_때_맨_앞에_역을_추가한다() {
        // given
        sections.add(_2호선_신림_봉천_거리5);

        // when
        sections.add(_2호선_서울대입구_신림_거리10);

        // then
        assertThat(sections.allStations()).containsExactly(서울대입구역, 신림역, 봉천역);
    }

    @Test
    void 하나의_호선에_두_개_이상의_Section이_있을_때_맨_앞에_역을_추가한다() {
        // given
        sections.add(_2호선_신림_봉천_거리5);
        sections.add(_2호선_봉천_낙성대_거리5);

        // when
        sections.add(_2호선_서울대입구_신림_거리10);

        // then
        assertThat(sections.allStations()).containsExactly(서울대입구역, 신림역, 봉천역, 낙성대역);
    }

    @Test
    void 성공적으로_중간에_역을_추가한다() {
        // given
        sections.add(_2호선_신림_봉천_거리5);

        Section _2호선_신림역_서울대입구역_거리3 = new Section(5L, _2호선, 신림역, 서울대입구역, new Distance(3));
        Section _2호선_서울대입구_봉천역_거리2 = new Section(6L, _2호선, 서울대입구역, 봉천역, new Distance(2));

        // when
        sections.addTwoSections(_2호선_신림역_서울대입구역_거리3, _2호선_서울대입구_봉천역_거리2);

        // then
        assertThat(sections.allStations()).containsExactly(신림역, 서울대입구역, 봉천역);
    }

    @Test
    void 역을_추가할_때_역은_같지만_호선이_다른_경우_예외가_발생한다() {
        // given
        sections.add(_2호선_신림_봉천_거리5);

        Station 샛강역 = new Station(5L, "샛강역");
        Section 신림선_신림_샛강_거리2 = new Section(5L, 신림선, 신림역, 샛강역, new Distance(2));
        Section 신림선_샛강_봉천_거리3 = new Section(6L, 신림선, 샛강역, 봉천역, new Distance(3));

        // expect
        assertThatThrownBy(() -> sections.addTwoSections(신림선_신림_샛강_거리2, 신림선_샛강_봉천_거리3))
                .isInstanceOf(IllegalAddSectionException.class);
    }

    @ParameterizedTest(name = "앞 거리: {0} 뒤 거리: {1} 거리의 합: {2}")
    @CsvSource({"1, 3, 4", "1, 1, 2", "1, 5, 6"})
    void 중간에_역을_추가할_때_거리의_합이_잘못되면_예외가_발생한다(final int upSectionDistance, final int downSectionDistance, final int distanceSum) {
        // given
        sections.add(_2호선_신림_봉천_거리5);

        Section _2호선_신림역_서울대입구역 = new Section(5L, _2호선, 신림역, 서울대입구역, new Distance(upSectionDistance));
        Section _2호선_서울대입구_봉천역 = new Section(6L, _2호선, 서울대입구역, 봉천역, new Distance(downSectionDistance));

        // expect
        assertThatThrownBy(() -> sections.addTwoSections(_2호선_신림역_서울대입구역, _2호선_서울대입구_봉천역))
                .isInstanceOf(IllegalAddSectionException.class);
    }

    @Test
    void 중간에_역을_추가할_때_갈림길이_되면_예외가_발생한다() {
        // given
        sections.add(_2호선_신림_봉천_거리5);

        Section _2호선_신림역_서울대입구역 = new Section(5L, _2호선, 신림역, 서울대입구역, new Distance(10));

        // expect
        assertThatThrownBy(() -> sections.add(_2호선_신림역_서울대입구역))
                .isInstanceOf(IllegalAddSectionException.class);
    }

    @Test
    void 두_개_이상의_Section이_있을_때_끝에_역을_추가한다() {
        // given
        sections.add(_2호선_신림_봉천_거리5);

        // when
        sections.add(_2호선_봉천_낙성대_거리5);

        // then
        assertThat(sections.allStations()).containsExactly(신림역, 봉천역, 낙성대역);
    }

    @Test
    void 노선에_등록된_역이_2개인_경우_하나의_역을_제거하면_두_역이_모두_제거된다() {
        // given
        sections.add(_2호선_신림_봉천_거리5);

        // when
        sections.removeStation(봉천역);

        // then
        assertThat(sections.allStations()).isEmpty();
    }

    @Test
    void 노선에_등록된_역이_3개_이상인_경우_맨_앞의_역을_제거한다() {
        // given
        sections.add(_2호선_신림_봉천_거리5);
        sections.add(_2호선_봉천_낙성대_거리5);

        // when
        sections.removeStation(신림역);

        // then
        assertThat(sections.allStations()).containsExactly(봉천역, 낙성대역);
    }

    @Test
    void 노선에_등록된_역이_3개_이상일_때_중간_역을_제거한다() {
        // given
        sections.add(_2호선_신림_봉천_거리5);
        sections.add(_2호선_봉천_낙성대_거리5);

        // when
        sections.removeStation(봉천역);

        // then
        assertThat(sections.allStations()).containsExactly(신림역, 낙성대역);
    }

    @Test
    void 노선에_등록된_역이_3개_이상인_경우_맨_뒤의_역을_제거한다() {
        // given
        sections.add(_2호선_신림_봉천_거리5);
        sections.add(_2호선_봉천_낙성대_거리5);

        // when
        sections.removeStation(낙성대역);

        // then
        assertThat(sections.allStations()).containsExactly(신림역, 봉천역);
    }

    @Test
    void 존재하지_않은_역으로_삭제를_시도하면_예외가_발생한다() {
        // given
        sections.add(_2호선_신림_봉천_거리5);
        sections.add(_2호선_봉천_낙성대_거리5);

        // expect
        assertThatThrownBy(() -> sections.removeStation(서울대입구역))
                .isInstanceOf(IllegalRemoveSectionException.class);
    }

    @Test
    void 중간의_역이_제거되면_거리가_재배정된다() {
        // given
        sections.add(_2호선_신림_봉천_거리5);
        sections.add(_2호선_봉천_낙성대_거리5);

        // when
        sections.removeStation(봉천역);

        // then
        Section _2호선_신림_낙성대_거리10 = sections.getSections().get(0);
        assertThat(_2호선_신림_낙성대_거리10.getDistance()).isEqualTo(new Distance(10));
    }
}
