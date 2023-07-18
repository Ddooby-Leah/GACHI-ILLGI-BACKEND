package com.ddooby.gachiillgi.interfaces.controller;

import com.ddooby.gachiillgi.domain.service.FollowService;
import com.ddooby.gachiillgi.domain.service.FollowUserVOList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public void followUser(@RequestBody FollowRequestDTO followRequestDTO) {
        followService.followUser(followRequestDTO.getFollowerId(), followRequestDTO.getFolloweeId());
    }

    @DeleteMapping("")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public void unfollowUser(@RequestBody FollowRequestDTO followRequestDTO) {
        followService.unfollowUser(followRequestDTO.getFollowerId(), followRequestDTO.getFolloweeId());
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public FollowUserVOList getMyFollowUser(@RequestParam Long userId) {
        return followService.getFollowers(userId);
    }
}
