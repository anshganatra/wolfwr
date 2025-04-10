package com.csc540.wolfwr.service;

import com.csc540.wolfwr.dao.RewardDAO;
import com.csc540.wolfwr.dto.RewardDTO;
import com.csc540.wolfwr.model.Reward;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RewardService {

    private final RewardDAO rewardDAO;

    public RewardService(RewardDAO rewardDAO) {
        this.rewardDAO = rewardDAO;
    }

    public RewardDTO createReward(RewardDTO dto) {
        Reward reward = new Reward();
        BeanUtils.copyProperties(dto, reward);
        rewardDAO.save(reward);
        return dto;
    }

    public RewardDTO getReward(Integer memberId, Integer year) {
        Reward reward = rewardDAO.getByMemberAndYear(memberId, year);
        if (reward == null) {
            return null; // or handle it in another appropriate way, e.g., creating a new reward record
        }
        RewardDTO dto = new RewardDTO();
        BeanUtils.copyProperties(reward, dto);
        return dto;
    }

    public List<RewardDTO> getAllRewards() {
        return rewardDAO.getAll().stream().map(reward -> {
            RewardDTO dto = new RewardDTO();
            BeanUtils.copyProperties(reward, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    // Returns the Rewards for all Platinum members at the end of the year.
    public List<RewardDTO> getRewardsByYear(Integer year) {
        List<Reward> rewards = rewardDAO.getRewardsByYear(year);
        return rewards.stream().map(reward -> {
            RewardDTO dto = new RewardDTO();
            BeanUtils.copyProperties(reward, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    public RewardDTO updateReward(RewardDTO dto) {
        Reward reward = new Reward();
        BeanUtils.copyProperties(dto, reward);
        rewardDAO.update(reward);
        return dto;
    }

    public void deleteReward(Integer memberId, Integer year) {
        rewardDAO.delete(memberId, year);
    }
}
