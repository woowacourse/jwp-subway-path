package subway.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static subway.TestData._2호선;
import static subway.TestData._2호선_봉천_서울대입구_10;
import static subway.TestData._2호선_서울대입구_사당_6;
import static subway.TestData._2호선_신림_봉천_7;
import static subway.TestData.봉천역;
import static subway.TestData.사당역;
import static subway.TestData.서울대입구역;
import static subway.TestData.신림선;
import static subway.TestData.신림역;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.exception.IllegalAddSectionException;
import subway.exception.IllegalRemoveSectionException;
import java.util.ArrayList;

class SectionsTest {

    private Sections sections;

    @BeforeEach
    void setUp() {
        this.sections = new Sections(new ArrayList<>());
    }

    @Test
    void 하나의_Section을_추가한다() {
        sections.add(_2호선_봉천_서울대입구_10);

        assertThat(sections.getSections()).containsExactly(_2호선_봉천_서울대입구_10);
    }

    @Test
    void 두_Section을_동시에_등록할_때_기존에_존재하면_예외가_발생한다() {
        assertThatThrownBy(() -> sections.addTwoSections(_2호선_신림_봉천_7, _2호선_신림_봉천_7))
                .isInstanceOf(IllegalAddSectionException.class);
    }

    @Test
    void 두_Section을_동시에_등록할_때_기존에_존재하는_Section과_방향이_반대면_예외를_발생한다() {
        Section _2호선_봉천_신림_5 = new Section(_2호선, 봉천역, 신림역, new Distance(5));

        assertThatThrownBy(() -> sections.addTwoSections(_2호선_신림_봉천_7, _2호선_봉천_신림_5))
                .isInstanceOf(IllegalAddSectionException.class);
    }

    @Test
    void 두_Section을_동시에_등록할_때_방향이_같으면_예외가_발생한다() {
        Section 신림선_신림_봉천_5 = new Section(신림선, 신림역, 봉천역, new Distance(5));

        assertThatThrownBy(() -> sections.addTwoSections(_2호선_신림_봉천_7, 신림선_신림_봉천_5))
                .isInstanceOf(IllegalAddSectionException.class);
    }

    @Test
    void 하나의_역을_추가할_때_기존에_존재하면_예외가_발생한다() {
        sections.add(_2호선_신림_봉천_7);

        assertThatThrownBy(() -> sections.add(_2호선_신림_봉천_7))
                .isInstanceOf(IllegalAddSectionException.class);
    }

    @Test
    void 하나의_Section을_등록할_때_기존에_존재하는_Section과_방향만_반대면_예외를_발생한다() {
        sections.add(_2호선_신림_봉천_7);

        Section _2호선_봉천_신림_거리7 = new Section(_2호선, 봉천역, 신림역, new Distance(7));

        assertThatThrownBy(() -> sections.add(_2호선_봉천_신림_거리7))
                .isInstanceOf(IllegalAddSectionException.class);
    }

    @Test
    void 하나의_Section을_등록할_때_존재하는_Section과_역의_방향이_같아도_호선이_다르면_예외가_발생하지_않는다() {
        sections.add(_2호선_신림_봉천_7);

        Section 신림선_신림_봉천_9 = new Section(신림선, 신림역, 봉천역, new Distance(9));

        assertThatNoException().isThrownBy(() -> sections.add(신림선_신림_봉천_9));
    }

    @Test
    void 특정_호선의_모든_역들을_순서대로_조회한다() {
        sections.add(_2호선_신림_봉천_7);

        var allStation = sections.allStations();

        assertThat(allStation).containsExactly(신림역, 봉천역);
    }

    @Test
    void 비어있는_호선을_조회하면_아무_역이_없는_결과가_반환된다() {
        var stations = sections.allStations();

        assertThat(stations.isEmpty()).isTrue();
    }

    @Test
    void 비어있는_호선에_두_역을_추가한다() {
        assertThatThrownBy(() -> sections.addTwoSections(_2호선_신림_봉천_7, _2호선_봉천_서울대입구_10))
                .isInstanceOf(IllegalAddSectionException.class);
    }

    @Test
    void 하나의_호선에_두_개의_Section이_있을_때_맨_앞에_역을_추가한다() {
        sections.add(_2호선_봉천_서울대입구_10);
        sections.add(_2호선_서울대입구_사당_6);

        sections.add(_2호선_신림_봉천_7);

        assertThat(sections.allStations()).containsExactly(신림역, 봉천역, 서울대입구역, 사당역);
    }

    @Test
    void 성공적으로_중간에_역을_추가한다() {
        // given
        var _2호선_신림_서울대입구_10 = new Section(_2호선, 신림역, 서울대입구역, new Distance(17));
        sections.add(_2호선_신림_서울대입구_10);

        // when
        sections.addTwoSections(_2호선_신림_봉천_7, _2호선_봉천_서울대입구_10);

        // then
        assertThat(sections.allStations()).containsExactly(신림역, 봉천역, 서울대입구역);
    }

    @Test
    void 역을_추가할_때_역은_같지만_호선이_다른_경우_예외가_발생한다() {
        // given
        sections.add(_2호선_신림_봉천_7);

        Station 샛강역 = new Station(5L, "샛강역");
        Section 신림선_신림_샛강_거리3 = new Section(5L, 신림선, 신림역, 샛강역, new Distance(3));
        Section 신림선_샛강_봉천_거리4 = new Section(6L, 신림선, 샛강역, 봉천역, new Distance(4));

        // expect
        assertThatThrownBy(() -> sections.addTwoSections(신림선_신림_샛강_거리3, 신림선_샛강_봉천_거리4))
                .isInstanceOf(IllegalAddSectionException.class);
    }

    @ParameterizedTest(name = "앞 거리: {0} 뒤 거리: {1} 거리의 합: {2}")
    @CsvSource({"1, 3, 4", "1, 1, 2", "1, 5, 6"})
    void 중간에_역을_추가할_때_거리의_합이_잘못되면_예외가_발생한다(final int upSectionDistance, final int downSectionDistance, final int distanceSum) {
        // given
        sections.add(_2호선_신림_봉천_7);

        Section _2호선_신림역_서울대입구역 = new Section(5L, _2호선, 신림역, 서울대입구역, new Distance(upSectionDistance));
        Section _2호선_서울대입구_봉천역 = new Section(6L, _2호선, 서울대입구역, 봉천역, new Distance(downSectionDistance));

        // expect
        assertThatThrownBy(() -> sections.addTwoSections(_2호선_신림역_서울대입구역, _2호선_서울대입구_봉천역))
                .isInstanceOf(IllegalAddSectionException.class);
    }

    @Test
    void 중간에_역을_추가할_때_갈림길이_되면_예외가_발생한다() {
        // given
        sections.add(_2호선_신림_봉천_7);

        Section _2호선_신림역_서울대입구역 = new Section(5L, _2호선, 신림역, 서울대입구역, new Distance(10));

        // expect
        assertThatThrownBy(() -> sections.add(_2호선_신림역_서울대입구역))
                .isInstanceOf(IllegalAddSectionException.class);
    }

    @Test
    void 두_개_이상의_Section이_있을_때_끝에_역을_추가한다() {
        // given
        sections.add(_2호선_신림_봉천_7);

        // when
        sections.add(_2호선_봉천_서울대입구_10);

        // then
        assertThat(sections.allStations()).containsExactly(신림역, 봉천역, 서울대입구역);
    }

    @Test
    void 노선에_등록된_역이_2개인_경우_하나의_역을_제거하면_두_역이_모두_제거된다() {
        // given
        sections.add(_2호선_신림_봉천_7);

        // when
        sections.removeStation(봉천역);

        // then
        assertThat(sections.allStations()).isEmpty();
    }

    @Test
    void 노선에_등록된_역이_3개_이상인_경우_맨_앞의_역을_제거한다() {
        // given
        sections.add(_2호선_신림_봉천_7);
        sections.add(_2호선_봉천_서울대입구_10);

        // when
        sections.removeStation(신림역);

        // then
        assertThat(sections.allStations()).containsExactly(봉천역, 서울대입구역);
    }

    @Test
    void 노선에_등록된_역이_3개_이상일_때_중간_역을_제거한다() {
        // given
        sections.add(_2호선_신림_봉천_7);
        sections.add(_2호선_봉천_서울대입구_10);

        // when
        sections.removeStation(봉천역);

        // then
        assertThat(sections.allStations()).containsExactly(신림역, 서울대입구역);
    }

    @Test
    void 노선에_등록된_역이_3개_이상인_경우_맨_뒤의_역을_제거한다() {
        // given
        sections.add(_2호선_신림_봉천_7);
        sections.add(_2호선_봉천_서울대입구_10);

        // when
        sections.removeStation(서울대입구역);

        // then
        assertThat(sections.allStations()).containsExactly(신림역, 봉천역);
    }

    @Test
    void 존재하지_않은_역으로_삭제를_시도하면_예외가_발생한다() {
        // given
        sections.add(_2호선_신림_봉천_7);
        sections.add(_2호선_봉천_서울대입구_10);

        // expect
        assertThatThrownBy(() -> sections.removeStation(사당역))
                .isInstanceOf(IllegalRemoveSectionException.class);
    }

    @Test
    void 중간의_역이_제거되면_거리가_재배정된다() {
        // given
        sections.add(_2호선_신림_봉천_7);
        sections.add(_2호선_봉천_서울대입구_10);

        // when
        sections.removeStation(봉천역);

        // then
        Section _2호선_신림_서울대입구_거리17 = sections.getSections().get(0);
        assertThat(_2호선_신림_서울대입구_거리17.getDistance()).isEqualTo(new Distance(17));
    }
}
