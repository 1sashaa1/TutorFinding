package com.jtspringproject.JtSpringProject.controller;

import com.jtspringproject.JtSpringProject.services.YouTubeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/youtube")
public class YouTubeController {
    private final YouTubeService youTubeService;

    public YouTubeController(YouTubeService youTubeService) {
        this.youTubeService = youTubeService;
    }

    @GetMapping("/videos")
    public ResponseEntity<?> getVideos(
            @RequestParam String channelId,
            @RequestParam(defaultValue = "10") int maxResults) {

        try {
            return ResponseEntity.ok(youTubeService.getChannelVideos(channelId, maxResults));
        } catch (Exception e) {  // Используем общее исключение
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", "An error occurred while fetching data: " + e.getMessage()));
        }
    }
}