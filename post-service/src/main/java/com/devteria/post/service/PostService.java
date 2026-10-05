package com.devteria.post.service;

import com.devteria.post.dto.request.PostRequest;
import com.devteria.post.dto.response.PageResponse;
import com.devteria.post.dto.response.PostResponse;
import com.devteria.post.entity.Post;
import com.devteria.post.mapper.PostMapper;
import com.devteria.post.repository.PostRepository;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static java.util.stream.Collectors.toList;

@Service
@Data
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostService {

    PostRepository post_repository;
    PostMapper post_mapper;

    public PageResponse<PostResponse> getAll(int page, int size){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userId = authentication.getName();

        Sort sort = Sort.by("createdAt").descending();

        Pageable pageable = PageRequest.of(page - 1, size, sort);

        var pageData = post_repository.findAllByUserId(userId, pageable);

        return PageResponse.<PostResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream().map(post_mapper::toPostResponse).toList())
                .build();
    }

    public PostResponse create (PostRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Post post = Post.builder()
                .content(request.getContent())
                .userId(authentication.getName())
                .createdAt(Instant.now())
                .modifiedAt(Instant.now())
                .build();
        post = post_repository.save(post);
        return post_mapper.toPostResponse(post);
    }
}
