package com.morningcx.ms.blog.service.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.morningcx.ms.blog.base.exception.BizException;
import com.morningcx.ms.blog.base.util.ContextUtil;
import com.morningcx.ms.blog.base.util.EntityUtil;
import com.morningcx.ms.blog.entity.*;
import com.morningcx.ms.blog.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @author gcx
 * @date 2019/2/21
 */
@Service
public class ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ContentMapper contentMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 根据id查询文章元信息
     *
     * @param id
     * @return
     */
    public Article getMetaById(Integer id) {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        wrapper.eq("recycle", 0);
        wrapper.eq("author_id", ContextUtil.getLoginId());
        Article article = articleMapper.selectOne(wrapper);
        BizException.throwIfNull(article, "文章不存在");
        // 查询标签
        List<Object> tagIds = articleTagMapper.selectObjs(new QueryWrapper<ArticleTag>()
                .eq("article_id", article.getId())
                .select("tag_id"));
        List<Object> tagNames = tagMapper.selectObjs(new QueryWrapper<Tag>()
                .in("id", tagIds)
                .select("name"));
        article.setTagNames(tagNames.stream().map(o -> (String) o).collect(Collectors.toList()));
        return article;
    }

    /**
     * 修改文章等级（修饰符、推荐、置顶、评论）
     *
     * @param id
     * @param level
     * @param value
     * @return
     */
    @Transactional
    public int updateLevel(Integer id, String level, Integer value) {
        UpdateWrapper<Article> updateWrapper = new UpdateWrapper<Article>()
                .set(level, value)
                .set("update_time", new Date())
                .eq("id", id)
                .eq("recycle", 0);
        return articleMapper.update(null, updateWrapper);
    }


    /**
     * 根据id更新文章元信息
     *
     * @param article
     * @return
     */
    @Transactional
    public int updateMeta(Article article) {
        Integer loginId = ContextUtil.getLoginId();
        // 判断文章
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("id", article.getId());
        wrapper.eq("recycle", 0);
        wrapper.eq("author_id", loginId);
        BizException.throwIf(articleMapper.selectCount(wrapper) == 0, "文章不存在");
        // 判断分类
        QueryWrapper<Category> categoryWrapper = new QueryWrapper<>();
        categoryWrapper.eq("id", article.getCategoryId());
        categoryWrapper.eq("user_id", ContextUtil.getLoginId());
        BizException.throwIf(categoryMapper.selectCount(categoryWrapper) == 0, "分类不存在");
        // 创建新文章对象，防止其他参数混入(比方说点击数)
        Article updateArticle = new Article();
        updateArticle.setId(article.getId());
        updateArticle.setTitle(article.getTitle());
        updateArticle.setIntroduction(article.getIntroduction());
        updateArticle.setCategoryId(article.getCategoryId());
        updateArticle.setType(article.getType());
        updateArticle.setUpdateTime(new Date());
        // 删除原有标签，添加新标签
        articleTagMapper.delete(new QueryWrapper<ArticleTag>().eq("article_id", article.getId()));
        insertTags(article.getId(), article.getTagNames());
        return articleMapper.updateById(updateArticle);
    }

    /**
     * 创建新文章
     *
     * @param article
     */
    @Transactional
    public Integer insertArticle(Article article) {
        // 判断分类
        QueryWrapper<Category> categoryWrapper = new QueryWrapper<>();
        categoryWrapper.eq("id", article.getCategoryId());
        categoryWrapper.eq("user_id", ContextUtil.getLoginId());
        BizException.throwIf(categoryMapper.selectCount(categoryWrapper) == 0, "分类不存在");
        // 插入内容，新建文章默认为空
        Content content = new Content();
        content.setContent("");
        contentMapper.insert(content);
        // 插入文章元信息
        EntityUtil.trim(article);
        article.setContentId(content.getId());
        // 设置默认私密文章
        article.setModifier(1);
        // 设置作者id
        article.setAuthorId(ContextUtil.getLoginId());
        Date now = new Date();
        article.setCreateTime(now);
        article.setUpdateTime(now);
        article.setViews(0);
        article.setLikes(0);
        // todo 是否推荐
        article.setRecommend(0);
        // 是否开启评论
        article.setComment(1);
        article.setRecycle(0);
        // 排序大小，是否置顶
        article.setSort(0);
        article.setDeleted(0);
        articleMapper.insert(article);
        // 插入标签
        insertTags(article.getId(), article.getTagNames());
        return article.getId();
    }

    /**
     * 插入文章标签
     *
     * @param articleId
     * @param tagNames
     */
    private void insertTags(Integer articleId, List<String> tagNames) {
        ArticleTag articleTag = new ArticleTag();
        articleTag.setArticleId(articleId);
        Set<String> set = new HashSet<>();
        for (String tagName : tagNames) {
            tagName = tagName.trim();
            // 标签去重
            if (set.add(tagName)) {
                Tag oldTag = tagMapper.selectOne(new QueryWrapper<Tag>().eq("name", tagName).select("id"));
                // 若原标签不存在，则插入
                if (oldTag == null) {
                    Tag newTag = new Tag();
                    newTag.setName(tagName);
                    newTag.setDescription(tagName);
                    tagMapper.insert(newTag);
                    articleTag.setTagId(newTag.getId());
                } else {
                    articleTag.setTagId(oldTag.getId());
                }
                articleTagMapper.insert(articleTag);
            }
        }
    }

    /**
     * 普通文章列表
     *
     * @param article
     * @param page
     * @param limit
     * @return
     */
    public IPage<Article> listArticle(Article article, Integer page, Integer limit) {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        // 当前登录用户未回收的文章，未删除mp将自动添加
        wrapper.eq("recycle", 0);
        wrapper.eq("author_id", ContextUtil.getLoginId());
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
        // 标签条件
        // select article_id
        // from t_article_tag where tag_id in (54,53) GROUP BY article_id HAVING COUNT(article_id) = 2
        wrapper.setEntity(EntityUtil.removeEmptyString(article));
        // "sort", "recommend",
        wrapper.orderByDesc("create_time");
        IPage<Article> articleIPage = articleMapper.selectPage(new Page<>(page, limit), wrapper);
        // 查询分类
        List<Article> records = articleIPage.getRecords();
        if (records.size() == 0) {
            return new Page<>();
        }
        Set<Integer> categoryIds = records.stream().map(Article::getCategoryId).collect(Collectors.toSet());
        Map<Integer, String> categoryMap = categoryMapper.selectList(new QueryWrapper<Category>()
                .select("id", "name")
                .in("id", categoryIds))
                .stream().collect(Collectors.toMap(Category::getId, Category::getName));
        records.forEach(a -> a.setCategory(categoryMap.get(a.getCategoryId())));
        return articleIPage;
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
     * 回收站文章列表
     *
     * @param page
     * @param limit
     * @return
     */
    public IPage<Article> listRecycleBin(Article article, Integer page, Integer limit) {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        // 回收站中的文章
        wrapper.eq("recycle", 1);
        wrapper.eq("author_id", ContextUtil.getLoginId());
        wrapper.select("id", "title", "introduction", "update_time");
        wrapper.setEntity(EntityUtil.removeEmptyString(article));
        return articleMapper.selectPage(new Page<>(page, limit), wrapper);
    }

    /**
     * 回收文章
     *
     * @param recycleIds
     * @return
     */
    @Transactional
    public Integer recycleArticle(List<Integer> recycleIds) {
        BizException.throwIf(recycleIds == null || recycleIds.size() == 0, "删除文章ID不能为空");
        Integer authorId = ContextUtil.getLoginId();
        int count = articleMapper.recycleBatchIds(recycleIds, authorId);
        BizException.throwIf(count == 0 || count != recycleIds.size(), "删除失败");
        return count;
    }


    /**
     * 恢复文章
     *
     * @param recoverIds
     * @return
     */
    @Transactional
    public Integer recoverArticle(List<Integer> recoverIds) {
        BizException.throwIf(recoverIds == null || recoverIds.size() == 0, "恢复文章ID不能为空");
        Integer authorId = ContextUtil.getLoginId();
        int count = articleMapper.recoverBatchIds(recoverIds, authorId);
        BizException.throwIf(count == 0 || count != recoverIds.size(), "删除失败");
        return count;
    }

    /**
     * 彻底删除id
     *
     * @param deleteIds
     * @return
     */
    @Transactional
    public Integer deleteArticle(List<Integer> deleteIds) {
        BizException.throwIf(deleteIds == null || deleteIds.size() == 0, "彻底删除文章ID不能为空");
        // 限制用户只能删除自己的文章
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("author_id", ContextUtil.getLoginId());
        wrapper.in("id", deleteIds);
        Integer selectCount = articleMapper.selectCount(wrapper);
        BizException.throwIf(selectCount != deleteIds.size(), "文章不存在");
        // 确保文章已经在回收站
        wrapper.eq("recycle", 1);
        Integer recycleCount = articleMapper.selectCount(wrapper);
        BizException.throwIf(recycleCount != deleteIds.size(), "只能彻底删除回收站中的文章");
        int deleteCount = articleMapper.deleteBatchIds(deleteIds);
        // 不用非0判断，因为前面已经判断过了
        BizException.throwIf(deleteCount != deleteIds.size(), "删除失败");
        return deleteCount;
    }
}
