package com.furkancelik.arizakayitsistemi.dto;

import com.furkancelik.arizakayitsistemi.model.FileAttachment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileAttachmentDTO {

    private String name;
    private String fileType;

    public FileAttachmentDTO(FileAttachment attachment) {
        this.name = attachment.getName();
        this.fileType = attachment.getFileType();
    }
}
