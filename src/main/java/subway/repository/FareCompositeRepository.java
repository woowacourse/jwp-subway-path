package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.LineExpenseDao;
import subway.dao.entity.LineExpenseEntity;
import subway.domain.fare.discount.DiscountComposite;
import subway.domain.fare.expense.ExpenseComposite;
import subway.domain.fare.expense.LineExpensePolicy;

@Repository
public class FareCompositeRepository {

    private final LineExpenseDao lineExpenseDao;
    private final ExpenseComposite expenseComposite;
    private final DiscountComposite discountComposite;

    public FareCompositeRepository(
            final LineExpenseDao lineExpenseDao,
            final ExpenseComposite expenseComposite,
            final DiscountComposite discountComposite
    ) {
        this.lineExpenseDao = lineExpenseDao;
        this.expenseComposite = expenseComposite;
        this.discountComposite = discountComposite;
    }

    public DiscountComposite collectDiscountComposite() {
        return discountComposite;
    }

    public ExpenseComposite collectExpenseComposite() {
        lineExpenseDao.findAll()
                .stream()
                .map(LineExpenseEntity::toDomain)
                .map(LineExpensePolicy::new)
                .forEach(expenseComposite::addExpensePolicy);

        return expenseComposite;
    }
}
