package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.domain.*;

import java.util.ArrayList;
import java.util.List;

@Repository
public class JdbcLineRepository implements LineRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Section> SectionRowMapper = (rs, rowNum) -> {
        Station upStation = new Station(rs.getLong(3), rs.getString(7));
        Station downStation = new Station(rs.getLong(4), rs.getString(8));
        return new Section(rs.getLong(1), upStation, downStation, rs.getInt(5), rs.getInt(6));
    };

    public JdbcLineRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Line> findLinesWithSort() {
        String sql = "SELECT section.*, s1.name as up_station_name, s2.name as down_station_name, line.name as line_name, line.color as line_color FROM section " +
                "LEFT OUTER JOIN station s1 ON section.up_station_id=s1.id " +
                "LEFT OUTER JOIN station s2 ON section.down_station_id=s2.id " +
                "LEFT OUTER JOIN line ON section.line_id = line.id " +
                "ORDER BY `order`";

        return jdbcTemplate.query(sql, rs -> {
            List<Line> lines = new ArrayList<>();
            List<Section> sections = new ArrayList<>();

            Long lineId = null;
            String lineName = "";
            String lineColor = "";
            while(rs.next()) {
                if (lineId != null && rs.getLong("line_id") != lineId) {
                    lines.add(new Line(lineId, lineName, lineColor, new Sections(sections)));
                    sections = new ArrayList<>();
                }

                lineId = rs.getLong("line_id");
                lineName = rs.getString("line_name");
                lineColor = rs.getString("line_color");

                Station upStation = new Station(rs.getLong("up_station_id"), rs.getString("up_station_name"));
                Station downStation = new Station(rs.getLong("down_station_id"), rs.getString("down_station_name"));
                sections.add(new Section(rs.getLong("id"), upStation, downStation, rs.getInt("distance"), rs.getInt("order")));
            }
            lines.add(new Line(lineId, lineName, lineColor, new Sections(sections)));
            return lines;
        });
    }

    @Override
    public List<Section> findSectionsWithSort() {
        String sql = "SELECT section.*, s1.name as up_station_name, s2.name as down_station_name FROM section" +
                " LEFT OUTER JOIN station s1 ON section.up_station_id=s1.id" +
                " LEFT OUTER JOIN station s2 ON section.down_station_id=s2.id" +
                " ORDER BY `order`";
        return jdbcTemplate.query(sql, SectionRowMapper);
    }

    @Override
    public List<Section> findSectionsByLineIdWithSort(final Long lineId) {
        String sql = "SELECT section.*, s1.name as up_station_name, s2.name as down_station_name FROM section" +
                " LEFT OUTER JOIN station s1 ON section.up_station_id=s1.id" +
                " LEFT OUTER JOIN station s2 ON section.down_station_id=s2.id" +
                " WHERE line_id = ? ORDER BY `order`";
        return jdbcTemplate.query(sql, SectionRowMapper, lineId);
    }
}
