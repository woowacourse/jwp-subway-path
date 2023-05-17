package subway.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

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
import subway.domain.Line;
import subway.domain.PathException;
import subway.domain.Section;
import subway.domain.Station;
import subway.ui.dto.PathRequest;
import subway.ui.dto.PathResponse;
import subway.ui.dto.StationResponse;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PathServiceTest {

    @Mock
    StationDao stationDao;
    @Mock
    SectionDao sectionDao;
    @InjectMocks
    PathService pathService;

    @Test
    void 입력된_역_중_등록되지_않은_역이_존재하면_예외가_발생한다() {
        // given
        given(stationDao.findById(1L)).willReturn(Optional.of(new Station(1L, "잠실")));
        given(stationDao.findById(2L)).willReturn(Optional.empty());
        PathRequest request = new PathRequest(1L, 2L);

        // when & then
        Assertions.assertThatThrownBy(() -> pathService.findPath(request))
            .isInstanceOf(PathException.class)
            .hasMessage("등록되지 않은 역과의 경로는 찾을 수 없습니다.");
    }

    @Test
    void 하나의_노선을_사용하는_최단_거리가_검색된다() {
        // given
        // 강남 - 10 - 잠실 - 10 - 건대
        Station kundae = new Station(1L, "건대");
        Station jamsil = new Station(2L, "잠실");
        Station gangnam = new Station(3L, "강남");

        Line green = new Line(1L, "2호선", "green");

        Section kundaeJamsil10 = new Section(kundae, jamsil, green, 10);
        Section jamsilGangnam10 = new Section(jamsil, gangnam, green, 10);

        given(stationDao.findById(1L)).willReturn(Optional.of(kundae));
        given(stationDao.findById(3L)).willReturn(Optional.of(gangnam));
        given(sectionDao.findAll()).willReturn(List.of(kundaeJamsil10, jamsilGangnam10));

        // when
        PathRequest request = new PathRequest(kundae.getId(), gangnam.getId());

        // then
        PathResponse response = pathService.findPath(request);
        assertThat(response.getFare()).isEqualTo(1450);
        assertThat(response.getStations()).containsExactly(StationResponse.of(kundae), StationResponse.of(jamsil),
            StationResponse.of(gangnam));
    }

    @Test
    void 환승을_통한_최단_거리가_검색된다() {

    }
}
