package entity;

import lombok.Data;

import java.util.List;

/**
 * @author dinghao
 * @version 1.0.0
 * @ClassName Country.java
 * @Description TODO
 * @createTime 2020年07月23日 10:23:00
 */
@Data
public class Country {
    //code
    String code;
    //name
    String name;
    //村
    List<Town> towns;
}
