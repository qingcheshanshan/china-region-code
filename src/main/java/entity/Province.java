package entity;

import lombok.Data;

import java.util.List;

/**
 * @author dinghao
 * @version 1.0.0
 * @ClassName Province.java
 * @Description TODO
 * @createTime 2020年07月23日 10:22:00
 */
@Data
public class Province {
    //code
    String code;
    //name
    String name;
    //市
    List<City> cities;

}
