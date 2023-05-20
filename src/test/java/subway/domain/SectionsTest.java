package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {

    private static final Station 첫번쨰역_ = Station.from("첫번쨰역");
    private static final Station 두번쨰역_ = Station.from("두번쨰역");
    private static final Station 세번쨰역_ = Station.from("세번쨰역");
    private static final Station 네번쨰역_ = Station.from("네번쨰역");

    @DisplayName("구간들을 가진 Sections 를 생성한다.")
    @Test
    void create_success() {
        //given
        final List<Section> sectionList = List.of(
            Section.withNullId(첫번쨰역_, 두번쨰역_, 10),
            Section.withNullId(두번쨰역_, 세번쨰역_, 20),
            Section.withNullId(세번쨰역_, 네번쨰역_, 5)
        );

        //when
        //then
        assertDoesNotThrow(() -> Sections.of(sectionList));
    }

    @DisplayName("두 개의 하행역으로 향하는 상행역이 있는 경우 예외가 발생한다.")
    @Test
    void create_fail_duplicateUpStation() {
        //given
        final List<Section> sectionList = List.of(
            Section.withNullId(첫번쨰역_, 두번쨰역_, 10),
            Section.withNullId(첫번쨰역_, 세번쨰역_, 20),
            Section.withNullId(세번쨰역_, 네번쨰역_, 5)
        );

        //when
        //then
        assertThatThrownBy(() -> Sections.of(sectionList))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("두 개의 하행역으로 향하는 상행역이 존재합니다.");
    }

    @DisplayName("구간이 모두 연결되어 있지 않은 경우 예외가 발생한다.")
    @Test
    void create_fail_notConnected() {
        //given
        final List<Section> sectionList = List.of(
            Section.withNullId(첫번쨰역_, 두번쨰역_, 10),
            Section.withNullId(세번쨰역_, 네번쨰역_, 20)
        );

        //when
        //then
        assertThatThrownBy(() -> Sections.of(sectionList))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("모든 구간이 연결되어 있지 않습니다.( 하행 종점만이 하행역이 없는 상행역이 될 수 있습니다.)");
    }

    @DisplayName("구간 사이에 구간을 추가한다.")
    @Test
    void addSection_betweenSection() {
        //given
        final List<Section> sectionList = List.of(
            Section.withNullId(첫번쨰역_, 두번쨰역_, 10),
            Section.withNullId(두번쨰역_, 세번쨰역_, 20)
        );

        final Sections sections = Sections.of(sectionList);
        final Section newSection = Section.of(두번쨰역_, 네번쨰역_, SectionDirection.DOWN, 5);

        //when
        sections.addSection(두번쨰역_, newSection);

        //then
        final List<Section> sectionsAfterAdd = sections.getSections();
        assertAll(
            () -> assertThat(sectionsAfterAdd).hasSize(3),
            () -> assertThat(sectionsAfterAdd).contains(
                Section.withNullId(첫번쨰역_, 두번쨰역_, 10),
                Section.withNullId(두번쨰역_, 네번쨰역_, 5),
                Section.withNullId(네번쨰역_, 세번쨰역_, 15)
            )
        );
    }

    @DisplayName("제일 앞에 구간을 추가한다.")
    @Test
    void addSection_startOfSections() {
        //given
        final List<Section> sectionList = List.of(
            Section.withNullId(첫번쨰역_, 두번쨰역_, 10),
            Section.withNullId(두번쨰역_, 세번쨰역_, 20)
        );

        final Sections sections = Sections.of(sectionList);
        final Section newSection = Section.of(첫번쨰역_, 네번쨰역_, SectionDirection.UP, 5);

        //when
        sections.addSection(첫번쨰역_, newSection);

        //then
        final List<Section> sectionsAfterAdd = sections.getSections();
        assertAll(
            () -> assertThat(sectionsAfterAdd).hasSize(3),
            () -> assertThat(sectionsAfterAdd).contains(
                Section.withNullId(네번쨰역_, 첫번쨰역_, 5),
                Section.withNullId(첫번쨰역_, 두번쨰역_, 10),
                Section.withNullId(두번쨰역_, 세번쨰역_, 20)
            )
        );
    }

    @DisplayName("제일 뒤에 구간을 추가한다.")
    @Test
    void addSection_endOfSections() {
        //given
        final List<Section> sectionList = List.of(
            Section.withNullId(첫번쨰역_, 두번쨰역_, 10),
            Section.withNullId(두번쨰역_, 세번쨰역_, 20)
        );

        final Sections sections = Sections.of(sectionList);
        final Section newSection = Section.of(세번쨰역_, 네번쨰역_, SectionDirection.DOWN, 5);

        //when
        sections.addSection(세번쨰역_, newSection);

        //then
        final List<Section> sectionsAfterAdd = sections.getSections();
        assertAll(
            () -> assertThat(sectionsAfterAdd).hasSize(3),
            () -> assertThat(sectionsAfterAdd).contains(
                Section.withNullId(첫번쨰역_, 두번쨰역_, 10),
                Section.withNullId(두번쨰역_, 세번쨰역_, 20),
                Section.withNullId(세번쨰역_, 네번쨰역_, 5)
            )
        );
    }

    @DisplayName("구간 사이에 존재하는 역을 삭제한다.")
    @Test
    void removeStation_betweenSection() {
        //given
        final List<Section> sectionList = List.of(
            Section.withNullId(첫번쨰역_, 두번쨰역_, 10),
            Section.withNullId(두번쨰역_, 세번쨰역_, 20)
        );

        final Sections sections = Sections.of(sectionList);

        //when
        sections.removeStation(두번쨰역_);

        //then
        final List<Section> sectionsAfterAdd = sections.getSections();
        assertAll(
            () -> assertThat(sectionsAfterAdd).hasSize(1),
            () -> assertThat(sectionsAfterAdd).contains(Section.withNullId(첫번쨰역_, 세번쨰역_, 30))
        );
    }

    @DisplayName("구간 제일 앞에 존재하는 역을 삭제한다.")
    @Test
    void removeStation_startOfSections() {
        //given
        final List<Section> sectionList = List.of(
            Section.withNullId(첫번쨰역_, 두번쨰역_, 10),
            Section.withNullId(두번쨰역_, 세번쨰역_, 20)
        );

        final Sections sections = Sections.of(sectionList);

        //when
        sections.removeStation(첫번쨰역_);

        //then
        final List<Section> sectionsAfterAdd = sections.getSections();
        assertAll(
            () -> assertThat(sectionsAfterAdd).hasSize(1),
            () -> assertThat(sectionsAfterAdd).contains(Section.withNullId(두번쨰역_, 세번쨰역_, 20))
        );
    }

    @DisplayName("구간 제일 뒤에 존재하는 역을 삭제한다.")
    @Test
    void removeStation_endOfSections() {
        //given
        final List<Section> sectionList = List.of(
            Section.withNullId(첫번쨰역_, 두번쨰역_, 10),
            Section.withNullId(두번쨰역_, 세번쨰역_, 20)
        );

        final Sections sections = Sections.of(sectionList);

        //when
        sections.removeStation(세번쨰역_);

        //then
        final List<Section> sectionsAfterAdd = sections.getSections();
        assertAll(
            () -> assertThat(sectionsAfterAdd).hasSize(1),
            () -> assertThat(sectionsAfterAdd).contains(Section.withNullId(첫번쨰역_, 두번쨰역_, 10))
        );
    }
}
