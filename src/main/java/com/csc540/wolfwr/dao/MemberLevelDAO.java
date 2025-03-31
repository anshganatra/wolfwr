package com.csc540.wolfwr.dao;

import com.csc540.wolfwr.model.MemberLevel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MemberLevelDAO {
    private final JdbcTemplate jdbcTemplate;

    public MemberLevelDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<MemberLevel> memberLevelRowMapper = new RowMapper<MemberLevel>() {
        @Override
        public MemberLevel mapRow(ResultSet rs, int rowNum) throws SQLException {
            MemberLevel memberLevel = new MemberLevel();
            memberLevel.setLevelName(rs.getString("level_name"));
            memberLevel.setCashbackRate(rs.getFloat("cashback_rate"));
            return memberLevel;
        }
    };

    // Create - Insert a new MemberLevel into the database
    public int save(MemberLevel memberLevel) {
        String sql = "INSERT INTO MemberLevels (level_name, cashback_rate) VALUES (?, ?)";
        return jdbcTemplate.update(sql, memberLevel.getLevelName(), memberLevel.getCashbackRate());
    }

    public MemberLevel getMemberLevelByName(String level_name) {
        String sql = "SELECT * FROM MemberLevels WHERE level_name = ?";
        return jdbcTemplate.queryForObject(sql, memberLevelRowMapper, level_name);
    }

    public List<MemberLevel> getMemberLevels() {
        String sql = "SELECT * FROM MemberLevels";
        return jdbcTemplate.query(sql, memberLevelRowMapper);
    }

    public int update(MemberLevel memberLevel) {
        String sql = "UPDATE MemberLevels SET level_name = ?, cashback_rate = ? WHERE level_name = ?";
        return jdbcTemplate.update(sql, memberLevel.getLevelName(), memberLevel.getCashbackRate(), memberLevel.getLevelName());
    }

    public int delete(String level_name) {
        String sql = "DELETE FROM MemberLevels WHERE level_name = ?";
        return jdbcTemplate.update(sql, level_name);
    }
}
