package entity;

import lombok.Data;

/**
 * @author dinghao
 * @version 1.0.0
 * @ClassName RegionTable.java
 * @Description TODO
 * @createTime 2020年07月23日 10:21:00
 */
@Data
public class RegionTable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 地区代码
     */
    private String code;

    /**
     * 地区名称
     */
    private String name;

    /**
     * 等级类型
     */
    private String type;

    /**
     * 城乡分类代码
     */
    private String classification;

    /**
     * pcode
    **/
    private String pcode;
    /**
     * region_table
     */
    private static final long serialVersionUID = 1L;



}
