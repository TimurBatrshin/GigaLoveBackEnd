package com.gigalove.user;

import com.gigalove.profile.Profile;
import com.gigalove.profile.ProfileRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public UserController(UserRepository userRepository, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profile> getProfile(@PathVariable Long id) {
        Optional<Profile> p = profileRepository.findByUserId(id);
        return p.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profile> updateProfile(@PathVariable Long id, @RequestBody Profile payload) {
        Profile p = profileRepository.findByUserId(id).orElseGet(Profile::new);
        if (p.getId() == null) p.setUserId(id);
        if (payload.getBio() != null) p.setBio(payload.getBio());
        if (payload.getAge() != null) p.setAge(payload.getAge());
        if (payload.getCity() != null) p.setCity(payload.getCity());
        profileRepository.save(p);
        return ResponseEntity.ok(p);
    }
}


