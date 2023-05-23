package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.LineExpenseDao;
import subway.dao.entity.LineExpenseEntity;
import subway.domain.fare.expense.ExpenseComposite;
import subway.domain.fare.expense.LineExpense;
import subway.domain.fare.expense.LineExpensePolicy;

import java.util.Optional;

@Repository
public class FareRepository {

    private final LineExpenseDao lineExpenseDao;
    private final ExpenseComposite expenseComposite;

    public FareRepository(final LineExpenseDao lineExpenseDao, final ExpenseComposite expenseComposite) {
        this.lineExpenseDao = lineExpenseDao;
        this.expenseComposite = expenseComposite;
    }

    public Long saveLineExpense(final LineExpenseEntity lineExpenseEntity) {
        final Optional<LineExpenseEntity> maybeLineExpenseEntity = lineExpenseDao.findByLineId(lineExpenseEntity.getLineId());
        if (maybeLineExpenseEntity.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 노선 추가 비용 정책 정보입니다.");
        }

        return lineExpenseDao.save(lineExpenseEntity);
    }

    public Long update(final LineExpenseEntity updateLineExpenseEntity) {
        final Optional<LineExpenseEntity> maybeLineExpenseEntity = lineExpenseDao.findByLineId(updateLineExpenseEntity.getLineId());
        if (maybeLineExpenseEntity.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 노선 추가 비용 정책 정보입니다.");
        }

        lineExpenseDao.update(updateLineExpenseEntity);

        return maybeLineExpenseEntity.get().getId();
    }

    public LineExpense findLineExpenseByLineId(final Long lineId) {
        final LineExpenseEntity findLineExpenseEntity = getLineExpenseEntityOrThrowException(lineId);

        return findLineExpenseEntity.toDomain();
    }

    public ExpenseComposite collectExpenseComposite() {
        lineExpenseDao.findAll()
                .stream()
                .map(LineExpenseEntity::toDomain)
                .map(LineExpensePolicy::new)
                .forEach(expenseComposite::addExpensePolicy);

        return expenseComposite;
    }

    private LineExpenseEntity getLineExpenseEntityOrThrowException(final Long lineId) {
        return lineExpenseDao.findByLineId(lineId)
                .orElseThrow(() -> new IllegalArgumentException("노선 식별자값으로 해당 노선 추가 비용 정책 정보를 조회할 수 없습니다."));
    }
}
