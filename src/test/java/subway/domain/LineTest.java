package subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineTest {

    private Line line;

    @BeforeEach
    void setting() {
        line = new Line("name", "red");
    }

    @Test
    @DisplayName("노선에 역이 존재할 때, 입력된 두 역의 정보가 존재하지 않는다면 예외를 던진다")
    void addStation_exception_nonExists() {
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
    @DisplayName("")
    void Test() {
        //given
        //when
        //then
    }
}
