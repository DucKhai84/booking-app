package com.devteria.post.controller;

import com.devteria.post.dto.request.ApiResponse;
import com.devteria.post.dto.request.PostRequest;
import com.devteria.post.dto.response.PageResponse;
import com.devteria.post.dto.response.PostResponse;
import com.devteria.post.service.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
    PostService post_service;

    @GetMapping("/my-posts")
    ApiResponse<PageResponse<PostResponse>> getAll(@RequestParam(value = "pageIndex", required = false, defaultValue = "1") int page,
                                                   @RequestParam(value = "pageSize", required = false, defaultValue = "10") int size){
        return ApiResponse.<PageResponse<PostResponse>>builder()
                .result(post_service.getAll(page, size))
                .build();
    }


    @PostMapping("/create")
    ApiResponse<PostResponse> create(@RequestBody PostRequest request){
        return ApiResponse.<PostResponse>builder()
                .result(post_service.create(request))
                .build();
    }
}
