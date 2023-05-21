package subway.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.core.Line;
import subway.domain.core.Section;
import subway.dto.StationDeleteRequest;
import subway.dto.StationInitialSaveRequest;
import subway.dto.StationSaveRequest;
import subway.repository.LineRepository;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Transactional
@SpringBootTest
public class StationServiceTest {

    @Autowired
    private StationService stationService;

    @Autowired
    private LineRepository lineRepository;

    @Test
    void 역을_등록한다() {
        // given
        lineRepository.save(new Line("1호선", "RED", 0, List.of(
                new Section("A", "B", 2)
        )));
        lineRepository.save(new Line("2호선", "BLUE", 0, List.of(
                new Section("Z", "B", 2),
                new Section("B", "Y", 3)
        )));
        final StationSaveRequest request = new StationSaveRequest("1호선", "B", "C", "RIGHT", 3);

        // when
        stationService.save(request);

        // then
        assertThat(lineRepository.findAll()).flatExtracting(Line::getSections)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(List.of(
                        new Section("A", "B", 2),
                        new Section("B", "C", 3),
                        new Section("Z", "B", 2),
                        new Section("B", "Y", 3)
                ));
    }

    @Test
    void 역을_제거한다() {
        // given
        lineRepository.save(new Line("1호선", "RED", 0, List.of(
                new Section("A", "B", 2),
                new Section("B", "C", 3)
        )));
        lineRepository.save(new Line("2호선", "BLUE", 0, List.of(
                new Section("Z", "B", 2),
                new Section("B", "Y", 3)
        )));
        final StationDeleteRequest request = new StationDeleteRequest("1호선", "B");

        // when
        stationService.delete(request);

        // then
        assertThat(lineRepository.findAll()).flatExtracting(Line::getSections)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(List.of(
                        new Section("A", "C", 5),
                        new Section("Z", "B", 2),
                        new Section("B", "Y", 3)
                ));
    }

    @Test
    void 라인이_비어있을_때_초기_역을_등록한다() {
        // given
        lineRepository.save(new Line("1호선", "RED", 0, Collections.emptyList()));
        final StationInitialSaveRequest request = new StationInitialSaveRequest("1호선", "A", "B", 3);

        // when
        stationService.initialSave(request);

        // then
        assertThat(lineRepository.findAll()).flatExtracting(Line::getSections)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(List.of(
                        new Section("A", "B", 3)
                ));
    }
}
