package com.furkancelik.arizakayitsistemi.repository;

import com.furkancelik.arizakayitsistemi.model.FileAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FileAttachmentRepository extends JpaRepository<FileAttachment, Long> {

    List<FileAttachment> findByDateBeforeAndPostIsNull(Date date);
}
