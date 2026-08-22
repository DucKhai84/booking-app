package com.devteria.profile.controller;

import com.devteria.profile.service.UserProfileService;
import com.devteria.profile.dto.response.UserProfileResponse;
import com.devteria.profile.dto.request.ProfileCreationRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileController {

    UserProfileService userProfileService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    List<UserProfileResponse> getAll (){
        return userProfileService.getAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    UserProfileResponse getProfile(@PathVariable String id){
        return userProfileService.getProfile(id);
    }
}
