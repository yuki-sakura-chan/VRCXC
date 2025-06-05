package com.github.vrcxc.controller;

import com.github.vrcxc.domain.entity.Memos;
import com.github.vrcxc.service.MemosService;
import com.github.vrcxc.utils.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/memos")
public class MemosController {

    private final MemosService memosService;

    public MemosController(MemosService memosService) {
        this.memosService = memosService;
    }

    @PostMapping
    public R<?> insertMemos(@RequestBody Memos memos) {
        return R.success(memosService.save(memos));
    }

}
