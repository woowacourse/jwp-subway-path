package subway.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import subway.application.LineService;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @Mock
    private LineDao lineDao;

    @Mock
    private SectionDao sectionDao;

    @Mock
    private StationDao stationDao;

    @InjectMocks
    private LineService lineService;

    @DisplayName("새 라인의 이름과 색깔을 전달하여 저장을 요청하고 성공하면 저장된 라인의 이름과 색깔은 요청과 동일하다")
    @Test
    void saveLine() {
        LineRequest request = new LineRequest("1호선", "blue");
        when(lineDao.insert(any())).thenReturn(new LineEntity(1L, request.getName(), request.getColor()));

        LineResponse response = lineService.saveLine(request);

        assertSoftly(softly -> {
            softly.assertThat(response.getName()).isEqualTo(request.getName());
            softly.assertThat(response.getColor()).isEqualTo(request.getColor());
            softly.assertThat(response.getStations()).isEmpty();
        });
    }

    @DisplayName("라인의 id를 전달받아 DB에 저장된 라인을 반환한다")
    @Test
    void findLine() {
        when(lineDao.findById(1L)).thenReturn(new LineEntity(1L, "1호선", "blue"));
        when(sectionDao.findAllByLineId(1L)).thenReturn(List.of(
                new SectionEntity(1L, 1L, 2L, 6),
                new SectionEntity(1L, 2L, 3L, 4)
        ));
        when(stationDao.findById(1L)).thenReturn(new Station(1L, "잠실나루"));
        when(stationDao.findById(2L)).thenReturn(new Station(2L, "잠실"));
        when(stationDao.findById(3L)).thenReturn(new Station(3L, "잠실새내"));

        LineResponse response = lineService.findLineById(1L);
        List<Long> stations = response.getStations()
                .stream()
                .map(station -> station.getId())
                .collect(Collectors.toList());

        assertSoftly(softly -> {
            softly.assertThat(response.getId()).isEqualTo(1);
            softly.assertThat(response.getName()).isEqualTo("1호선");
            softly.assertThat(response.getColor()).isEqualTo("blue");
            softly.assertThat(stations).hasSize(3);
            softly.assertThat(stations).containsExactly(1L, 2L, 3L);
        });
    }
}
