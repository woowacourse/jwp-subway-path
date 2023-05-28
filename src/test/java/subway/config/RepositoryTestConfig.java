package subway.config;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Import;
import subway.config.fare.FarePolicyConfig;
import subway.dao.LineDao;
import subway.dao.LineExpenseDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.fare.discount.DiscountComposite;
import subway.domain.fare.expense.ExpenseComposite;

@Import(FarePolicyConfig.class)
@ImportAutoConfiguration
public abstract class RepositoryTestConfig extends DaoTestConfig {

    protected StationDao stationDao;
    protected SectionDao sectionDao;
    protected LineDao lineDao;
    protected LineExpenseDao lineExpenseDao;

    @Autowired
    protected ExpenseComposite expenseComposite;

    @Autowired
    protected DiscountComposite discountComposite;

    @BeforeEach
    void daoSetUp() {
        stationDao = new StationDao(jdbcTemplate);
        sectionDao = new SectionDao(jdbcTemplate);
        lineDao = new LineDao(jdbcTemplate);
        lineExpenseDao = new LineExpenseDao(jdbcTemplate);
    }
}
