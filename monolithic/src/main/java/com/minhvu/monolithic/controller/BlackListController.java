package com.minhvu.monolithic.controller;

import com.minhvu.monolithic.service.BlackListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/blacklist")
@RequiredArgsConstructor
public class BlackListController extends BaseController {

    private final BlackListService blocklistService;

    @PostMapping("/block/{blockedUserId}")
    public ResponseEntity<?> blockUser(@PathVariable UUID blockedUserId) {
        UUID blockerId = getCurrentUser().getId();
        blocklistService.blockUser(blockerId, blockedUserId);
        return ResponseEntity.ok("User has been blocked successfully.");
    }

    @DeleteMapping("/unblock/{blockedUserId}")
    public ResponseEntity<?> unblockUser(@PathVariable UUID blockedUserId) {
        UUID blockerId = getCurrentUser().getId();
        blocklistService.unblockUser(blockerId, blockedUserId);
        return ResponseEntity.ok("User has been unblocked successfully.");
    }

    @GetMapping("/isBlocked/{blockedUserId}")
    public ResponseEntity<Boolean> isBlocked(@PathVariable UUID blockedUserId) {
        UUID blockerId = getCurrentUser().getId();
        return ResponseEntity.ok(blocklistService.isBlocked(blockerId, blockedUserId));
    }

    @GetMapping("/isBlockedByUser/{blockedUserId}")
    public ResponseEntity<Boolean> isBlockedByUser(@PathVariable UUID blockedUserId) {
        UUID blockerId = getCurrentUser().getId();
        return ResponseEntity.ok(blocklistService.isBlocked(blockedUserId, blockerId));
    }

    @GetMapping("/blocklist")
    public ResponseEntity<?> getBlockList(@RequestParam(required = false, defaultValue = "0") int page,
                                          @RequestParam(required = false, defaultValue = "10") int size) {
        UUID blockerId = getCurrentUser().getId();
        return ResponseEntity.ok(blocklistService.getBlockListByBlocker(blockerId, page, size));
    }
}
