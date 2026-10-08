package com.devteria.post.service;

import com.devteria.post.dto.request.PostRequest;
import com.devteria.post.dto.response.PageResponse;
import com.devteria.post.dto.response.PostResponse;
import com.devteria.post.dto.response.UserProfileResponse;
import com.devteria.post.entity.Post;
import com.devteria.post.mapper.PostMapper;
import com.devteria.post.repository.PostRepository;
import com.devteria.post.repository.httpClient.ProfileClient;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PostService {

    DateTimeFormatter dateTimeFormatter;

    PostRepository post_repository;
    PostMapper post_mapper;
    ProfileClient profile_client;

    public PageResponse<PostResponse> getMyPost(int page, int size){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userId = authentication.getName();

        UserProfileResponse userProfile = null;

        try {
            userProfile = profile_client.getProfile(userId).getResult();
        }catch(Exception e){
            log.error("Error fetching user profile");
        }

        Sort sort = Sort.by("createdAt").descending();

        Pageable pageable = PageRequest.of(page - 1, size, sort);

        var pageData = post_repository.findAllByUserId(userId, pageable);

        String lastName = userProfile != null ? userProfile.getLastName() : null;
        var postList = pageData.getContent().stream().map(post -> {
            var postResponse = post_mapper.toPostResponse(post);
            postResponse.setCreated(dateTimeFormatter.format(post.getCreatedAt()));
            postResponse.setUserName(lastName);
            return postResponse;
        }).toList();

        return PageResponse.<PostResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(postList)
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
