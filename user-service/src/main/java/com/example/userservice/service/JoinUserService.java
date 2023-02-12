package com.example.userservice.service;

import com.example.userservice.dto.CreateUserDto;
import com.example.userservice.dto.JoinUserDto;
import com.example.userservice.entity.JoinUser;
import com.example.userservice.repository.JoinUserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JoinUserService {

    private final JoinUserRepository joinUserRepository;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public List<JoinUserDto> findAll(Pageable pageable) {
        return joinUserRepository.findAllBy(pageable).stream()
                .map(JoinUserDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long save(CreateUserDto createUserDto) {
        String loginId = createUserDto.getLoginId();
        if (joinUserRepository.existsByLoginId(loginId)) {
            return -1L;
        }

        JoinUser saved = joinUserRepository.save(createUserDto.toEntity());
        sendMessage(new JoinUserDto(saved));

        return saved.getId();
    }

    private void sendMessage(JoinUserDto joinUserDto) {
        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String data = null;
        try {
            data = writer.writeValueAsString(joinUserDto);

            kafkaTemplate.send("join-user", data);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        log.info("content kafka send - " + data);
    }
}
