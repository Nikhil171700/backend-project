package com.example.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.model.Application;
import com.example.backend.model.FreelancerJob;
import com.example.backend.model.Notification;
import com.example.backend.model.User;
import com.example.backend.repository.NotificationRepository;
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(
            NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    // Create a notification for a specific user
    @Transactional
    public void createNotification(
            User recipient,
            String message,
            String type,
            Long referenceId) {

        Notification notification = new Notification();

        notification.setRecipient(recipient);
        notification.setMessage(message);
        notification.setType(type);
        notification.setReferenceId(referenceId);
        notification.setRead(false);

        notificationRepository.save(notification);
    }
// Notify the freelancer when a client is interested in their post
@Transactional
public void notifyFreelancerAboutClientInterest(
        FreelancerJob freelancerJob,
        User clientUser) {

    User freelancerUser = freelancerJob
            .getFreelancer()
            .getUser();

    String clientName = clientUser.getName();
    String jobTitle = freelancerJob.getTitle();

    createNotification(
            freelancerUser,
            clientName + " is interested in your freelancer post: "
                    + jobTitle,
            "CLIENT_INTEREST",
            freelancerJob.getId()
    );
}
    // Notify the client when a freelancer applies
    @Transactional
    public void notifyClient(Application application) {

        User clientUser = application.getClientJob()
                .getClient()
                .getUser();

        String freelancerName = application.getFreelancer()
                .getUser()
                .getName();

        String jobTitle = application.getClientJob().getTitle();

        createNotification(
                clientUser,
                freelancerName + " applied for your job: " + jobTitle,
                "NEW_APPLICATION",
                application.getId()
        );
    }

    // Notify the freelancer when the client reviews an application
    @Transactional
    public void notifyFreelancer(Application application) {

        User freelancerUser = application.getFreelancer()
                .getUser();

        String status = application.getStatus();
        String jobTitle = application.getClientJob().getTitle();

        String message;

        if ("ACCEPTED".equals(status)) {
            message = "Your application for \"" + jobTitle
                    + "\" has been accepted.";
        } else if ("REJECTED".equals(status)) {
            message = "Your application for \"" + jobTitle
                    + "\" has been rejected.";
        } else {
            return;
        }

        createNotification(
                freelancerUser,
                message,
                "APPLICATION_" + status,
                application.getId()
        );
    }

    // Retrieve all notifications for one user
    public List<Notification> getMyNotifications(String email) {
        return notificationRepository
                .findByRecipient_EmailOrderByCreatedAtDesc(email);
    }

    // Retrieve unread notifications for one user
    public List<Notification> getUnreadNotifications(String email) {
        return notificationRepository
                .findByRecipient_EmailAndReadFalseOrderByCreatedAtDesc(
                        email
                );
    }

    // Count unread notifications for one user
    public long getUnreadCount(String email) {
        return notificationRepository
                .countByRecipient_EmailAndReadFalse(email);
    }

    // Mark a notification as read, only for its recipient
    @Transactional
    public void markAsRead(Long notificationId, String email) {

        Notification notification = notificationRepository
                .findById(notificationId)
                .orElseThrow(() ->
                        new RuntimeException("Notification not found"));

        if (!notification.getRecipient().getEmail().equals(email)) {
            throw new RuntimeException(
                    "You cannot modify another user's notification"
            );
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }
}