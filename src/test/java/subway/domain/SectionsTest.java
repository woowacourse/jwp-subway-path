package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SectionsTest {

    @Test
    void 초기_상태에_구간을_추가한다() {
        // given
        final Line line = new Line(1L, "2호선", "green");
        Long lineId = line.getId();
        final Sections sections = new Sections();

        final Station 디노 = new Station(1L, "디노");
        final Station 후추 = new Station(2L, "후추");

        // when
        sections.insertInitially(line, 디노, 후추, 7);

        // then
        assertThat(sections.getSections()).contains(
                new Section(lineId, new Station(1L, "디노"), new Station(2L, "후추"), 7)
        );
    }

    @Test
    void 역을_오른쪽_사이에_추가한다() {
        // given
        // 뚝섬 - 5 - 성수 - 7 - 건대입구
        // 뚝섬 - 5 - 성수 - 3- 디노 - 4 - 건대입구
        final Line line = new Line(1L, "2호선", "green");
        Long lineId = line.getId();
        final Station 뚝섬 = new Station(1L, "뚝섬");
        final Station 성수 = new Station(2L, "성수");
        final Station 건대입구 = new Station(3L, "건대입구");

        final Section 뚝섬_성수 = new Section(lineId, 뚝섬, 성수, 5);
        final Section 성수_건대입구 = new Section(lineId, 성수, 건대입구, 7);

        final Sections sections = new Sections(new ArrayList<>(List.of(뚝섬_성수, 성수_건대입구)));

        final Station 디노 = new Station(4L, "디노");

        sections.insert(line, 성수, 디노, 3);

        assertThat(sections.getSections()).contains(
                new Section(lineId, new Station(1L, "뚝섬"), new Station(2L, "성수"), 5),
                new Section(lineId, new Station(2L, "성수"), new Station(4L, "디노"), 3),
                new Section(lineId, new Station(4L, "디노"), new Station(3L, "건대입구"), 4)
        );
    }

    @Test
    void 역을_왼쪽_사이에_추가한다() {
        // given
        // 뚝섬 - 5 - 성수 - 7 - 건대입구
        // 뚝섬 - 3 - 후추 - 2 - 성수 - 7 - 건대입구
        final Line line = new Line(1L, "2호선", "green");
        Long lineId = line.getId();
        final Station 뚝섬 = new Station(1L, "뚝섬");
        final Station 성수 = new Station(2L, "성수");
        final Station 건대입구 = new Station(3L, "건대입구");

        final Section 뚝섬_성수 = new Section(lineId, 뚝섬, 성수, 5);
        final Section 성수_건대입구 = new Section(lineId, 성수, 건대입구, 7);

        final Sections sections = new Sections(new ArrayList<>(List.of(뚝섬_성수, 성수_건대입구)));

        final Station 후추 = new Station(4L, "후추");

        sections.insert(line, 후추, 성수, 2);

        assertThat(sections.getSections()).contains(
                new Section(lineId, new Station(1L, "뚝섬"), new Station(4L, "후추"), 3),
                new Section(lineId, new Station(4L, "후추"), new Station(2L, "성수"), 2),
                new Section(lineId, new Station(2L, "성수"), new Station(3L, "건대입구"), 7)
        );
    }

    @Test
    void 역을_오른쪽_끝에_추가한다() {
        // given
        // 뚝섬 - 5 - 성수
        // 뚝섬 - 5 - 성수 - 3- 디노
        final Line line = new Line(1L, "2호선", "green");
        Long lineId = line.getId();
        final Station 뚝섬 = new Station(1L, "뚝섬");
        final Station 성수 = new Station(2L, "성수");

        final Section 뚝섬_성수 = new Section(lineId, 뚝섬, 성수, 5);

        final Sections sections = new Sections(new ArrayList<>(List.of(뚝섬_성수)));

        final Station 디노 = new Station(4L, "디노");

        sections.insert(line, 성수, 디노, 3);

        assertThat(sections.getSections()).contains(
                new Section(lineId, new Station(1L, "뚝섬"), new Station(2L, "성수"), 5),
                new Section(lineId, new Station(2L, "성수"), new Station(4L, "디노"), 3)
        );
    }

    @Test
    void 역을_왼쪽_끝에_추가한다() {
        // given
        // 성수 - 7 - 건대입구
        // 후추 - 2 - 성수 - 7 - 건대입구
        final Line line = new Line(1L, "2호선", "green");
        Long lineId = line.getId();
        final Station 성수 = new Station(2L, "성수");
        final Station 건대입구 = new Station(3L, "건대입구");

        final Section 성수_건대입구 = new Section(lineId, 성수, 건대입구, 7);

        final Sections sections = new Sections(new ArrayList<>(List.of(성수_건대입구)));

        final Station 후추 = new Station(4L, "후추");

        sections.insert(line, 후추, 성수, 2);

        assertThat(sections.getSections()).contains(
                new Section(lineId, new Station(4L, "후추"), new Station(2L, "성수"), 2),
                new Section(lineId, new Station(2L, "성수"), new Station(3L, "건대입구"), 7)
        );
    }

    @Test
    void 역을_수정한다() {
        // given
        final Line line = new Line(1L, "2호선", "green");
        Long lineId = line.getId();
        final Station 뚝섬 = new Station(1L, "뚝섬");
        final Station 성수 = new Station(2L, "성수");
        final Station 건대입구 = new Station(3L, "건대입구");

        final Section 뚝섬_성수 = new Section(lineId, 뚝섬, 성수, 5);
        final Section 성수_건대입구 = new Section(lineId, 성수, 건대입구, 7);

        final Sections sections = new Sections(new ArrayList<>(List.of(뚝섬_성수, 성수_건대입구)));
        // when
        sections.updateStation(성수, new Station(4L, "후추"));
        // then
        assertThat(sections.getSections()).contains(
                new Section(lineId, new Station(1L, "뚝섬"), new Station(4L, "후추"), 5), new Section(
                        lineId, new Station(4L, "후추"), new Station(3L, "건대입구"), 7));
    }

}
