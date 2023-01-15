package com.alan.shop.models;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "REVIEW_PRODUCT")
public class ReviewProduct {
    @Id
    private String id;
    private String userId;
    private String productId;
    private String billId;
    private String content;
    private int star;
    private Timestamp createdAt;
}
