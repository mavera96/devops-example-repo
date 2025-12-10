package com.example.helloworld.web;

import com.example.helloworld.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping({ "", "message" })
    public ResponseEntity<String> getMessage() {
        return ResponseEntity.ok(messageService.getMessage());
    }
}
