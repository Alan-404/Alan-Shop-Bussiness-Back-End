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
@Table(name = "DISTRIBUTORS")
public class Distributor {
    @Id
    private String id;
    private String userId;
    private String description;
    private Timestamp registeredAt;
    private Timestamp modifiedAt;
}
