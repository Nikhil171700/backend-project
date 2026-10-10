// package com.example.backend.repository;

// import java.util.List;

// import org.springframework.data.jpa.repository.JpaRepository;

// import com.example.backend.model.Notification;

// public interface NotificationRepository
//         extends JpaRepository<Notification, Long> {

//     List<Notification>
//     findByRecipient_EmailOrderByCreatedAtDesc(String email);

//     List<Notification>
//     findByRecipient_EmailAndReadFalseOrderByCreatedAtDesc(
//             String email
//     );

//     long countByRecipient_EmailAndReadFalse(String email);
// }
package com.example.backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend.model.Notification;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification> findByRecipient_EmailOrderByCreatedAtDesc(
            String email);

    List<Notification> findByRecipient_EmailAndReadFalseOrderByCreatedAtDesc(
            String email);

    long countByRecipient_EmailAndReadFalse(String email);
}