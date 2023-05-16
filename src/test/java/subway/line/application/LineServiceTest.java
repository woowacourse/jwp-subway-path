package subway.line.application;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static subway.line.domain.fixture.SectionFixtures.포함된_구간들을_검증한다;
import static subway.line.domain.fixture.StationFixture.선릉;
import static subway.line.domain.fixture.StationFixture.역1;
import static subway.line.domain.fixture.StationFixture.역2;
import static subway.line.domain.fixture.StationFixture.역3;
import static subway.line.domain.fixture.StationFixture.역4;
import static subway.line.domain.fixture.StationFixture.잠실;
import static subway.line.exception.line.LineExceptionType.INCONSISTENT_EXISTING_SECTION;
import static subway.line.exception.line.LineExceptionType.SURCHARGE_IS_NEGATIVE;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
import subway.common.exception.BaseExceptionType;
import subway.line.application.dto.AddStationToLineCommand;
import subway.line.application.dto.DeleteStationFromLineCommand;
import subway.line.application.dto.LineCreateCommand;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.line.domain.LineValidator;
import subway.line.domain.Section;
import subway.line.domain.Sections;
import subway.line.domain.Station;
import subway.line.domain.StationRepository;
import subway.line.domain.event.ChangeLineEvent;
import subway.line.domain.service.RemoveStationFromLineService;
import subway.line.exception.line.LineException;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("LineService 은(는)")
class LineServiceTest {

    private final LineRepository lineRepository = mock(LineRepository.class);
    private final StationRepository stationRepository = mock(StationRepository.class);
    private final ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);
    private final LineValidator lineValidator = new LineValidator(lineRepository);
    private final RemoveStationFromLineService removeStationFromLineService =
            new RemoveStationFromLineService(lineRepository);

    private final LineService lineService = new LineService(
            lineRepository, stationRepository, lineValidator, removeStationFromLineService, publisher
    );

    private void 역을_저장한다(final Station station) {
        given(stationRepository.findByName(station.name()))
                .willReturn(Optional.of(station));
    }

    @Nested
    class 노선_생성_시 {

        @BeforeEach
        void setUp() {
            given(lineRepository.findAll()).willReturn(emptyList());
            given(lineRepository.findByName("1호선")).willReturn(Optional.empty());
            역을_저장한다(잠실);
            역을_저장한다(선릉);
            willDoNothing().given(lineRepository).save(any());
        }

        @Test
        void 노선을_생성한다() {
            // given
            final LineCreateCommand command = new LineCreateCommand("1호선", 0);

            // when
            final UUID uuid = lineService.create(command);

            // then
            assertThat(uuid).isNotNull();
            then(publisher).should(times(1))
                    .publishEvent(any(ChangeLineEvent.class));
        }

        @Test
        void 추가요금이_음수라면_예외이다() {
            // given
            willDoNothing().given(lineRepository).save(any());

            final LineCreateCommand command = new LineCreateCommand("1호선", -1);

            // when
            final BaseExceptionType exceptionType = assertThrows(LineException.class, () ->
                    lineService.create(command)
            ).exceptionType();

            // then
            assertThat(exceptionType).isEqualTo(SURCHARGE_IS_NEGATIVE);
        }
    }

    @Nested
    class 노선에_역_추가_시 {

        @BeforeEach
        void setUp() {
            역을_저장한다(역1);
            역을_저장한다(역2);
            역을_저장한다(역3);
            역을_저장한다(역4);
        }

        @Test
        void 노선에_역을_추가한다() {
            // given
            final Section section = new Section(역1, 역2, 10);
            final Line line = new Line("1호선", 0, new Sections(section));
            given(lineRepository.findAll()).willReturn(List.of(line));
            given(lineRepository.findByName("1호선")).willReturn(Optional.of(line));
            final AddStationToLineCommand command = new AddStationToLineCommand(
                    "1호선",
                    "역1",
                    "역3",
                    6);

            // when
            lineService.addStation(command);

            // then
            verify(lineRepository, times(1)).update(line);
            assertThat(line.sections()).hasSize(2);
            then(publisher).should(times(1))
                    .publishEvent(any(ChangeLineEvent.class));
        }

        @Test
        void 추가할_구간이_이미_다른_노선에_존재하며_해당_노선과_거리나_역의_상하관계가_일치하지_않는_경우_예외() {
            // given
            final Line line1 = new Line("1호선", 0,
                    new Section(역1, 역2, 10),
                    new Section(역2, 역3, 10)
            );
            final Line line2 = new Line("2호선", 0,
                    new Section(역3, 역4, 10)
            );
            given(lineRepository.findAll()).willReturn(List.of(line1, line2));
            given(lineRepository.findByName("2호선")).willReturn(Optional.of(line2));
            final AddStationToLineCommand command = new AddStationToLineCommand(
                    "2호선",
                    "역2",
                    "역1",
                    10);

            // when & then
            final BaseExceptionType exceptionType = assertThrows(LineException.class, () ->
                    lineService.addStation(command)
            ).exceptionType();
            assertThat(exceptionType).isEqualTo(INCONSISTENT_EXISTING_SECTION);
        }
    }

    @Nested
    class 노선_에서_역_제거_시 {

        @Test
        void 노선에서_역을_제거한다() {
            // given
            final Sections sections = new Sections(List.of(
                    new Section(역1, 역2, 10),
                    new Section(역2, 역3, 10)));
            final DeleteStationFromLineCommand command = new DeleteStationFromLineCommand("1호선", "역2");
            final Line line = new Line("1호선", 0, sections);
            given(lineRepository.findByName("1호선"))
                    .willReturn(Optional.of(line));
            역을_저장한다(역2);

            // when
            lineService.removeStation(command);

            // then
            verify(lineRepository, times(1)).update(line);
            assertThat(line.sections()).hasSize(1);
            then(publisher).should(times(1))
                    .publishEvent(any(ChangeLineEvent.class));
            포함된_구간들을_검증한다(line.sections(), "역1-[20km]-역3");
        }

        @Test
        void 노션에_역이_두개일떄_노선에서_역_제거시_노선도_제거된다() {
            // given
            final Sections sections = new Sections(new Section(역1, 역2, 10));
            final DeleteStationFromLineCommand command = new DeleteStationFromLineCommand("1호선", "역2");
            final Line line = new Line("1호선", 0, sections);
            given(lineRepository.findByName("1호선"))
                    .willReturn(Optional.of(line));
            역을_저장한다(역2);

            // when
            lineService.removeStation(command);

            // then
            verify(lineRepository, times(0)).update(line);
            verify(lineRepository, times(1)).delete(line);
            assertThat(line.sections()).hasSize(0);
        }
    }
}
