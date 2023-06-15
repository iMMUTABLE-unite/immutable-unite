package com.kh.iMMUTABLE.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reply")
@Getter @Setter @ToString
public class Reply {
    @Id
    @Column(name = "reply_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int replyId;
    private String replyComment;
    private LocalDateTime replyDate;

}