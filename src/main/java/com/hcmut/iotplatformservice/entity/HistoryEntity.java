package com.hcmut.iotplatformservice.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoryEntity extends BaseEntity {

    private static final long serialVersionUID = -5240560133250596992L;
    private String id;
    private Integer timestamp;
    private Integer value;
}