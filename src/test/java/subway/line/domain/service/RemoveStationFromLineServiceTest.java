package subway.line.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static subway.line.domain.fixture.SectionFixtures.포함된_구간들을_검증한다;
import static subway.line.domain.fixture.StationFixture.역1;
import static subway.line.domain.fixture.StationFixture.역2;
import static subway.line.domain.fixture.StationFixture.역3;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.line.domain.Section;
import subway.line.domain.Sections;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("RemoveStationFromLineService 은(는)")
class RemoveStationFromLineServiceTest {

    private final LineRepository lineRepository = mock(LineRepository.class);
    private final RemoveStationFromLineService removeStation = new RemoveStationFromLineService(lineRepository);

    @Test
    void 노선에서_역을_제거한다() {
        // given
        final Sections sections = new Sections(List.of(
                new Section(역1, 역2, 10),
                new Section(역2, 역3, 10))
        );
        final Line line = new Line("1호선", sections);

        // when
        removeStation.remove(line, 역2);

        // then
        verify(lineRepository, times(1)).update(line);
        verify(lineRepository, times(0)).delete(line);
        assertThat(line.sections()).hasSize(1);
        포함된_구간들을_검증한다(line.sections(), "역1-[20km]-역3");
    }

    @Test
    void 노션에_역이_두개일떄_노선에서_역_제거시_노선도_제거된다() {
        // given
        final Sections sections = new Sections(new Section(역1, 역2, 10));
        final Line line = new Line("1호선", sections);

        // when
        removeStation.remove(line, 역2);

        // then
        verify(lineRepository, times(0)).update(line);
        verify(lineRepository, times(1)).delete(line);
        assertThat(line.sections()).hasSize(0);
    }
}
