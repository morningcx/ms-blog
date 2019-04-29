package com.morningcx.ms.blog.service.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.morningcx.ms.blog.base.util.EntityUtil;
import com.morningcx.ms.blog.entity.AccessLog;
import com.morningcx.ms.blog.entity.Article;
import com.morningcx.ms.blog.mapper.AccessLogMapper;
import com.morningcx.ms.blog.mapper.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author gcx
 * @date 2019/4/29
 */
@Service
public class AccessLogService {
    @Autowired
    private AccessLogMapper accessLogMapper;
    @Autowired
    private ArticleMapper articleMapper;

    /**
     * 分页获取请求日志
     *
     * @param accessLog
     * @param page
     * @param limit
     * @return
     */
    public IPage<AccessLog> listPage(AccessLog accessLog, Integer page, Integer limit) {
        return accessLogMapper.selectPage(new Page<>(page, limit), new QueryWrapper<AccessLog>()
                .setEntity(EntityUtil.removeEmptyString(accessLog))
                .orderByDesc("time"));
    }

    /**
     * 分页获取文章浏览和喜欢日志
     *
     * @param page
     * @param limit
     * @param articleId
     * @param type      0：浏览和喜欢，1：浏览，2：喜欢
     * @return
     */
    public IPage<AccessLog> listArticleLog(Integer page, Integer limit, Integer articleId, Integer type) {
        QueryWrapper<AccessLog> wrapper = new QueryWrapper<>();
        String key = articleId == null ? "" : "[id:" + articleId + "]";
        if (type == null || type > 2) {
            type = 0;
        }
        if (type <= 0 || type == 1) {
            wrapper.likeRight("content", "浏览文章" + key);
        }
        if (type <= 0) {
            wrapper.or();
        }
        if (type <= 0 || type == 2) {
            wrapper.likeRight("content", "喜欢文章" + key);
        }
        wrapper.orderByDesc("time");
        IPage<AccessLog> articleLogPage = accessLogMapper.selectPage(new Page<>(page, limit), wrapper);
        List<AccessLog> records = articleLogPage.getRecords();
        if (records.size() == 0) {
            return new Page<>();
        }
        // 查询出文章的id
        List<String> articleIds = records.stream().map(accessLog -> {
            String content = accessLog.getContent();
            return content.substring(content.indexOf(":") + 1, content.length() - 1);
        }).collect(Collectors.toList());
        Map<Integer, String> articleMap = articleMapper.selectList(new QueryWrapper<Article>()
                .in("id", articleIds).select("id", "title"))
                .stream().collect(Collectors.toMap(Article::getId, Article::getTitle));
        /*records.forEach(accessLog -> {
            String content = accessLog.getContent();
            String operation = content.substring(0, 2);
            String id = content.substring(content.indexOf(":") + 1, content.length() - 1);
            accessLog.setContent(operation + "了文章《" + articleMap.get(Integer.parseInt(id)) + "》");
        });*/
        return articleLogPage;
    }
}
