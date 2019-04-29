package com.morningcx.ms.blog.base.enums;

/**
 *
 * @author gcx
 * @date 2019/3/13
 */
public enum LogTypeEnum {
    /**
     * 操作类型
     */
    CREATE("新增"),
    UPDATE("更新"),
    Query("查询"),
    PAGE("分页"),
    DELETE("删除"),
    RECYCLE("回收"),
    RECOVER("恢复"),
    DOWNLOAD("下载"),
    UPLOAD("上传"),
    LOGIN("登录");

    private String name;

    LogTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
