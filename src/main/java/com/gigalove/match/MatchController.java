package com.gigalove.match;

import com.gigalove.photo.PhotoRepository;
import com.gigalove.profile.Profile;
import com.gigalove.profile.ProfileRepository;
import com.gigalove.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class MatchController {
    public record SwipeCard(String id, String userId, String name, Integer age, String city, List<String> interests, List<String> photos, Double distance) {}

    private final MatchRepository matchRepository;
    private final ProfileRepository profileRepository;
    private final PhotoRepository photoRepository;

    public MatchController(MatchRepository matchRepository, ProfileRepository profileRepository, PhotoRepository photoRepository) {
        this.matchRepository = matchRepository;
        this.profileRepository = profileRepository;
        this.photoRepository = photoRepository;
    }

    private Long currentUserId() {
        try {
            return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        } catch (Exception e) { return 1L; }
    }

    @GetMapping("/swipes")
    public List<SwipeCard> getCards() {
        Long currentUserId = currentUserId();
        List<Profile> profiles = profileRepository.findAll();
        return profiles.stream()
                .filter(p -> !Objects.equals(p.getUserId(), currentUserId))
                .limit(20)
                .map(p -> new SwipeCard(
                        String.valueOf(p.getUserId()),
                        String.valueOf(p.getUserId()),
                        nullSafeName(p),
                        p.getAge(),
                        p.getCity(),
                        List.of(),
                        photoRepository.findByUserIdOrderBySortOrderAsc(p.getUserId())
                                .stream().map(ph -> ph.getUrl()).collect(Collectors.toList()),
                        2.5
                ))
                .collect(Collectors.toList());
    }

    private String nullSafeName(Profile p) { return Optional.ofNullable(p.getBio()).orElse("Пользователь"); }

    @PostMapping("/swipes/{userId}/like")
    public ResponseEntity<Map<String, Object>> like(@PathVariable Long userId) {
        boolean isMatch = Math.random() > 0.3;
        if (isMatch) {
            MatchEntity m = new MatchEntity();
            m.setUserId1(currentUserId());
            m.setUserId2(userId);
            m.setStatus("PENDING");
            m.setCreatedAt(Instant.now());
            m.setLastMessageAt(Instant.now());
            matchRepository.save(m);
            return ResponseEntity.ok(Map.of("isMatch", true, "matchId", m.getId()));
        }
        return ResponseEntity.ok(Map.of("isMatch", false));
    }

    @GetMapping("/matches")
    public List<MatchEntity> getMatches() { return matchRepository.findAll(); }
}


