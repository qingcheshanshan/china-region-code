package entity;

import lombok.Data;

import java.util.List;

/**
 * @author dinghao
 * @version 1.0.0
 * @ClassName County.java
 * @Description TODO
 * @createTime 2020年07月23日 10:22:00
 */
@Data
public class County {
    //code
    String code;
    //name
    String name;
    //镇
    List<Country> countries;
}
