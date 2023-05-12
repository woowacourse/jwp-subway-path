package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static subway.domain.fixture.StationFixture.역1;
import static subway.domain.fixture.StationFixture.역2;
import static subway.domain.fixture.StationFixture.역3;
import static subway.domain.fixture.StationFixture.역4;
import static subway.domain.fixture.StationFixture.역5;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.application.dto.LineQueryResponse;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Section;
import subway.domain.Sections;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("LineQueryService 은(는)")
class LineQueryServiceTest {

    private final LineRepository lineRepository = mock(LineRepository.class);
    private final LineQueryService lineQueryService = new LineQueryService(lineRepository);

    @Test
    void id_를_통해서_노선을_조회한다() {
        // given
        final Sections sections = new Sections(List.of(
                new Section(역1, 역2, 10),
                new Section(역2, 역3, 10),
                new Section(역3, 역4, 10)
        ));
        final Line line = new Line("1호선", sections);
        given(lineRepository.findById(1L))
                .willReturn(Optional.of(line));

        // when
        final LineQueryResponse response = lineQueryService.findById(1L);

        // then
        assertThat(response.getLineName()).isEqualTo("1호선");
        assertThat(response.getStationQueryResponseList())
                .extracting(it -> it.getUpStationName() + "-[" + it.getDistance() + "km]-" + it.getDownStationName())
                .containsExactly(
                        "역1-[10km]-역2",
                        "역2-[10km]-역3",
                        "역3-[10km]-역4"
                );
    }

    @Test
    void 모든_노선을_조회한다() {
        // given
        final Sections sections1 = new Sections(List.of(
                new Section(역1, 역2, 10),
                new Section(역2, 역3, 10)
        ));
        final Line line1 = new Line("1호선", sections1);

        final Sections sections2 = new Sections(List.of(
                new Section(역3, 역4, 10),
                new Section(역4, 역5, 10)
        ));
        final Line line2 = new Line("2호선", sections2);

        given(lineRepository.findAll())
                .willReturn(List.of(line1, line2));

        // when
        final List<LineQueryResponse> responses = lineQueryService.findAll();

        // then
        assertThat(responses.get(0).getLineName()).isEqualTo("1호선");
        assertThat(responses.get(1).getLineName()).isEqualTo("2호선");
        assertThat(responses.get(0).getStationQueryResponseList())
                .extracting(it -> it.getUpStationName() + "-[" + it.getDistance() + "km]-" + it.getDownStationName())
                .containsExactly(
                        "역1-[10km]-역2",
                        "역2-[10km]-역3"
                );
        assertThat(responses.get(1).getStationQueryResponseList())
                .extracting(it -> it.getUpStationName() + "-[" + it.getDistance() + "km]-" + it.getDownStationName())
                .containsExactly(
                        "역3-[10km]-역4",
                        "역4-[10km]-역5"
                );
    }
}
