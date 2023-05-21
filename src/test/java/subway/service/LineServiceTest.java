package subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.mock;
import static subway.domain.fixture.SectionFixtures.createSection;
import static subway.domain.fixture.SectionFixtures.포함된_구간들을_검증한다;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.request.LineCreateRequest;
import subway.dto.request.StationAddToLineRequest;
import subway.dto.request.StationDeleteFromLineRequest;
import subway.exception.DuplicateLineException;
import subway.exception.NotFoundLineException;
import subway.exception.NotFoundStationException;
import subway.persistence.repository.LineRepository;
import subway.persistence.repository.StationRepository;


@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("LineService 은(는)")
class LineServiceTest {

    private final LineRepository lineRepository = mock(LineRepository.class);
    private final StationRepository stationRepository = mock(StationRepository.class);
    private final LineService lineService = new LineService(lineRepository, stationRepository);

    @Nested
    class 노선을_생성시 {
        final LineCreateRequest request = new LineCreateRequest(
                "1호선",
                "잠실역",
                "사당역",
                10,
                0);

        @Test
        void 성공한다() {
            // given
            given(lineRepository.findByName("1호선"))
                    .willReturn(Optional.empty());
            역을_저장한다("잠실역");
            역을_저장한다("사당역");
            given(lineRepository.save(any()))
                    .willReturn(1L);

            // when
            final Long id = lineService.create(request);

            // then
            assertThat(id).isEqualTo(1L);
        }

        @Test
        void 이미_존재하는_노선을_전달시_예외() {
            // given
            given(lineRepository.findByName("1호선"))
                    .willReturn(Optional.of(new Line("1호선", null)));

            // when & then
            assertThatThrownBy(() -> lineService.create(request))
                    .isInstanceOf(DuplicateLineException.class);
        }

        @Test
        void 존재하지_않는_역을_상행역으로_전달시_예외() {
            given(lineRepository.findByName("1호선"))
                    .willReturn(Optional.empty());
            역을_저장한다("사당역");

            // when & then
            assertThatThrownBy(() -> lineService.create(request))
                    .isInstanceOf(NotFoundStationException.class);
        }

        @Test
        void 존재하지_않는_역을_하행역으로_전달시_예외() {
            given(lineRepository.findByName("1호선"))
                    .willReturn(Optional.empty());
            역을_저장한다("잠실역");

            // when & then
            assertThatThrownBy(() -> lineService.create(request))
                    .isInstanceOf(NotFoundStationException.class);
        }
    }

    @Nested
    class 노선에_역을_추가시 {

        final StationAddToLineRequest request = new StationAddToLineRequest(
                "1호선",
                "잠실역",
                "사당역",
                6);

        @Test
        void 성공한다() {
            // given
            final Station up = new Station("잠실역");
            final Station down = new Station("역삼역");
            역을_저장한다("잠실역");
            역을_저장한다("사당역");

            final Section section = new Section(up, down, 10);
            final Line line = new Line("1호선", new Sections(section));
            given(lineRepository.findByName("1호선"))
                    .willReturn(Optional.of(line));

            // when
            lineService.addStation(request);

            // then
            verify(lineRepository, times(1)).update(line);
            assertThat(line.getSections()).hasSize(2);
        }

        @Test
        void 존재하지_않는_노선을_전달시_예외() {
            // given
            given(lineRepository.findByName("1호선"))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> lineService.addStation(request))
                    .isInstanceOf(NotFoundLineException.class);
        }

        @Test
        void 존재하지_않는_역을_상행역으로_전달시_예외() {
            given(lineRepository.findByName("1호선"))
                    .willReturn(Optional.of(new Line(null, null)));
            역을_저장한다("사당역");

            // when & then
            assertThatThrownBy(() -> lineService.addStation(request))
                    .isInstanceOf(NotFoundStationException.class);
        }

        @Test
        void 존재하지_않는_역을_하행역으로_전달시_예외() {
            given(lineRepository.findByName("1호선"))
                    .willReturn(Optional.of(new Line(null, null)));
            역을_저장한다("잠실역");

            // when & then
            assertThatThrownBy(() -> lineService.addStation(request))
                    .isInstanceOf(NotFoundStationException.class);
        }
    }

    @Test
    void 노선에서_역을_제거한다() {
        // given
        final Sections sections = new Sections(List.of(
                createSection("역1", "역2", 10),
                createSection("역2", "역3", 10)));
        final StationDeleteFromLineRequest request = new StationDeleteFromLineRequest("1호선", "역2");
        final Line line = new Line("1호선", sections);
        given(lineRepository.findByName("1호선"))
                .willReturn(Optional.of(line));
        역을_저장한다("역2");

        // when
        lineService.removeStation(request);

        // then
        verify(lineRepository, times(1)).update(line);
        assertThat(line.getSections()).hasSize(1);
        포함된_구간들을_검증한다(line.getSections(), "역1-[20km]-역3");
    }
    @Test
    void 노션에_역이_두개일떄_노선에서_역_제거시_노선도_제거된다() {
        // given
        final Sections sections = new Sections(createSection("역1", "역2", 10));
        final StationDeleteFromLineRequest request = new StationDeleteFromLineRequest("1호선", "역2");
        final Line line = new Line("1호선", sections);
        given(lineRepository.findByName("1호선"))
                .willReturn(Optional.of(line));
        역을_저장한다("역2");

        // when
        lineService.removeStation(request);

        // then
        verify(lineRepository, times(0)).update(line);
        verify(lineRepository, times(1)).delete(line);
        assertThat(line.getSections()).hasSize(0);
    }

    private void 역을_저장한다(final String name) {
        given(stationRepository.findByName(name))
                .willReturn(Optional.of(new Station(name)));
    }
}
