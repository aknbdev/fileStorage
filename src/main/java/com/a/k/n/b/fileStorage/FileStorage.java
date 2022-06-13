package com.a.k.n.b.fileStorage;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity(name = ("file_storage"))
public class FileStorage {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = ("name"))
    private String name;

    @Column(name = ("extension"))
    private String extension;

    @Column(name = ("file-size"))
    private Long fileSize;

    @Column(name = ("token"))
    private String token;

    @Column(name = ("content-type"))
    private String contentType;

    @Column(name = ("upload-path"))
    private String uploadPath;

    @Column(name = ("file-storage-status"))
    @Enumerated(STRING)
    private FileStorageStatus fileStorageStatus;
}
