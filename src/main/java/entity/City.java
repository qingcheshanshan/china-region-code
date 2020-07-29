package entity;

import lombok.Data;

import java.util.List;

/**
 * @author dinghao
 * @version 1.0.0
 * @ClassName City.java
 * @Description TODO
 * @createTime 2020年07月23日 10:23:00
 */
@Data
public class City {
    //code
    String code;
    //name
    String name;
    //县
    List<County> counties;
}
