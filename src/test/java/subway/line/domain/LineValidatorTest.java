package subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static subway.line.domain.fixture.StationFixture.역1;
import static subway.line.domain.fixture.StationFixture.역2;
import static subway.line.domain.fixture.StationFixture.역3;
import static subway.line.domain.fixture.StationFixture.역4;
import static subway.line.domain.fixture.StationFixture.역5;
import static subway.line.exception.line.LineExceptionType.DUPLICATE_LINE_NAME;
import static subway.line.exception.line.LineExceptionType.INCONSISTENT_EXISTING_SECTION;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.common.exception.BaseExceptionType;
import subway.line.exception.line.LineException;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("LineValidator 은(는)")
class LineValidatorTest {

    private final LineRepository lineRepository = mock(LineRepository.class);
    private final LineValidator validator = new LineValidator(lineRepository);

    @Test
    void 이미_동일한_이름을_가진_노선이_있다면_예외() {
        // given
        given(lineRepository.findByName("역1"))
                .willReturn(Optional.of(new Line("1",
                        0, new Section(역1, 역2, 2))));

        // when
        final BaseExceptionType exceptionType = assertThrows(LineException.class, () ->
                validator.validateDuplicateLineName("역1")
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(DUPLICATE_LINE_NAME);
    }

    @Test
    void 기존_구간과_거리가_동일하지_않다면_예외이다() {
        // give
        final Line line1 = new Line("1호선",
                0, new Section(역1, 역2, 1),
                new Section(역2, 역3, 2)
        );
        final Line line2 = new Line("2호선",
                0, new Section(역3, 역4, 3),
                new Section(역4, 역5, 4)
        );
        given(lineRepository.findAll())
                .willReturn(List.of(line1, line2));
        final Section section = new Section(역3, 역4, 10);

        // when & then
        final BaseExceptionType exceptionType = assertThrows(LineException.class, () ->
                validator.validateSectionConsistency(section)
        ).exceptionType();
        assertThat(exceptionType).isEqualTo(INCONSISTENT_EXISTING_SECTION);
    }

    @Test
    void 기존_구간의_하행_역과_상행_역이_다르다면_예외이다() {
        // give
        final Line line1 = new Line("1호선",
                0, new Section(역1, 역2, 1),
                new Section(역2, 역3, 2)
        );
        final Line line2 = new Line("2호선",
                0, new Section(역3, 역4, 3),
                new Section(역4, 역5, 4)
        );
        given(lineRepository.findAll())
                .willReturn(List.of(line1, line2));
        final Section section = new Section(역2, 역1, 1);

        // when & then
        final BaseExceptionType exceptionType = assertThrows(LineException.class, () ->
                validator.validateSectionConsistency(section)
        ).exceptionType();
        assertThat(exceptionType).isEqualTo(INCONSISTENT_EXISTING_SECTION);
    }

    @Test
    void 기존_구간과_거리와_상행역_하행역이_동일한_경우에만_예외가_아니다() {
        // give
        willDoNothing().given(lineRepository).save(any());
        final Line line1 = new Line("1호선",
                0, new Section(역1, 역2, 1),
                new Section(역2, 역3, 2)
        );
        final Line line2 = new Line("2호선",
                0, new Section(역3, 역4, 3),
                new Section(역4, 역5, 4)
        );
        given(lineRepository.findAll())
                .willReturn(List.of(line1, line2));
        final Section section = new Section(역1, 역2, 1);

        // when & then
        assertDoesNotThrow(() ->
                validator.validateSectionConsistency(section)
        );
    }
}
