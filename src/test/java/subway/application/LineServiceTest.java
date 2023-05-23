package subway.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static subway.TestSource.*;

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
        given(stationDao.findById(cheonho.getId())).willReturn(Optional.of(cheonho));
        given(stationDao.findById(jamsil.getId())).willReturn(Optional.of(jamsil));
        given(lineDao.insert(new Line(null, "8호선", "pink", 0))).willReturn(pink);
        LineRequest lineRequest = new LineRequest("8호선", "pink", cheonho.getId(), jamsil.getId(), 10, 0);

        PostLineResponse postLineResponse = lineService.saveLine(lineRequest);

        assertThat(postLineResponse.getLineId()).isEqualTo(pink.getId());
        assertThat(postLineResponse.getLineName()).isEqualTo("8호선");
        assertThat(postLineResponse.getLineColor()).isEqualTo("pink");
        assertThat(postLineResponse.getAdditionalCharge()).isEqualTo(0);
    }

    @Test
    void 등록되지_않은_역으로_노선을_생성하면_예외가_발생한다() {
        given(stationDao.findById(cheonho.getId())).willReturn(Optional.of(cheonho));
        given(stationDao.findById(2L)).willReturn(Optional.empty());
        LineRequest lineRequest = new LineRequest("8호선", "pink", cheonho.getId(), 2L, 10, 0);

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
        given(lineDao.findById(pink.getId())).willReturn(Optional.of(pink));
        given(sectionDao.findAllByLineId(pink.getId())).willReturn(List.of(jamsilJangji10, cheonhoJamsil10));

        // when
        GetLineResponse response = lineService.findLineById(pink.getId());

        // then
        List<StationResponse> stations = response.getStations();
        assertAll(() -> {
            assertThat(response.getLineId()).isEqualTo(pink.getId());
            assertThat(response.getLineName()).isEqualTo("8호선");
            assertThat(response.getLineColor()).isEqualTo("pink");
            assertThat(response.getAdditionalCharge()).isEqualTo(0);
            assertThat(stations.get(0).getName()).isEqualTo("천호");
            assertThat(stations.get(1).getName()).isEqualTo("잠실");
            assertThat(stations.get(2).getName()).isEqualTo("장지");
        });
    }

    @Test
    void 전체_조회_시_등록된_호선_순서대로_노선_정보와_포함된_역을_순서대로_반환한다() {
        // given
        given(lineDao.findAll()).willReturn(List.of(pink, green));
        given(sectionDao.findAllByLineId(pink.getId())).willReturn(List.of(jamsilJangji10, cheonhoJamsil10));
        given(sectionDao.findAllByLineId(green.getId())).willReturn(List.of(kundaeJamsil10, jamsilGangnam10));

        // when
        List<GetLineResponse> responses = lineService.findAllLines();

        // then
        GetLineResponse line8 = responses.get(0);
        List<StationResponse> line8Stations = line8.getStations();
        GetLineResponse line2 = responses.get(1);
        List<StationResponse> line2Stations = line2.getStations();
        assertAll(() -> {
            assertThat(line8.getLineId()).isEqualTo(pink.getId());
            assertThat(line8.getLineName()).isEqualTo("8호선");
            assertThat(line8.getLineColor()).isEqualTo("pink");
            assertThat(line8.getAdditionalCharge()).isEqualTo(0);
            assertThat(line8Stations.get(0).getName()).isEqualTo("천호");
            assertThat(line8Stations.get(1).getName()).isEqualTo("잠실");
            assertThat(line8Stations.get(2).getName()).isEqualTo("장지");

            assertThat(line2.getLineId()).isEqualTo(green.getId());
            assertThat(line2.getLineName()).isEqualTo("2호선");
            assertThat(line2.getLineColor()).isEqualTo("green");
            assertThat(line2.getAdditionalCharge()).isEqualTo(0);
            assertThat(line2Stations.get(0).getName()).isEqualTo("건대입구");
            assertThat(line2Stations.get(1).getName()).isEqualTo("잠실");
            assertThat(line2Stations.get(2).getName()).isEqualTo("강남");
        });
    }
}
