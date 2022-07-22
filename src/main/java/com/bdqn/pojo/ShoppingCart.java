package com.bdqn.pojo;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ShoppingCart implements Serializable{
    private Long id;
    private String name;
    private Long userId;
    private Long dishId;
    private Long setmealId;
    private String dishFlavor;
    private Integer number;
    private BigDecimal amount;
    private String image;
    private Date createTime;
}
