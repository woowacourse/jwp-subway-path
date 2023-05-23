package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.PassengerType;
import subway.domain.fare.FareInfo;
import subway.domain.fare.discount.DiscountComposite;
import subway.domain.fare.expense.ExpenseComposite;
import subway.domain.route.Route;
import subway.domain.vo.Money;
import subway.repository.FareRepository;

@Transactional(readOnly = true)
@Service
public class CalculateFareService {

    private final FareRepository fareRepository;
    private final DiscountComposite discountComposite;

    public CalculateFareService(final FareRepository fareRepository, final DiscountComposite discountComposite) {
        this.fareRepository = fareRepository;
        this.discountComposite = discountComposite;
    }

    public Money calculate(final PassengerType passengerType, final Route route) {
        final ExpenseComposite expenseComposite = fareRepository.collectExpenseComposite();

        FareInfo fareInfo = FareInfo.from(passengerType, route);
        fareInfo = expenseComposite.doImpose(fareInfo);
        fareInfo = discountComposite.doDiscount(fareInfo);

        return fareInfo.getTotalFare();
    }
}
