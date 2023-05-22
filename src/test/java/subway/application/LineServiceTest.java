package subway.application;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.dto.LineAndStationsResponse;
import subway.dto.LineResponse;
import subway.dto.StationAddRequest;
import subway.dto.StationResponse;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static subway.common.fixture.DomainFixture.디노;
import static subway.common.fixture.DomainFixture.디노_조앤;
import static subway.common.fixture.DomainFixture.조앤;
import static subway.common.fixture.DomainFixture.후추;
import static subway.common.fixture.DomainFixture.후추_디노;
import static subway.common.fixture.WebFixture.일호선_남색_요청;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;
    @Mock
    private SectionRepository sectionRepository;

    @InjectMocks
    private LineService lineService;

    @Test
    void 호선을_저장한다() {
        //given
        when(lineRepository.save(any(Line.class)))
                .thenReturn(new Line(1L, "일호선", "남색", new ArrayList<>()));

        //when
        final LineResponse lineResponse = lineService.saveLine(일호선_남색_요청);

        //then
        assertSoftly(softly -> {
            softly.assertThat(lineResponse.getId()).isEqualTo(1L);
            softly.assertThat(lineResponse.getName()).isEqualTo("일호선");
            softly.assertThat(lineResponse.getColor()).isEqualTo("남색");
        });
    }

    @Test
    void 동일한_호선을_저장하면_예외를_던진다() {
        //given
        when(lineRepository.contains(any(Line.class)))
                .thenReturn(true);

        //expect
        assertThatThrownBy(() -> lineService.saveLine(일호선_남색_요청))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 호선입니다.");
    }

    @Test
    void id로_호선을_조회한다() {
        //given
        when(lineRepository.findLineById(1L))
                .thenReturn(new Line(1L, "일호선", "남색", List.of(new Section(후추, 디노, 7), new Section(디노, 조앤, 4))));

        //when
        final LineAndStationsResponse response = lineService.findLineById(1L);

        //then
        assertSoftly(softly -> {
            final LineResponse lineResponse = response.getLineResponse();
            softly.assertThat(lineResponse.getId()).isEqualTo(1L);
            softly.assertThat(lineResponse.getName()).isEqualTo("일호선");
            softly.assertThat(lineResponse.getColor()).isEqualTo("남색");

            final List<StationResponse> stationResponses = response.getStationResponses();
            softly.assertThat(stationResponses).hasSize(3);
            softly.assertThat(stationResponses.get(0).getName()).isEqualTo("후추");
            softly.assertThat(stationResponses.get(1).getName()).isEqualTo("디노");
            softly.assertThat(stationResponses.get(2).getName()).isEqualTo("조앤");
        });
    }

    @Test
    void 전체_호선을_조회한다() {
        //given
        when(lineRepository.findLines())
                .thenReturn(List.of(new Line(1L, "일호선", "남색", List.of(new Section(후추, 디노, 7), new Section(디노, 조앤, 4)))));

        //when
        final List<LineAndStationsResponse> responses = lineService.findLines();

        //then
        assertSoftly(softly -> {
            softly.assertThat(responses).hasSize(1);
            final LineAndStationsResponse lineAndStationsResponse = responses.get(0);

            final LineResponse lineResponse = lineAndStationsResponse.getLineResponse();
            softly.assertThat(lineResponse.getId()).isEqualTo(1L);
            softly.assertThat(lineResponse.getName()).isEqualTo("일호선");
            softly.assertThat(lineResponse.getColor()).isEqualTo("남색");

            final List<StationResponse> stationResponses = lineAndStationsResponse.getStationResponses();
            softly.assertThat(stationResponses).hasSize(3);
            softly.assertThat(stationResponses.get(0).getName()).isEqualTo("후추");
            softly.assertThat(stationResponses.get(1).getName()).isEqualTo("디노");
            softly.assertThat(stationResponses.get(2).getName()).isEqualTo("조앤");
        });
    }

    @Test
    void 호선에_역을_추가한다() {
        //given
        when(lineRepository.findLineById(1L))
                .thenReturn(new Line(1L, "일호선", "남색", List.of(후추_디노)));

        when(stationRepository.findStationById(후추.getId()))
                .thenReturn(후추);

        when(stationRepository.findStationById(조앤.getId()))
                .thenReturn(조앤);

        //when
        final LineAndStationsResponse response = lineService.addStationToLine(1L, new StationAddRequest(후추.getId(), 조앤.getId(), 3));

        //then
        assertSoftly(softly -> {
            final LineResponse lineResponse = response.getLineResponse();
            softly.assertThat(lineResponse.getId()).isEqualTo(1L);
            softly.assertThat(lineResponse.getName()).isEqualTo("일호선");
            softly.assertThat(lineResponse.getColor()).isEqualTo("남색");

            final List<StationResponse> stationResponses = response.getStationResponses();
            softly.assertThat(stationResponses).hasSize(3);
            softly.assertThat(stationResponses.get(0).getName()).isEqualTo("후추");
            softly.assertThat(stationResponses.get(1).getName()).isEqualTo("조앤");
            softly.assertThat(stationResponses.get(2).getName()).isEqualTo("디노");
        });
    }

    @Test
    void 호선에_역을_제거한다() {
        //given
        when(lineRepository.findLineById(1L))
                .thenReturn(new Line(1L, "일호선", "남색", List.of(후추_디노, 디노_조앤)));

        when(stationRepository.findStationById(디노.getId()))
                .thenReturn(디노);

        //when
        lineService.deleteStationFromLine(1L, 디노.getId());

        //then
        final LineAndStationsResponse response = lineService.findLineById(1L);
        assertSoftly(softly -> {
            final LineResponse lineResponse = response.getLineResponse();
            softly.assertThat(lineResponse.getId()).isEqualTo(1L);
            softly.assertThat(lineResponse.getName()).isEqualTo("일호선");
            softly.assertThat(lineResponse.getColor()).isEqualTo("남색");

            final List<StationResponse> stationResponses = response.getStationResponses();
            softly.assertThat(stationResponses).hasSize(2);
            softly.assertThat(stationResponses.get(0).getName()).isEqualTo("후추");
            softly.assertThat(stationResponses.get(1).getName()).isEqualTo("조앤");
        });
    }
}
