package subway.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.ui.dto.GetLineResponse;
import subway.ui.dto.LineRequest;
import subway.ui.dto.PostLineResponse;
import subway.ui.dto.StationResponse;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LineServiceTest {

    @Mock
    StationDao stationDao;
    @Mock
    LineDao lineDao;
    @Mock
    SectionDao sectionDao;
    @InjectMocks
    LineService lineService;

    @Test
    void 존재하는_두_역을_통해_노선을_생성한다() {
        given(stationDao.findById(1L)).willReturn(Optional.of(new Station(1L, "천호")));
        given(stationDao.findById(2L)).willReturn(Optional.of(new Station(2L, "잠실")));
        given(lineDao.insert(new Line(null, "8호선", "pink"))).willReturn(new Line(1L, "8호선", "pink"));
        LineRequest lineRequest = new LineRequest("8호선", "pink", 1L, 2L, 10);

        PostLineResponse postLineResponse = lineService.saveLine(lineRequest);

        assertThat(postLineResponse.getLineId()).isEqualTo(1L);
        assertThat(postLineResponse.getLineName()).isEqualTo("8호선");
        assertThat(postLineResponse.getLineColor()).isEqualTo("pink");
    }

    @Test
    void 등록되지_않은_역으로_노선을_생성하면_예외가_발생한다() {
        given(stationDao.findById(1L)).willReturn(Optional.of(new Station(1L, "천호")));
        given(stationDao.findById(2L)).willReturn(Optional.empty());
        LineRequest lineRequest = new LineRequest("8호선", "pink", 1L, 2L, 10);

        assertThatThrownBy(() -> lineService.saveLine(lineRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않는 역이 있습니다.");
    }

    @Test
    void 이미_등록된_노선을_삭제할_수_있다() {
        assertDoesNotThrow(() -> lineService.deleteLineById(1L));
    }

    @Test
    void 조회_시_노선_정보와_포함된_역을_상행부터_순서대로_반환한다() {
        // given
        Station jangji = new Station(1L, "장지");
        Station cheonho = new Station(2L, "천호");
        Station jamsil = new Station(3L, "잠실");
        Line pink = new Line(1L, "8호선", "pink");
        Section jamsilJangji10 = new Section(1L, jamsil, jangji, pink, 10);
        Section cheonhoJamsil10 = new Section(2L, cheonho, jamsil, pink, 10);

        given(lineDao.findById(1L)).willReturn(pink);
        given(sectionDao.findAllByLineId(1L)).willReturn(List.of(jamsilJangji10, cheonhoJamsil10));

        // when
        GetLineResponse response = lineService.findLineById(1L);

        // then
        List<StationResponse> stations = response.getStations();
        assertAll(() -> {
            assertThat(response.getLineId()).isEqualTo(1L);
            assertThat(response.getLineName()).isEqualTo("8호선");
            assertThat(response.getLineColor()).isEqualTo("pink");
            assertThat(stations.get(0).getName()).isEqualTo("천호");
            assertThat(stations.get(1).getName()).isEqualTo("잠실");
            assertThat(stations.get(2).getName()).isEqualTo("장지");
        });
    }

    @Test
    void 전체_조회_시_등록된_호선_순서대로_노선_정보와_포함된_역을_순서대로_반환한다() {
        // given
        Station jangji = new Station(1L, "장지");
        Station cheonho = new Station(2L, "천호");
        Station jamsil = new Station(3L, "잠실");
        Station gangnam = new Station(4L, "강남");
        Station kundae = new Station(5L, "건대");
        Line pink = new Line(1L, "8호선", "pink");
        Line green = new Line(2L, "2호선", "green");
        Section jamsilJangji10 = new Section(1L, jamsil, jangji, pink, 10);
        Section cheonhoJamsil10 = new Section(2L, cheonho, jamsil, pink, 10);
        Section jamsilGangnam10 = new Section(3L, jamsil, gangnam, green, 10);
        Section kundaeJamsil10 = new Section(4L, kundae, jamsil, green, 10);

        given(lineDao.findAll()).willReturn(List.of(pink, green));
        given(sectionDao.findAllByLineId(1L)).willReturn(List.of(jamsilJangji10, cheonhoJamsil10));
        given(sectionDao.findAllByLineId(2L)).willReturn(List.of(kundaeJamsil10, jamsilGangnam10));

        // when
        List<GetLineResponse> responses = lineService.findAllLines();

        // then
        GetLineResponse line8 = responses.get(0);
        List<StationResponse> line8Stations = line8.getStations();
        GetLineResponse line2 = responses.get(1);
        List<StationResponse> line2Stations = line2.getStations();
        assertAll(() -> {
            assertThat(line8.getLineId()).isEqualTo(1L);
            assertThat(line8.getLineName()).isEqualTo("8호선");
            assertThat(line8.getLineColor()).isEqualTo("pink");
            assertThat(line8Stations.get(0).getName()).isEqualTo("천호");
            assertThat(line8Stations.get(1).getName()).isEqualTo("잠실");
            assertThat(line8Stations.get(2).getName()).isEqualTo("장지");

            assertThat(line2.getLineId()).isEqualTo(2L);
            assertThat(line2.getLineName()).isEqualTo("2호선");
            assertThat(line2.getLineColor()).isEqualTo("green");
            assertThat(line2Stations.get(0).getName()).isEqualTo("건대");
            assertThat(line2Stations.get(1).getName()).isEqualTo("잠실");
            assertThat(line2Stations.get(2).getName()).isEqualTo("강남");
        });
    }
}
