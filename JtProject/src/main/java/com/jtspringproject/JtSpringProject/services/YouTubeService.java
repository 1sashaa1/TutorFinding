package com.jtspringproject.JtSpringProject.services;

import com.jtspringproject.JtSpringProject.dto.VideoDto;
import com.jtspringproject.JtSpringProject.dto.YouTubeResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class YouTubeService {
    private static final String YT_API_URL = "https://www.googleapis.com/youtube/v3";

    @Value("${google.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public YouTubeService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public List<VideoDto> getChannelVideos(String channelId, int maxResults) {
        String url = String.format("%s/search?part=snippet&channelId=%s&maxResults=%d&key=%s",
                YT_API_URL, channelId, maxResults, apiKey);

        try {
            ResponseEntity<YouTubeResponse> response =
                    restTemplate.getForEntity(url, YouTubeResponse.class);

            return response.getBody().getItems().stream()
                    .map(item -> new VideoDto(
                            item.getId().getVideoId(),
                            item.getSnippet().getTitle(),
                            item.getSnippet().getDescription()
                    ))
                    .collect(Collectors.toList());

        } catch (RestClientException e) {
            throw new RuntimeException("YouTube API error: " + e.getMessage(), e);
        }
    }
}