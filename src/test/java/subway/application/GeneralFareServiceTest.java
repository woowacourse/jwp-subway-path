package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import subway.application.request.UpdateLineExpenseRequest;
import subway.application.response.LineExpenseResponse;
import subway.config.ServiceTestConfig;
import subway.dao.entity.LineExpenseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class GeneralFareServiceTest extends ServiceTestConfig {

    GeneralFareService generalFareService;

    @BeforeEach
    void setUp() {
        generalFareService = new GeneralFareService(fareRepository);
    }

    @Test
    void 기존_정책이_있을때_노선_추가_비용_정책을_수정할_수_있다() {
        // given
        final Long 파랑_노선_식별자값 = 1L;
        fareRepository.saveLineExpense(new LineExpenseEntity("1000", 10, 파랑_노선_식별자값));

        final UpdateLineExpenseRequest 파랑_노선_추가_비용_정책_수정_데이터 = new UpdateLineExpenseRequest("2000", 5);

        // when
        final Long 파랑_노선_추가_비용_정책_식별자값 = generalFareService.updateLineExpense(파랑_노선_식별자값, 파랑_노선_추가_비용_정책_수정_데이터);

        final LineExpenseResponse 파랑_노선_추가_비용_정책_응답_데이터 = generalFareService.findLineExpenseByLineId(파랑_노선_식별자값);

        // then
        assertThat(파랑_노선_추가_비용_정책_응답_데이터)
                .usingRecursiveComparison()
                .isEqualTo(new LineExpenseResponse(파랑_노선_추가_비용_정책_식별자값, "2000", 5, 파랑_노선_식별자값));
    }

    @Test
    void 노선_식별자값으로_노선_추가_비용_정책을_조회한다() {
        // given
        final Long 파랑_노선_식별자값 = 1L;
        final Long 파랑_노선_추가_비용_정책_식별자값 = fareRepository.saveLineExpense(new LineExpenseEntity("1000", 10, 파랑_노선_식별자값));

        // when
        final LineExpenseResponse 파랑_노선_추가_비용_정책_응답_데이터 = generalFareService.findLineExpenseByLineId(파랑_노선_식별자값);

        // then
        assertThat(파랑_노선_추가_비용_정책_응답_데이터)
                .usingRecursiveComparison()
                .isEqualTo(new LineExpenseResponse(파랑_노선_추가_비용_정책_식별자값, "1000", 10, 파랑_노선_식별자값));
    }
}
