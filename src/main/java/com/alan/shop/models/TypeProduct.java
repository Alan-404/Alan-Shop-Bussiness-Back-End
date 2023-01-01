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
@Table(name = "TYPE_PRODUCT")
public class TypeProduct {
    @Id
    private String id;
    private String name;
    private Timestamp createdAt;
    private Timestamp modifiedAt;
}
