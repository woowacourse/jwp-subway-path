package subway.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import subway.config.RepositoryTestConfig;
import subway.dao.entity.LineExpenseEntity;
import subway.domain.fare.expense.LineExpense;
import subway.domain.vo.Distance;
import subway.domain.vo.Money;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FareRepositoryTest extends RepositoryTestConfig {

    FareRepository fareRepository;

    @BeforeEach
    void setUp() {
        fareRepository = new FareRepository(lineExpenseDao);
    }

    @Test
    void 노선_추가_비용_정책을_저장한다() {
        // given
        final Long 파랑_노선_식별자값 = 1L;
        final LineExpenseEntity 파랑_노선_추가_비용_정책_엔티티 = new LineExpenseEntity("1000", 5, 파랑_노선_식별자값);

        // when
        fareRepository.saveLineExpense(파랑_노선_추가_비용_정책_엔티티);

        final LineExpense 노선_추가_비용_정책 = fareRepository.findLineExpenseByLineId(파랑_노선_식별자값);

        // then
        assertThat(노선_추가_비용_정책)
                .isEqualTo(new LineExpense(Money.from("1000"), Distance.from(5), 파랑_노선_식별자값));

    }

    @Test
    void 이미_저장된_노선_추가_비용_정책일_경우_예외가_발생한다() {
        // given
        final Long 파랑_노선 = 1L;
        final LineExpenseEntity 파랑_노선_추가_비용_정책_엔티티 = new LineExpenseEntity("1000", 5, 파랑_노선);
        fareRepository.saveLineExpense(파랑_노선_추가_비용_정책_엔티티);

        // expect
        assertThatThrownBy(() -> fareRepository.saveLineExpense(파랑_노선_추가_비용_정책_엔티티))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 노선_추가_비용_정책을_수정한다() {
        // given
        final Long 파랑_노선_식별자값 = 1L;
        final LineExpenseEntity 파랑_노선_추가_비용_정책_엔티티 = new LineExpenseEntity("1000", 5, 파랑_노선_식별자값);

        fareRepository.saveLineExpense(파랑_노선_추가_비용_정책_엔티티);

        // when
        final LineExpenseEntity 파랑_노선_추가_비용_정책_수정_엔티티 = new LineExpenseEntity("2000", 10, 파랑_노선_식별자값);
        fareRepository.update(파랑_노선_추가_비용_정책_수정_엔티티);

        final LineExpense 노선_추가_비용_정책 = fareRepository.findLineExpenseByLineId(파랑_노선_식별자값);

        // then
        assertThat(노선_추가_비용_정책)
                .isEqualTo(new LineExpense(Money.from("2000"), Distance.from(10), 파랑_노선_식별자값));

    }

    @Test
    void 존재하지않는_노선_추가_비용_정책을_수정할_경우_예외가_발생한다() {
        // given
        final Long 파랑_노선 = 1L;
        final LineExpenseEntity 파랑_노선_추가_비용_정책_엔티티 = new LineExpenseEntity("1000", 5, 파랑_노선);

        // expect
        assertThatThrownBy(() -> fareRepository.update(파랑_노선_추가_비용_정책_엔티티))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
