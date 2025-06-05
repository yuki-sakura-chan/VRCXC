package com.github.vrcxc.controller;

import com.github.vrcxc.domain.entity.GameLogJoinLeave;
import com.github.vrcxc.service.GameLogJoinLeaveService;
import com.github.vrcxc.utils.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/game-log/join-leave")
public class GameLogJoinLeaveController {

    private final GameLogJoinLeaveService gameLogJoinLeaveService;

    public GameLogJoinLeaveController(GameLogJoinLeaveService gameLogJoinLeaveService) {
        this.gameLogJoinLeaveService = gameLogJoinLeaveService;
    }

    @PostMapping
    public R<?> insert(@RequestBody GameLogJoinLeave gameLogJoinLeave) {
        return R.success(gameLogJoinLeaveService.save(gameLogJoinLeave));
    }

    @PostMapping(value = "/batch")
    public R<?> batchInsert(@RequestBody List<GameLogJoinLeave> gameLogJoinLeaves) {
        return R.success(gameLogJoinLeaveService.saveBatch(gameLogJoinLeaves));
    }
}
