package subway.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static subway.TestSource.*;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.fare.FareCalculator;
import subway.domain.fare.FarePolicyRelatedParameters;
import subway.domain.path.PathException;
import subway.domain.path.PathFinder;
import subway.domain.path.PathInfo;
import subway.ui.dto.FareAndPathResponse;
import subway.ui.dto.FareAndPathRequest;
import subway.ui.dto.StationResponse;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FareAndPathServiceTest {

    @Mock
    StationDao stationDao;
    @Mock
    SectionDao sectionDao;
    @Mock
    FareCalculator fareCalculator;
    @Mock
    PathFinder pathFinder;
    @InjectMocks
    FareAndPathService fareAndPathService;

    @Test
    void 입력된_역_중_등록되지_않은_역이_존재하면_예외가_발생한다() {
        // given
        given(stationDao.findById(jamsil.getId())).willReturn(Optional.of(jamsil));
        given(stationDao.findById(2L)).willReturn(Optional.empty());
        FareAndPathRequest request = new FareAndPathRequest(jamsil.getId(), 2L, 20);

        // when & then
        Assertions.assertThatThrownBy(() -> fareAndPathService.findFareAndPath(request))
            .isInstanceOf(PathException.class)
            .hasMessage("등록되지 않은 역과의 경로는 찾을 수 없습니다.");
    }

    @Test
    void 최단_거리가_검색된다() {
        // given
        // 강남 - 10 - 잠실 - 10 - 건대
        given(stationDao.findById(kundae.getId())).willReturn(Optional.of(kundae));
        given(stationDao.findById(gangnam.getId())).willReturn(Optional.of(gangnam));
        given(sectionDao.findAll()).willReturn(List.of(kundaeJamsil10, jamsilGangnam10));
        given(pathFinder.findPath(any(Sections.class), any(Station.class), any(Station.class)))
            .willReturn(
                new PathInfo(List.of(kundae, jamsil, gangnam), new Sections(List.of(kundaeJamsil10, jamsilGangnam10))));
        given(fareCalculator.calculate(any(FarePolicyRelatedParameters.class))).willReturn(1450);

        // when
        FareAndPathRequest request = new FareAndPathRequest(kundae.getId(), gangnam.getId(), 20);

        // then
        FareAndPathResponse response = fareAndPathService.findFareAndPath(request);
        assertThat(response.getFare()).isEqualTo(1450);
        assertThat(response.getStations()).containsExactly(StationResponse.of(kundae), StationResponse.of(jamsil),
            StationResponse.of(gangnam));
    }
}
