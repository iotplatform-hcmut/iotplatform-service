package com.hcmut.iotplatformservice.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MoisEntity extends BaseEntity {
    private static final long serialVersionUID = 7368350684486399059L;
    private String id;
    private List<Integer> values;
}