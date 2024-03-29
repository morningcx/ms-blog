package com.morningcx.ms.blog.service.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.morningcx.ms.blog.base.exception.BizException;
import com.morningcx.ms.blog.base.util.ContextUtil;
import com.morningcx.ms.blog.entity.*;
import com.morningcx.ms.blog.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author gcx
 * @date 2019/3/27
 */
@Service
public class WebArticleService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ContentMapper contentMapper;

    /**
     * 分页查询文章，包括分类条件查询、标签条件查询
     *
     * @param article
     * @param page
     * @param limit
     * @return
     */
    public IPage<Article> listArticle(Article article, Integer page, Integer limit) {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("recycle", 0);
        wrapper.eq("modifier", 0);
        // 分类条件
        if (article.getCategoryId() != null) {
            // 获取分类条件下所有子分类文章
            Set<Object> childNodeIds = getChildNodeIds(Collections.singletonList(article.getCategoryId()), new HashSet<>());
            wrapper.in("category_id", childNodeIds);
            article.setCategoryId(null);
        }
        // 单个标签条件
        if (article.getTagNames() != null && article.getTagNames().size() != 0) {
            List<Object> articleIds = articleTagMapper.selectObjs(new QueryWrapper<ArticleTag>().lambda()
                    .eq(ArticleTag::getTagId, article.getTagNames().get(0))
                    .select(ArticleTag::getArticleId));
            if (articleIds.size() == 0) {
                return new Page<>();
            }
            wrapper.in("id", articleIds);
        }
        wrapper.select("id", "title", "introduction", "create_time", "category_id", "views", "likes", "sort", "recommend");
        // 先按置顶再按创建日期，推荐文章还是不要置顶了
        wrapper.orderByDesc("sort", "create_time");
        IPage<Article> articleIPage = articleMapper.selectPage(new Page<>(page, limit > 20 ? 20 : limit), wrapper);
        // 列表为空则不需要进行分类和标签操作
        List<Article> records = articleIPage.getRecords();
        if (records.size() == 0) {
            return new Page<>();
        }
        // 查询标签
        List<Integer> articleIds = records.stream().map(Article::getId).collect(Collectors.toList());
        StringBuilder orderBy = new StringBuilder();
        articleIds.forEach(a -> orderBy.append(",").append(a));
        List<Object> articleTagIds = articleTagMapper.selectObjs(new QueryWrapper<ArticleTag>()
                .in("article_id", articleIds)
                .groupBy("article_id")
                // 按照in的顺序返回
                .orderByAsc("field(article_id" + orderBy + ")")
                .select("GROUP_CONCAT(CAST(tag_id AS char))"));
        Set<String> allTagIds = new HashSet<>();
        articleTagIds.forEach(at -> allTagIds.addAll(Arrays.asList(((String) at).split(","))));
        Map<Integer, String> tagMap = tagMapper.selectList(new QueryWrapper<Tag>().in("id", allTagIds).select("id", "name"))
                .stream().collect(Collectors.toMap(Tag::getId, Tag::getName));
        for (int i = 0; i < records.size(); i++) {
            String[] tagIds = ((String) articleTagIds.get(i)).split(",");
            List<String> tags = new ArrayList<>(tagIds.length);
            for (String tagId : tagIds) {
                tags.add(tagId + "," + tagMap.get(Integer.parseInt(tagId)));
            }
            records.get(i).setTagNames(tags);
        }
        // 查询分类
        Set<Integer> categoryIds = records.stream().map(Article::getCategoryId).collect(Collectors.toSet());
        Map<Integer, Category> categoryMap = categoryMapper.selectList(new QueryWrapper<Category>()
                .select("id", "name")
                .in("id", categoryIds))
                .stream().collect(Collectors.toMap(Category::getId, Function.identity()));
        records.forEach(a -> a.setCategory(categoryMap.get(a.getCategoryId()).getName()));
        return articleIPage;
    }

    /**
     * 根据条件查询热门文章列表(所有文章排行、分类频道排行)
     *
     * @param article
     * @return
     */
    public List<Article> listHotArticles(Article article, Integer page, Integer limit) {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("modifier", 0);
        wrapper.eq("recycle", 0);
        if (article.getCategoryId() != null) {
            // 获取分类条件下所有子分类文章
            Set<Object> childNodeIds = getChildNodeIds(Collections.singletonList(article.getCategoryId()), new HashSet<>());
            wrapper.in("category_id", childNodeIds);
            article.setCategoryId(null);
        }
        // 单个标签条件
        if (article.getTagNames() != null && article.getTagNames().size() != 0) {
            List<Object> articleIds = articleTagMapper.selectObjs(new QueryWrapper<ArticleTag>().lambda()
                    .eq(ArticleTag::getTagId, article.getTagNames().get(0))
                    .select(ArticleTag::getArticleId));
            if (articleIds.size() == 0) {
                return new Page<Article>().getRecords();
            }
            wrapper.in("id", articleIds);
        }
        wrapper.orderByDesc("views");
        wrapper.select("id", "title", "create_time", "views");
        return articleMapper.selectPage(new Page<>(page, limit > 10 ? 10 : limit), wrapper).getRecords();
    }

    /**
     * 获取子分类id，BFS
     *
     * @param pids
     * @return
     */
    private Set<Object> getChildNodeIds(List<Object> pids, Set<Object> set) {
        if (pids == null || pids.size() == 0) {
            return set;
        }
        for (Object pid : pids) {
            // 避免无限递归
            if (!set.add(pid)) {
                return set;
            }
        }
        List<Object> childIds = categoryMapper.selectObjs(
                new QueryWrapper<Category>().in("pid", pids).select("id"));
        return getChildNodeIds(childIds, set);
    }

    /**
     * t_article单表分页，查询简单的文章信息(用于ArticleCell页面)
     *
     * @return
     */
    public IPage<Article> listSimpleArticles(Integer page, Integer limit) {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("modifier", 0);
        wrapper.eq("recycle", 0);
        wrapper.select("id", "title", "introduction", "create_time", "views", "likes", "sort", "recommend");
        // 先按置顶再按创建日期
        wrapper.orderByDesc("sort", "create_time");
        return articleMapper.selectPage(new Page<>(page, limit), wrapper);
    }

    /**
     * 列举最近的前9篇推荐文章
     *
     * @return
     */
    public List<Article> listRecommendArticles() {
        return articleMapper.selectList(new QueryWrapper<Article>()
                .eq("modifier", 0)
                .eq("recycle", 0)
                .eq("recommend", 1)
                .select("id", "title", "introduction")
                .orderByDesc("create_time")
                .last("limit 9"));
    }

    /**
     * 查询归档信息
     *
     * @param page
     * @param limit
     * @return
     */
    public IPage<Article> listArchives(Integer page, Integer limit) {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("modifier", 0);
        wrapper.eq("recycle", 0);
        wrapper.select("id", "title", "create_time");
        wrapper.orderByDesc("create_time");
        return articleMapper.selectPage(new Page<>(page, limit > 20 ? 20 : limit), wrapper);
    }

    /**
     * 获取文章全部信息
     *
     * @param id
     * @return
     */
    public Article getFullById(Integer id) {
        // todo 带上config参数指向首页
        Article article = articleMapper.selectById(id);
        BizException.throwIfNull(article, "文章" + id + "不存在");
        // 设置访问限制，用户未登录，或者用户登录但文章不属于自己，则只能访问公开并未回收的文章
        if (!article.getAuthorId().equals(getLoginId())) {
            BizException.throwIf(article.getModifier() == 1 || article.getRecycle() == 1, "文章不存在");
        }
        // 作者
        User user = userMapper.selectOne(new QueryWrapper<User>()
                .eq("id", article.getAuthorId())
                .select("name"));
        article.setAuthor(user == null ? "" : user.getName());
        // 分类
        Category category = categoryMapper.selectOne(new QueryWrapper<Category>()
                .eq("id", article.getCategoryId())
                .select("name"));
        article.setCategory(category == null ? "" : category.getName());
        // todo 附带上标签id 标签
        List<Tag> tags = tagMapper.selectList(new QueryWrapper<Tag>()
                .inSql("id", "select tag_id from t_article_tag where article_id=" + article.getId())
                .select("name"));
        article.setTagNames(tags.stream().map(Tag::getName).collect(Collectors.toList()));
        // 内容
        Content content = contentMapper.selectById(article.getContentId());
        article.setContent(content);
        // 更新浏览次数 todo 重复
        articleMapper.updateViewsById(id);
        return article;
    }

    /**
     * 更新文章点赞次数
     *
     * @param id
     * @return
     */
    public int updateLikesById(Integer id) {
        // todo 重复点击(其实和浏览次数是同一个判断)，私密或回收文章添加点赞限制
        return articleMapper.updateLikesById(id);
    }

    /**
     * 返回当前登录用户的id，没有session则说明未登录
     *
     * @return
     */
    private Integer getLoginId() {
        HttpSession s = ContextUtil.getAttributes().getRequest().getSession(false);
        if (s == null) {
            return null;
        }
        return (Integer) s.getAttribute(ContextUtil.LOGIN_USER_ID);
    }
}

