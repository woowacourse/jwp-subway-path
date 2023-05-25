package subway.line.domain.fare.infrastructure;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import subway.line.domain.fare.Fare;

import java.math.BigDecimal;

@Repository
public class SurchargeDao {
    private final JdbcTemplate jdbcTemplate;

    public SurchargeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Fare save(long lineId, BigDecimal surcharge) {
        final var sql = "insert into SURCHARGE (line_id, surcharge) values (?, ?)";
        jdbcTemplate.update(sql, lineId, surcharge.toPlainString());
        return new Fare(surcharge);
    }

    public Fare findByLineId(long lineId) {
        final var sql = "select * from SURCHARGE where line_id = ?";
        return jdbcTemplate.queryForObject(sql,
                (rs, rowNum) -> new Fare(new BigDecimal(rs.getString("surcharge"))),
                lineId);
    }
}
