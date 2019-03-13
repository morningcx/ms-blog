package com.morningcx.ms.blog.base.enums;

/**
 *
 * @author gcx
 * @date 2019/3/13
 */
public enum OpEnum {
    /**
     * 操作类型
     */
    CREATE("新增"),
    UPDATE("更新"),
    READ("查询"),
    DELETE("删除"),
    RECYCLE("回收"),
    RECOVER("恢复"),
    DOWNLOAD("下载"),
    UPLOAD("上传"),
    LOGIN("登录");

    private String name;

    OpEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
