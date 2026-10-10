// package com.example.backend.controller;

// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.Authentication;
// import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PatchMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.example.backend.model.Notification;
// import com.example.backend.service.NotificationService;

// @RestController
// @RequestMapping("/notifications")
// @CrossOrigin(origins = "http://localhost:5173")
// public class NotificationController {

//     private final NotificationService notificationService;

//     public NotificationController(
//             NotificationService notificationService) {
//         this.notificationService = notificationService;
//     }

//     private Map<String, Object> toResponse(Notification notification) {
//         Map<String, Object> result = new HashMap<>();

//         result.put("id", notification.getId());
//         result.put("message", notification.getMessage());
//         result.put("type", notification.getType());
//         result.put("referenceId", notification.getReferenceId());
//         result.put("read", notification.isRead());
//         result.put("createdAt", notification.getCreatedAt());

//         return result;
//     }

//     @GetMapping
//     public List<Map<String, Object>> getMyNotifications(
//             Authentication authentication) {

//         return notificationService
//                 .getMyNotifications(authentication.getName())
//                 .stream()
//                 .map(this::toResponse)
//                 .toList();
//     }

//     @GetMapping("/unread")
//     public List<Map<String, Object>> getUnreadNotifications(
//             Authentication authentication) {

//         return notificationService
//                 .getUnreadNotifications(authentication.getName())
//                 .stream()
//                 .map(this::toResponse)
//                 .toList();
//     }

//     @GetMapping("/unread/count")
//     public Map<String, Long> getUnreadCount(
//             Authentication authentication) {

//         return Map.of(
//                 "count",
//                 notificationService.getUnreadCount(
//                         authentication.getName())
//         );
//     }

//     @PatchMapping("/{notificationId}/read")
//     public ResponseEntity<?> markAsRead(
//             @PathVariable Long notificationId,
//             Authentication authentication) {

//         try {
//             notificationService.markAsRead(
//                     notificationId,
//                     authentication.getName()
//             );

//             return ResponseEntity.ok(
//                     Map.of("message", "Notification marked as read")
//             );

//         } catch (RuntimeException e) {
//             return ResponseEntity.badRequest().body(
//                     Map.of("message", e.getMessage())
//             );
//         }
//     }
// }


package com.example.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.model.Notification;
import com.example.backend.service.NotificationService;

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "http://localhost:5173")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<?> getNotifications(
            Authentication authentication) {

        if (authentication == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Please log in"));
        }

        List<Map<String, Object>> result =
                notificationService
                        .getMyNotifications(authentication.getName())
                        .stream()
                        .map(this::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/unread")
    public ResponseEntity<?> getUnreadNotifications(
            Authentication authentication) {

        if (authentication == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Please log in"));
        }

        List<Map<String, Object>> result =
                notificationService
                        .getUnreadNotifications(authentication.getName())
                        .stream()
                        .map(this::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/unread/count")
    public ResponseEntity<?> getUnreadCount(
            Authentication authentication) {

        if (authentication == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Please log in"));
        }

        long count = notificationService.getUnreadCount(
                authentication.getName());

        return ResponseEntity.ok(Map.of("count", count));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(
            @PathVariable Long id,
            Authentication authentication) {

        if (authentication == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Please log in"));
        }

        try {
            notificationService.markAsRead(
                    id, authentication.getName());

            return ResponseEntity.ok(
                    Map.of("message", "Notification marked as read"));

        } catch (RuntimeException e) {
            return ResponseEntity.status(403)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    private Map<String, Object> toResponse(
            Notification notification) {

        Map<String, Object> response = new HashMap<>();

        response.put("id", notification.getId());
        response.put("message", notification.getMessage());
        response.put("type", notification.getType());
        response.put("referenceId", notification.getReferenceId());
        response.put("read", notification.isRead());
        response.put("createdAt", notification.getCreatedAt());

        return response;
    }
}