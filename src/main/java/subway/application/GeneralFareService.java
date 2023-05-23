package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.request.UpdateLineExpenseRequest;
import subway.application.response.LineExpenseResponse;
import subway.dao.entity.LineExpenseEntity;
import subway.domain.fare.expense.LineExpense;
import subway.repository.FareRepository;

@Transactional(readOnly = true)
@Service
public class GeneralFareService {

    private final FareRepository fareRepository;

    public GeneralFareService(final FareRepository fareRepository) {
        this.fareRepository = fareRepository;
    }

    @Transactional
    public Long updateLineExpense(final Long lineId, final UpdateLineExpenseRequest request) {
        final LineExpenseEntity lineExpenseEntity
                = new LineExpenseEntity(request.getPerExpense(), request.getPerDistance(), lineId);

        return fareRepository.update(lineExpenseEntity);
    }

    public LineExpenseResponse findLineExpenseByLineId(final Long lineId) {
        final LineExpense findLineExpense = fareRepository.findLineExpenseByLineId(lineId);

        return LineExpenseResponse.from(findLineExpense);
    }
}
