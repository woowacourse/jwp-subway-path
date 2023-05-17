package subway.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationResponse;
import subway.persistence.entity.LineEntity;
import subway.persistence.entity.SectionEntity;
import subway.persistence.entity.StationEntity;
import subway.persistence.repository.LineRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@Sql("/testdata.sql")
@SpringBootTest
class LineServiceTest {

    @Autowired
    private LineService lineService;

    @MockBean
    private LineRepository lineRepository;

    @Test
    void 호선_정보를_저장한다() {
        given(lineRepository.saveLine(any()))
                .willReturn(new LineEntity(1L, "1호선", "blue"));
        final LineResponse lineResponse = lineService.saveLine(new LineRequest("1호선", "blue"));
        assertThat(lineResponse).usingRecursiveComparison().isEqualTo(new LineResponse(1L, "1호선", "blue"));
    }

    @Test
    void 식별자를_통해_호선을_찾는다() {
        ///given
        given(lineRepository.findLineById(anyLong()))
                .willReturn(new LineEntity(1L, "1호선", "blue"));
        given(lineRepository.findSectionsByLine(any()))
                .willReturn(List.of(
                        new SectionEntity(1L, 1L, 2L, 1L, 10L)
                ));
        given(lineRepository.findStationById(1L))
                .willReturn(new StationEntity(1L, "종로5가"));
        given(lineRepository.findStationById(2L))
                .willReturn(new StationEntity(2L, "광화문"));

        ///when
        final LineResponse lineResponseById = lineService.findLineResponseById(1L);

        ///then
        assertThat(lineResponseById).usingRecursiveComparison().isEqualTo(new LineResponse(1L, "1호선", "blue", List.of(new StationResponse("종로5가"), new StationResponse("광화문"))));
    }

    @Test
    void 전체_노선을_조회한다() {
        ///given
        final List<LineEntity> lineEntities = List.of(new LineEntity(1L, "3호선", "orange"),new LineEntity(2L,"2호선","green"));
        given(lineRepository.findAllLines())
                .willReturn(lineEntities);
        given(lineRepository.findSectionsByLine(any()))
                .willReturn(List.of(
                        new SectionEntity(1L, 1L, 2L, 1L, 10L)
                ));
        given(lineRepository.findStationById(1L))
                .willReturn(new StationEntity(1L, "충무로"));
        given(lineRepository.findStationById(2L))
                .willReturn(new StationEntity(2L, "동대입구"));

        ///when
        final List<LineResponse> lineResponses = lineService.findLineResponses();

        ///then
        assertThat(lineResponses.size()).isSameAs(2);
    }

}