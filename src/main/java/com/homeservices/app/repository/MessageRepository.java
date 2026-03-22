package com.homeservices.app.repository;

import com.homeservices.app.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("""
        SELECT m FROM Message m
        WHERE ((m.sender.id = :userId1 AND m.receiver.id = :userId2)
            OR (m.sender.id = :userId2 AND m.receiver.id = :userId1))
        AND m.isDeleted = false
        ORDER BY m.sentAt ASC
        """)
    List<Message> findConversation(Long userId1, Long userId2);

    @Query("""
        SELECT m FROM Message m
        WHERE (m.sender.id = :userId OR m.receiver.id = :userId)
        AND m.isDeleted = false
        ORDER BY m.sentAt DESC
        """)
    List<Message> findAllMessagesByUserId(Long userId);

    long countByReceiverIdAndIsReadFalse(Long receiverId);
}
