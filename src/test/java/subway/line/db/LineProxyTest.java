package subway.line.db;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.interstation.domain.InterStation;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("LineProxy 는")
class LineProxyTest {

    private LineProxy lineProxy;

    @BeforeEach
    void setUp() {
        lineProxy = new LineProxy(1L, "2호선", "green", List.of(
                new InterStation(1L, 1L, 2L, 3L),
                new InterStation(2L, 2L, 3L, 3L)
        ));
    }

    @Test
    void 이름이_변경되지_않으면_정보_수정_여부가_바뀌지_않는다() {
        //given
        final String newName = "2호선";

        //when
        lineProxy.updateName(newName);

        //then
        assertThat(lineProxy.isInfoNeedToUpdated()).isFalse();
    }

    @Test
    void 이름이_변경되면_정보_수정_여부가_바뀐다() {
        //given
        final String newName = "신분당선";

        //when
        lineProxy.updateName(newName);

        //then
        assertThat(lineProxy.isInfoNeedToUpdated()).isTrue();
    }

    @Test
    void 색상이_변경되지_않으면_정보_수정_여부가_바뀌지_않는다() {
        //given
        final String newColor = "green";

        //when
        lineProxy.updateColor(newColor);

        //then
        assertThat(lineProxy.isInfoNeedToUpdated()).isFalse();
    }

    @Test
    void 색상이_변경되면_정보_수정_여부가_바뀐다() {
        //given
        final String newColor = "blue";

        //when
        lineProxy.updateColor(newColor);

        //then
        assertThat(lineProxy.isInfoNeedToUpdated()).isTrue();
    }

    @Test
    void 구간이_변경되지_않으면_정보_수정_여부가_바뀌지_않는다() {
        //given
        //then
        assertSoftly(softly -> {
            softly.assertThat(lineProxy.isInfoNeedToUpdated()).isFalse();
            softly.assertThat(lineProxy.getAddedInterStations()).hasSize(0);
            softly.assertThat(lineProxy.getRemovedInterStations()).hasSize(0);
        });
    }

    @Nested
    @DisplayName("구간이 추가되면")
    class Context_add {

        @Test
        void 끝에_추가되면_정보_수정_여부가_바뀐다() {
            //given
            //when
            lineProxy.addInterStation(3L, null, 4L, 3L);

            //then
            assertSoftly(softly -> {
                softly.assertThat(lineProxy.isInfoNeedToUpdated()).isFalse();
                softly.assertThat(lineProxy.getAddedInterStations()).hasSize(1);
                softly.assertThat(lineProxy.getRemovedInterStations()).hasSize(0);
            });
        }

        @Test
        void 중간에_추가되면_정보_수정_여부가_바뀐다() {
            //given
            //when
            lineProxy.addInterStation(2L, 3L, 4L, 1L);

            //then
            assertSoftly(softly -> {
                softly.assertThat(lineProxy.isInfoNeedToUpdated()).isFalse();
                softly.assertThat(lineProxy.getAddedInterStations()).hasSize(2);
                softly.assertThat(lineProxy.getRemovedInterStations()).hasSize(1);
            });
        }

        @Test
        void 처음에_추가되면_정보_수정_여부가_바뀐다() {
            //given
            //when
            lineProxy.addInterStation(null, 1L, 4L, 1L);

            //then
            assertSoftly(softly -> {
                softly.assertThat(lineProxy.isInfoNeedToUpdated()).isFalse();
                softly.assertThat(lineProxy.getAddedInterStations()).hasSize(1);
                softly.assertThat(lineProxy.getRemovedInterStations()).hasSize(0);
            });
        }
    }

    @Nested
    @DisplayName("구간이 삭제되면")
    class Context_removed {

        @Test
        void 끝이_삭제되면_정보_수정_여부가_바뀐다() {
            //given
            //when
            lineProxy.deleteStation(3L);

            //then
            assertSoftly(softly -> {
                softly.assertThat(lineProxy.isInfoNeedToUpdated()).isFalse();
                softly.assertThat(lineProxy.getAddedInterStations()).hasSize(0);
                softly.assertThat(lineProxy.getRemovedInterStations()).hasSize(1);
            });
        }

        @Test
        void 중간이_삭제되면_정보_수정_여부가_바뀐다() {
            //given
            //when
            lineProxy.deleteStation(2L);

            //then
            assertSoftly(softly -> {
                softly.assertThat(lineProxy.isInfoNeedToUpdated()).isFalse();
                softly.assertThat(lineProxy.getAddedInterStations()).hasSize(1);
                softly.assertThat(lineProxy.getRemovedInterStations()).hasSize(2);
            });
        }

        @Test
        void 처음이_삭제되면_정보_수정_여부가_바뀐다() {
            //given
            //when
            lineProxy.deleteStation(1L);

            //then
            assertSoftly(softly -> {
                softly.assertThat(lineProxy.isInfoNeedToUpdated()).isFalse();
                softly.assertThat(lineProxy.getAddedInterStations()).hasSize(0);
                softly.assertThat(lineProxy.getRemovedInterStations()).hasSize(1);
            });
        }
    }
}
