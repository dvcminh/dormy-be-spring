package com.minhvu.monolithic.repository;

import com.minhvu.monolithic.entity.Message;
import com.minhvu.monolithic.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findAll();

    List<Message> findBySenderNameAndReceiverName(String senderName, String receiverName);

    List<Message> findByReceiverNameAndStatusAllIgnoreCaseOrderByDateAsc(String receiverName, Status status);

    List<Message> findBySenderNameAndReceiverNameAndStatusAllIgnoreCaseOrderByDateAsc(String senderName, String receiverName, Status status);
}