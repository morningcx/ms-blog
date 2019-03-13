package com.morningcx.ms.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.morningcx.ms.blog.base.exception.BizException;
import com.morningcx.ms.blog.base.util.EntityUtil;
import com.morningcx.ms.blog.base.util.ContextUtil;
import com.morningcx.ms.blog.entity.Article;
import com.morningcx.ms.blog.entity.ArticleTag;
import com.morningcx.ms.blog.entity.Content;
import com.morningcx.ms.blog.entity.Tag;
import com.morningcx.ms.blog.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
        article.setTags(articleTagMapper.listTagsByArticleId(id));
        return article;
    }

    /**
     * 根据id更新文章元信息
     *
     * @param article
     * @return
     */
    @Transactional
    public int updateMeta(Article article) {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("id", article.getId());
        wrapper.eq("recycle", 0);
        wrapper.eq("author_id", ContextUtil.getLoginId());
        BizException.throwIf(articleMapper.selectCount(wrapper) == 0, "文章不存在");
        // 创建新文章对象，防止其他参数混入
        Article updateArticle = new Article();
        updateArticle.setTitle(article.getTitle());
        updateArticle.setIntroduction(article.getIntroduction());
        updateArticle.setCategoryId(article.getCategoryId());
        updateArticle.setType(article.getType());
        updateArticle.setUpdateTime(new Date());
        // 删除原有标签，添加新标签
        articleTagMapper.delete(new QueryWrapper<ArticleTag>().eq("article_id", article.getId()));
        insertTags(article.getId(), article.getTags());
        return articleMapper.update(updateArticle, wrapper);
    }

    /**
     * 创建新文章
     *
     * @param article
     */
    @Transactional
    public Integer insertArticle(Article article) {
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
        article.setRecycle(0);
        article.setDeleted(0);
        articleMapper.insert(article);

        // 插入标签
        insertTags(article.getId(), article.getTags());
        return article.getId();
    }

    /**
     * 插入文章标签
     *
     * @param articleId
     * @param tags
     */
    private void insertTags(Integer articleId, List<Tag> tags) {
        ArticleTag articleTag = new ArticleTag();
        articleTag.setArticleId(articleId);
        Set<String> set = new HashSet<>();
        for (Tag tag : tags) {
            // 对标签名称进行修剪
            EntityUtil.trim(tag);
            // 标签去重
            if (set.add(tag.getName())) {
                Tag oldTag = tagMapper.selectOne(new QueryWrapper<Tag>().eq("name", tag.getName()));
                // 若原标签不存在，则插入
                if (oldTag == null) {
                    tagMapper.insert(tag);
                    articleTag.setTagId(tag.getId());
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
    public IPage<Article> listArticlesByCondition(Article article, Integer page, Integer limit) {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        // 当前登录用户未回收的文章，未删除mp将自动添加
        wrapper.eq("recycle", 0);
        wrapper.eq("author_id", ContextUtil.getLoginId());
        // 其他条件，清空article实体类内部recycle和author_id条件，防止重复
        article.setRecycle(null);
        article.setAuthorId(null);
        wrapper.setEntity(article);
        return articleMapper.selectPage(new Page<>(page, limit), wrapper);
    }

    /**
     * 回收站文章列表
     *
     * @param page
     * @param limit
     * @return
     */
    public IPage<Article> listRecycleBin(Integer page, Integer limit) {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        // 回收站中的文章
        wrapper.eq("recycle", 1);
        wrapper.eq("author_id", ContextUtil.getLoginId());
        // todo 回收站返回的数据不需要这么多，限定查询列名
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
        // todo 彻底删除文章没有update_time，没有作者id判断
        BizException.throwIf(deleteIds.size() == 0, "彻底删除文章ID不能为空");
        int count = articleMapper.deleteBatchIds(deleteIds);
        BizException.throwIf(count == 0 || count != deleteIds.size(), "删除失败");
        return count;
    }

    /*public List<Tag> listTagsByArticleId(Integer id) {
        QueryWrapper<ArticleTag> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id", id);
        List<ArticleTag> articleTags = articleTagMapper.selectList(wrapper);
        List<Integer> tagIds = new ArrayList<>();
        for (ArticleTag articleTag : articleTags) {
            tagIds.add(articleTag.getTagId());
        }
        return tagMapper.selectBatchIds(tagIds);
    }*/
}
