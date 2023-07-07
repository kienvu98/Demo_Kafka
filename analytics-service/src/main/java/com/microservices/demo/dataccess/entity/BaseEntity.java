package com.microservices.demo.dataccess.entity;

public interface BaseEntity<PK> {
    PK getId();
}
