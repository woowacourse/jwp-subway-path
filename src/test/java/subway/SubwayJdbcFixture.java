package subway;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;
import subway.domain.Line;

import javax.sql.DataSource;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
@Sql("classpath:/remove-section-line.sql")
public abstract class SubwayJdbcFixture {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected DataSource dataSource;

    protected StationDao stationDao;
    protected SectionDao sectionDao;
    protected LineDao lineDao;
    protected Long 이호선;
    protected Long 팔호선;
    protected Long 잠실역;
    protected Long 삼성역;
    protected Long 잠실새내역;
    protected Long 선릉역;
    protected Long 몽촌토성역;
    protected Long 석촌역;

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(jdbcTemplate, dataSource);
        stationDao = new StationDao(jdbcTemplate, dataSource);
        lineDao = new LineDao(jdbcTemplate, dataSource);
        save();
    }

    /**
     *       팔호선
     *      몽촌토성
     *         |
     *        10
     *         |
     * 이호선 : 잠실 -- 10 -- 잠실새내 -- 20 -- 삼성 -- 15 -- 선릉
     *         |
     *         15
     *         |
     *        석촌
     */
    private void save() {
        이호선 = lineDao.insert(new Line("2호선", "초록색"));
        팔호선 = lineDao.insert(new Line("8호선", "분홍색"));

        잠실역 = stationDao.insert(new StationEntity("잠실역")).getId();
        잠실새내역 = stationDao.insert(new StationEntity("잠실새내역")).getId();
        삼성역 = stationDao.insert(new StationEntity("삼성역")).getId();
        선릉역 = stationDao.insert(new StationEntity("선릉역")).getId();
        몽촌토성역 = stationDao.insert(new StationEntity("몽촌토성역")).getId();
        석촌역 = stationDao.insert(new StationEntity("석촌역")).getId();

        sectionDao.insert(new SectionEntity(10, 잠실역, 잠실새내역, 이호선));
        sectionDao.insert(new SectionEntity(20, 잠실새내역, 삼성역, 이호선));
        sectionDao.insert(new SectionEntity(15, 삼성역, 선릉역, 이호선));

        sectionDao.insert(new SectionEntity(15, 석촌역, 잠실역, 팔호선));
        sectionDao.insert(new SectionEntity(10, 잠실역, 몽촌토성역, 팔호선));
    }

    protected SectionEntity findById(Long sectionId) {
        final String sql = "SELECT * FROM section where id = ?";

        final RowMapper<SectionEntity> rowMapper = (rs, num) -> new SectionEntity(
                rs.getLong("id"),
                rs.getInt("distance"),
                rs.getLong("up_station_id"),
                rs.getLong("down_station_id"),
                rs.getLong("line_id")
        );

        return jdbcTemplate.queryForObject(sql, rowMapper, sectionId);
    }
}
