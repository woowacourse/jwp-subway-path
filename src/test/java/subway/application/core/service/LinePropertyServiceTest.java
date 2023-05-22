package subway.application.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.application.core.service.dto.in.IdCommand;
import subway.application.core.service.dto.in.SaveLinePropertyCommand;
import subway.application.core.service.dto.in.UpdateLinePropertyCommand;
import subway.application.core.service.dto.out.LinePropertyResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LinePropertyServiceTest {

    @Autowired
    private LinePropertyService linePropertyService;

    @Test
    @DisplayName("노선 정보를 저장할 수 있다")
    void saveLineProperty() {
        // given
        SaveLinePropertyCommand command = new SaveLinePropertyCommand("1호선", "파랑");
        LinePropertyResult saved = linePropertyService.saveLineProperty(command);

        // when
        IdCommand idCommand = new IdCommand(saved.getId());
        LinePropertyResult actual = linePropertyService.findLinePropertyById(idCommand);

        // then
        assertThat(saved).usingRecursiveComparison().isEqualTo(actual);
    }

    @Test
    @DisplayName("노선 정보를 모두 찾을 수 있다")
    void findLinePropertyResponses() {
        // given
        SaveLinePropertyCommand command = new SaveLinePropertyCommand("1호선", "파랑");
        LinePropertyResult saved = linePropertyService.saveLineProperty(command);

        // when
        List<LinePropertyResult> results = linePropertyService.findAllLineProperty();
        LinePropertyResult expected = new LinePropertyResult(saved.getId(), "1호선", "파랑");

        // then
        assertThat(results.get(0)).usingRecursiveComparison()
                .ignoringExpectedNullFields().isEqualTo(expected);
    }

    @Test
    @DisplayName("특정 노선 정보를 찾을 수 있다")
    void findLinePropertyResponseById() {
        // given
        SaveLinePropertyCommand command = new SaveLinePropertyCommand("1호선", "파랑");
        LinePropertyResult saved = linePropertyService.saveLineProperty(command);

        // when
        IdCommand idCommand = new IdCommand(saved.getId());
        LinePropertyResult result = linePropertyService.findLinePropertyById(idCommand);

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(saved);
    }

    @Test
    @DisplayName("특정 노선을 업데이트 할 수 있다")
    void updateLineProperty() {
        // given
        SaveLinePropertyCommand command = new SaveLinePropertyCommand("1호선", "파랑");
        LinePropertyResult saved = linePropertyService.saveLineProperty(command);

        // when
        UpdateLinePropertyCommand updateCommand =
                new UpdateLinePropertyCommand(saved.getId(), "1호선", "빨강");
        linePropertyService.updateLineProperty(updateCommand);
        LinePropertyResult findResult = linePropertyService.findLinePropertyById(
                new IdCommand(saved.getId()));
        LinePropertyResult expected = new LinePropertyResult(saved.getId(), "1호선", "빨강");

        // then
        assertThat(findResult).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("특정 노선을 삭제할 수 있다")
    void deleteLinePropertyById() {
        // given
        SaveLinePropertyCommand command = new SaveLinePropertyCommand("1호선", "파랑");
        LinePropertyResult saved = linePropertyService.saveLineProperty(command);

        // when
        IdCommand idCommand = new IdCommand(saved.getId());
        linePropertyService.deleteLinePropertyById(idCommand);

        // then
        assertThat(linePropertyService.findAllLineProperty()).isEmpty();
    }
}
