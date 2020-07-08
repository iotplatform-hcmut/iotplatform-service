package com.hcmut.iotplatformservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity extends BaseEntity {
    private static final long serialVersionUID = 5034810906970598721L;
    private int id;
    private String name;
    private String username;
    private String email;
    private Long phone;
    private Integer birthday;
}