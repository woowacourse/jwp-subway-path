package subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

class LineTest {

    private Line line;

    @BeforeEach
    void setting() {
        line = new Line(null, "name", "red", new ArrayList<>());
    }

    @Test
    @DisplayName("노선에 역이 존재할 때, 입력된 두 역의 정보가 존재하지 않는다면 예외를 던진다")
    void addSection_exception_nonExists() {
        //given
        line.addSection(new Section(
                new Station("푸우"),
                new Station("테오"),
                new Distance(3)
        ));

        Section section = new Section(
                new Station("시카"),
                new Station("제이온"),
                new Distance(1)
        );

        //when, then
        assertThatThrownBy(() -> line.addSection(section))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("이미 존재하는 노선이라면 예외를 던진다")
    void addSection_exception_alreadyExist() {
        //given
        line.addSection(new Section(
                new Station("푸우"),
                new Station("테오"),
                new Distance(3)
        ));

        line.addSection(new Section(
                new Station(1L, "테오"),
                new Station(2L, "제이온"),
                new Distance(1)
        ));

        Section section = new Section(
                new Station("푸우"),
                new Station("제이온"),
                new Distance(1)
        );

        //when, then
        assertThatThrownBy(() -> line.addSection(section))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("새로운 노선의 거리가 기존 노선의 거리를 넘는다면 예외를 던진다")
    void addSection_exception_overLength() {
        //given
        line.addSection(new Section(
                new Station("푸우"),
                new Station("테오"),
                new Distance(1)
        ));
        Section overLengthSection = new Section(
                new Station("푸우"),
                new Station("시카"),
                new Distance(100)
        );

        //when, then
        assertThatThrownBy(() -> line.addSection(overLengthSection))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("새로운 노선의 거리가 기존 노선의 거리를 넘지 않는다면 정상 작동한다")
    void addSection_insideSection() {
        //given
        line.addSection(new Section(
                new Station("푸우"),
                new Station("테오"),
                new Distance(100)
        ));
        Section newSection = new Section(
                new Station("푸우"),
                new Station("시카"),
                new Distance(1)
        );

        assertThatCode(() -> line.addSection(newSection))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("종착역에 대해서는 거리에 관계없이 항상 노선을 추가할 수 있다")
    void addSection_endPoint() {
        //given
        line.addSection(new Section(
                new Station("푸우"),
                new Station("테오"),
                new Distance(100)
        ));
        Section overLengthSection = new Section(
                new Station("테오"),
                new Station("시카"),
                new Distance(1000)
        );

        //when, then
        assertThatCode(() -> line.addSection(overLengthSection))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("아무 역이 존재하지 않을 때 삭제하려고 하면 예외를 던진다")
    void deleteSection_exception_whenEmpty() {
        //given
        Station station = new Station("테오");

        //when, then
        assertThatThrownBy(() -> line.deleteStation(station))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("해당 역이 존재하지 않을 때 삭제하려고 하면 예외를 던진다")
    void deleteSection_exception_whenNotExists() {
        //given
        Section section = new Section(
                new Station("테오"),
                new Station("시카"),
                new Distance(1000)
        );
        line.addSection(section);

        Station station = new Station("푸우");

        //when, then
        assertThatThrownBy(() -> line.deleteStation(station))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("노선 중간에 있는 역을 삭제할 수 있다")
    void deleteSection_middle() {
        //given
        line.addSection(new Section(
                new Station("테오"),
                new Station("시카"),
                new Distance(1000)
        ));
        line.addSection(new Section(
                new Station("시카"),
                new Station("제이온"),
                new Distance(1000)
        ));

        Station station = new Station("시카");

        //when, then
        assertThatCode(() -> line.deleteStation(station))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("종착역을 삭제할 수 있다")
    void deleteSection_endPoint() {
        //given
        line.addSection(new Section(
                new Station("테오"),
                new Station("시카"),
                new Distance(1000)
        ));
        line.addSection(new Section(
                new Station("시카"),
                new Station("제이온"),
                new Distance(1000)
        ));

        Station station = new Station("테오");

        //when, then
        assertThatCode(() -> line.deleteStation(station))
                .doesNotThrowAnyException();
    }
}
