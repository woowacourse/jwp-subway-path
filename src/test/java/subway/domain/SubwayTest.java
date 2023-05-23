package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.application.exception.SubwayServiceException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.domain.LineFixture.FIXTURE_LINE_1;
import static subway.domain.SectionFixture.*;
import static subway.domain.StationFixture.*;

class SubwayTest {

    @DisplayName("기존 노선도의 첫 부분에 노선을 추가할 수 있다.")
    @Test
    void addSection_start() {
        Subway subway = Subway.of(FIXTURE_LINE_1, List.of(
                SECTION_MIDDLE_1,
                SECTION_MIDDLE_2,
                SECTION_MIDDLE_3,
                SECTION_END
        ));
        Sections sections = subway.findUpdateSectionsByAddingSection(SECTION_START);
        assertThat(sections.getSections())
                .usingRecursiveComparison()
                .isEqualTo(List.of(SECTION_START));
    }

    @DisplayName("기존 노선도의 마지막 부분에 노선을 추가할 수 있다.")
    @Test
    void addSection_end() {
        Subway subway = Subway.of(FIXTURE_LINE_1, List.of(
                SECTION_MIDDLE_1,
                SECTION_MIDDLE_2
        ));
        Sections sections = subway.findUpdateSectionsByAddingSection(SECTION_MIDDLE_3);
        assertThat(sections.getSections())
                .usingRecursiveComparison()
                .isEqualTo(List.of(SECTION_MIDDLE_3));
    }

    @DisplayName("기존 노선도에서 중간 부분에 노선을 추가할 수 있다.(왼쪽은 노선에 있는 역이면서 양 옆 모두 역이 있고, 오른쪽은 노선에 없는 역)")
    @Test
    void addSection_middle_1() {
        Subway subway = Subway.of(FIXTURE_LINE_1, List.of(
                new Section(FIXTURE_LINE_1, FIXTURE_STATION_1, FIXTURE_STATION_2, new Distance(10)),
                new Section(FIXTURE_LINE_1, FIXTURE_STATION_2, FIXTURE_STATION_4, new Distance(10))
        ));
        Sections sections = subway.findUpdateSectionsByAddingSection(new Section(FIXTURE_LINE_1, FIXTURE_STATION_2, FIXTURE_STATION_3, new Distance(3)));
        assertThat(sections.getSections())
                .usingRecursiveComparison()
                .isEqualTo(List.of(
                        new Section(FIXTURE_LINE_1, FIXTURE_STATION_2, FIXTURE_STATION_3, new Distance(3)),
                        new Section(FIXTURE_LINE_1, FIXTURE_STATION_3, FIXTURE_STATION_4, new Distance(7))
                ));
    }

    @DisplayName("기존 노선도에서 중간 부분에 노선을 추가할 수 있다.(왼쪽은 노선에 있는 역이면서 오른쪽에만 역이 있고, 오른쪽은 노선에 없는 역)")
    @Test
    void addSection_middle_2() {
        Subway subway = Subway.of(FIXTURE_LINE_1, List.of(
                new Section(FIXTURE_LINE_1, FIXTURE_STATION_2, FIXTURE_STATION_4, new Distance(10))
        ));
        Sections sections = subway.findUpdateSectionsByAddingSection(new Section(FIXTURE_LINE_1, FIXTURE_STATION_2, FIXTURE_STATION_3, new Distance(3)));
        assertThat(sections.getSections())
                .usingRecursiveComparison()
                .isEqualTo(List.of(
                        new Section(FIXTURE_LINE_1, FIXTURE_STATION_2, FIXTURE_STATION_3, new Distance(3)),
                        new Section(FIXTURE_LINE_1, FIXTURE_STATION_3, FIXTURE_STATION_4, new Distance(7))
                ));
    }

    @DisplayName("기존 노선도에서 중간 부분에 노선을 추가할 수 있다.(왼쪽은 노선에 없는 역이고, 오른쪽은 노선에 있고, 양 옆에 역이 있는 경우)")
    @Test
    void addSection_middle_3() {
        Subway subway = Subway.of(FIXTURE_LINE_1, List.of(
                new Section(FIXTURE_LINE_1, FIXTURE_STATION_2, FIXTURE_STATION_3, new Distance(10)),
                new Section(FIXTURE_LINE_1, FIXTURE_STATION_3, FIXTURE_STATION_4, new Distance(10))
        ));

        Sections sections = subway.findUpdateSectionsByAddingSection(new Section(FIXTURE_LINE_1, FIXTURE_STATION_1, FIXTURE_STATION_3, new Distance(3)));
        assertThat(sections.getSections())
                .usingRecursiveComparison()
                .isEqualTo(List.of(
                        new Section(FIXTURE_LINE_1, FIXTURE_STATION_1, FIXTURE_STATION_3, new Distance(3)),
                        new Section(FIXTURE_LINE_1, FIXTURE_STATION_2, FIXTURE_STATION_1, new Distance(7))
                ));
    }

    @DisplayName("기존 노선도에서 중간 부분에 노선을 추가할 수 있다.(왼쪽은 노선에 없는 역이고, 오른쪽은 노선에 있고, 왼쪽에만 역이 있는 경우)")
    @Test
    void addSection_middle_4() {
        Subway subway = Subway.of(FIXTURE_LINE_1, List.of(
                new Section(FIXTURE_LINE_1, FIXTURE_STATION_2, FIXTURE_STATION_3, new Distance(10))
        ));

        Sections sections = subway.findUpdateSectionsByAddingSection(new Section(FIXTURE_LINE_1, FIXTURE_STATION_1, FIXTURE_STATION_3, new Distance(3)));
        assertThat(sections.getSections())
                .usingRecursiveComparison()
                .isEqualTo(List.of(
                        new Section(FIXTURE_LINE_1, FIXTURE_STATION_1, FIXTURE_STATION_3, new Distance(3)),
                        new Section(FIXTURE_LINE_1, FIXTURE_STATION_2, FIXTURE_STATION_1, new Distance(7))
                ));
    }

    @DisplayName("기존 노선에 역이 없을 때, 두 역을 모두 추가한다.")
    @Test
    void addSection_empty() {
        Subway subway = Subway.of(FIXTURE_LINE_1, new ArrayList<>());

        Sections sections = subway.findUpdateSectionsByAddingSection(new Section(FIXTURE_LINE_1, FIXTURE_STATION_1, FIXTURE_STATION_3, new Distance(3)));
        assertThat(sections.getSections())
                .usingRecursiveComparison()
                .isEqualTo(List.of(
                        new Section(FIXTURE_LINE_1, FIXTURE_STATION_1, FIXTURE_STATION_3, new Distance(3))
                ));
    }

    @DisplayName("기존 노선에 역이 있을 때, 두 역이 모두 없는 경우 예외를 던진다.")
    @Test
    void addSection_fail_1() {
        Subway subway = Subway.of(FIXTURE_LINE_1, List.of(
                new Section(FIXTURE_LINE_1, FIXTURE_STATION_2, FIXTURE_STATION_3, new Distance(10))
        ));

        assertThatThrownBy(() ->
                subway.findUpdateSectionsByAddingSection(new Section(FIXTURE_LINE_1, FIXTURE_STATION_5, FIXTURE_STATION_6, new Distance(3))))
                .isInstanceOf(SubwayServiceException.class)
                .hasMessageContaining("존재하지 않는 역들과의 구간을 등록할 수 없습니다.");
    }

    @DisplayName("기존 노선에 있는 두 역을 모두 추가하는 경우 예외를 던진다.- 순서대로 전달되는 경우")
    @Test
    void addSection_fail_2() {
        Subway subway = Subway.of(FIXTURE_LINE_1, List.of(
                new Section(FIXTURE_LINE_1, FIXTURE_STATION_1, FIXTURE_STATION_2, new Distance(10)),
                new Section(FIXTURE_LINE_1, FIXTURE_STATION_2, FIXTURE_STATION_3, new Distance(10))
        ));

        assertThatThrownBy(() ->
                subway.findUpdateSectionsByAddingSection(new Section(FIXTURE_LINE_1, FIXTURE_STATION_2, FIXTURE_STATION_3, new Distance(3))))
                .isInstanceOf(SubwayServiceException.class)
                .hasMessageContaining("노선에 이미 존재하는 두 역을 등록할 수 없습니다.");
    }

    @DisplayName("기존 노선에 있는 두 역을 모두 추가하는 경우 예외를 던진다.- 존재하기만하는 두 역을 전달되는 경우")
    @Test
    void addSection_fail_3() {
        Subway subway = Subway.of(FIXTURE_LINE_1, List.of(
                new Section(FIXTURE_LINE_1, FIXTURE_STATION_1, FIXTURE_STATION_2, new Distance(10)),
                new Section(FIXTURE_LINE_1, FIXTURE_STATION_2, FIXTURE_STATION_3, new Distance(10))
        ));

        assertThatThrownBy(() ->
                subway.findUpdateSectionsByAddingSection(new Section(FIXTURE_LINE_1, FIXTURE_STATION_3, FIXTURE_STATION_1, new Distance(3))))
                .isInstanceOf(SubwayServiceException.class)
                .hasMessageContaining("노선에 이미 존재하는 두 역을 등록할 수 없습니다.");
    }

    @DisplayName("기존에 있는 역 간의 거리 이상인 역을 추가하는 경우 예외를 발생한다.")
    @Test
    void addSection_fail_4() {
        Subway subway = Subway.of(FIXTURE_LINE_1, List.of(
                new Section(FIXTURE_LINE_1, FIXTURE_STATION_1, FIXTURE_STATION_2, new Distance(10)),
                new Section(FIXTURE_LINE_1, FIXTURE_STATION_2, FIXTURE_STATION_3, new Distance(10))
        ));

        assertThatThrownBy(() ->
                subway.findUpdateSectionsByAddingSection(new Section(FIXTURE_LINE_1, FIXTURE_STATION_2, FIXTURE_STATION_4, new Distance(10))))
                .isInstanceOf(SubwayServiceException.class)
                .hasMessageContaining("기존 역 사이 길이보다 크거나 같은 길이의 구간을 등록할 수 없습니다.");
    }

    @DisplayName("오른쪽에만 연결되어있는 역은 삭제 후 구간을 반환하지 않는다.")
    @Test
    void findDeleteSections_right() {
        Subway subway = Subway.of(FIXTURE_LINE_1, List.of(
                new Section(FIXTURE_LINE_1, FIXTURE_STATION_1, FIXTURE_STATION_2, new Distance(10)),
                new Section(FIXTURE_LINE_1, FIXTURE_STATION_2, FIXTURE_STATION_3, new Distance(10))
        ));

        assertThat(subway.findUpdateSectionsByDeletingSection(FIXTURE_STATION_1).getSections()).isEmpty();
    }

    @DisplayName("왼쪽에만 연결되어있는 역은 삭제 후 구간을 반환하지 않는다.")
    @Test
    void findDeleteSections_left() {
        Subway subway = Subway.of(FIXTURE_LINE_1, List.of(
                new Section(FIXTURE_LINE_1, FIXTURE_STATION_1, FIXTURE_STATION_2, new Distance(10)),
                new Section(FIXTURE_LINE_1, FIXTURE_STATION_2, FIXTURE_STATION_3, new Distance(10))
        ));

        assertThat(subway.findUpdateSectionsByDeletingSection(FIXTURE_STATION_3).getSections()).isEmpty();
    }

    @DisplayName("왼쪽에만 연결되어있는 역은 삭제 후 양쪽의 연결된 구간을 합쳐서 반환한다.")
    @Test
    void findDeleteSections_both() {
        Subway subway = Subway.of(FIXTURE_LINE_1, List.of(
                new Section(FIXTURE_LINE_1, FIXTURE_STATION_1, FIXTURE_STATION_2, new Distance(10)),
                new Section(FIXTURE_LINE_1, FIXTURE_STATION_2, FIXTURE_STATION_3, new Distance(10))
        ));

        assertThat(subway.findUpdateSectionsByDeletingSection(FIXTURE_STATION_2).getSections())
                .containsExactly(new Section(FIXTURE_LINE_1, FIXTURE_STATION_1, FIXTURE_STATION_3, new Distance(20)));
    }

    @DisplayName("연결되지 않은 역을 삭제하면 예외를 반환한다.")
    @Test
    void findDeleteSections_fail() {
        Subway subway = Subway.of(FIXTURE_LINE_1, List.of(
                new Section(FIXTURE_LINE_1, FIXTURE_STATION_1, FIXTURE_STATION_2, new Distance(10)),
                new Section(FIXTURE_LINE_1, FIXTURE_STATION_2, FIXTURE_STATION_3, new Distance(10))
        ));

        assertThatThrownBy(() -> subway.findUpdateSectionsByDeletingSection(FIXTURE_STATION_4))
                .isInstanceOf(SubwayServiceException.class)
                .hasMessageContaining("역이 노선에 없습니다.");
    }

    @DisplayName("한 노선에 대해 역들 간의 순서에 맞게 역을 반환한다.")
    @Test
    void getOrderedStations() {
        Subway subway = Subway.of(FIXTURE_LINE_1, List.of(
                SECTION_END,
                SECTION_MIDDLE_2,
                SECTION_MIDDLE_1,
                SECTION_MIDDLE_3,
                SECTION_START
        ));

        assertThat(subway.getOrderedStations())
                .containsExactly(
                        FIXTURE_STATION_1, FIXTURE_STATION_2, FIXTURE_STATION_3,
                        FIXTURE_STATION_4, FIXTURE_STATION_5, FIXTURE_STATION_6
                );
    }
}
