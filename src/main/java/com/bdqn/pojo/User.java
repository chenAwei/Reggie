package com.bdqn.pojo;

import lombok.Data;
import java.io.Serializable;;

@Data
public class User implements Serializable{
    private Long id;
    private String name;
    private String phone;
    private String sex;
    private String idNumber;
    private String avatar;
    private Integer status;
}
