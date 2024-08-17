package com.minhvu.feed.controller;

import com.minhvu.feed.dto.CompletReaction;
import com.minhvu.feed.dto.PostDto;
import com.minhvu.feed.dto.PostWithInteractionResponse;
import com.minhvu.feed.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/feeds")
public class FeedsController {
	@Autowired
	private FeedService feedService;

	@GetMapping("/{userId}")
	public HashMap<String, List<PostWithInteractionResponse>> getFeed(@PathVariable("userId") Long userId) {
		return feedService.getFeed(userId);
	}
}
