package com.example.userservice.controller;

import com.example.userservice.dto.CreateUserDto;
import com.example.userservice.dto.JoinUserDto;
import com.example.userservice.service.JoinUserService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class JoinUserController {

    private final JoinUserService joinUserService;

    @GetMapping
    public ResponseEntity<List<JoinUserDto>> findJoinUser(@PageableDefault(sort = "createdAt",
            direction = Direction.DESC) Pageable pageable) {
        List<JoinUserDto> result = joinUserService.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/users")
    public ResponseEntity<?> save(@RequestBody CreateUserDto createUserDto) {
        Long result = joinUserService.save(createUserDto);
        if (result == 1L) {
            return new ResponseEntity<>("ID가 중복됩니다", HttpStatus.CONFLICT);
        }
        return ResponseEntity.created(URI.create("/users" + result)).build();
    }
}
