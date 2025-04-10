package com.csc540.wolfwr.dao;

import com.csc540.wolfwr.model.Reward;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RewardDAO {

    private final JdbcTemplate jdbcTemplate;

    public RewardDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Reward> rewardRowMapper = new RowMapper<>() {
        @Override
        public Reward mapRow(ResultSet rs, int rowNum) throws SQLException {
            Reward reward = new Reward();
            reward.setMemberId(rs.getInt("member_ID"));
            reward.setYear(rs.getInt("year"));
            reward.setRewardTotal(rs.getBigDecimal("reward_total"));
            return reward;
        }
    };

    public int save(Reward reward) {
        String sql = "INSERT INTO Rewards (member_ID, year, reward_total) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, reward.getMemberId(), reward.getYear(), reward.getRewardTotal());
    }

    public Reward getByMemberAndYear(Integer memberId, Integer year) {
        String sql = "SELECT * FROM Rewards WHERE member_ID = ? AND year = ?";
        try {
            return jdbcTemplate.queryForObject(sql, rewardRowMapper, memberId, year);
        } catch (EmptyResultDataAccessException ex) {
            throw new IllegalArgumentException("Invalid member ID or year");
        }
    }

    public List<Reward> getRewardsByYear(Integer year) {
        String sql = "SELECT * FROM Rewards WHERE year = ?";
        return jdbcTemplate.query(sql, rewardRowMapper, year);
    }

    public List<Reward> getAll() {
        String sql = "SELECT * FROM Rewards";
        return jdbcTemplate.query(sql, rewardRowMapper);
    }

    public int update(Reward reward) {
        String sql = "UPDATE Rewards SET reward_total = ? WHERE member_ID = ? AND year = ?";
        return jdbcTemplate.update(sql, reward.getRewardTotal(), reward.getMemberId(), reward.getYear());
    }

    public int delete(Integer memberId, Integer year) {
        String sql = "DELETE FROM Rewards WHERE member_ID = ? AND year = ?";
        return jdbcTemplate.update(sql, memberId, year);
    }
}
