package com.devteria.post.mapper;


import com.devteria.post.dto.response.PostResponse;
import com.devteria.post.entity.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
    public PostResponse toPostResponse(Post post);
}
