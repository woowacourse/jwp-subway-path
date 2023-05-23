package subway.line.domain.fare.application;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import subway.line.domain.fare.Fare;
import subway.line.domain.fare.infrastructure.SurchargeDao;

@Repository
public class SurchargeRepository {
    private final SurchargeDao surchargeDao;

    public SurchargeRepository(SurchargeDao surchargeDao) {
        this.surchargeDao = surchargeDao;
    }

    public Fare saveSurcharge(long lineId, Fare surcharge) {
        return surchargeDao.save(lineId, surcharge.getMoney());
    }

    public Fare findByLineId(long lineId) {
        return surchargeDao.findByLineId(lineId);
    }

    public boolean hasLineSurcharge(long lineId) {
        try {
            surchargeDao.findByLineId(lineId);
            return true;
        } catch (DataAccessException exception) {
            return false;
        }
    }
}
