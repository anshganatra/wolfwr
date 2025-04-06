package com.csc540.wolfwr.controller;

import com.csc540.wolfwr.dto.RewardDTO;
import com.csc540.wolfwr.service.RewardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Reward API", description = "CRUD operations for rewards")
@RestController
@RequestMapping("/rewards")
public class RewardController {

    private final RewardService rewardService;

    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @PostMapping
    public ResponseEntity<RewardDTO> createReward(@Valid @RequestBody RewardDTO rewardDTO) {
        return ResponseEntity.ok(rewardService.createReward(rewardDTO));
    }

    @GetMapping("/{memberId}/{year}")
    public ResponseEntity<RewardDTO> getReward(@PathVariable Integer memberId, @PathVariable Integer year) {
        return ResponseEntity.ok(rewardService.getReward(memberId, year));
    }

    @GetMapping
    public ResponseEntity<List<RewardDTO>> getAllRewards() {
        return ResponseEntity.ok(rewardService.getAllRewards());
    }

    @PutMapping("/{memberId}/{year}")
    public ResponseEntity<RewardDTO> updateReward(@PathVariable Integer memberId,
                                                  @PathVariable Integer year,
                                                  @Valid @RequestBody RewardDTO rewardDTO) {
        rewardDTO.setMemberId(memberId);
        rewardDTO.setYear(year);
        return ResponseEntity.ok(rewardService.updateReward(rewardDTO));
    }

    @DeleteMapping("/{memberId}/{year}")
    public ResponseEntity<Void> deleteReward(@PathVariable Integer memberId, @PathVariable Integer year) {
        rewardService.deleteReward(memberId, year);
        return ResponseEntity.noContent().build();
    }
}
