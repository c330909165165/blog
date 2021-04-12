package com.dong.blog.service;

import com.dong.blog.NotFoundException;
import com.dong.blog.dao.BlogRepository;
import com.dong.blog.pojo.Blog;
import com.dong.blog.pojo.Type;
import com.dong.blog.util.MarkdownUtils;
import com.dong.blog.util.MyBeanUtils;
import com.dong.vo.BlogQuery;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService{

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public Blog getBlog(Long id) {
        Optional<Blog> byId = blogRepository.findById(id);

        if(!byId.isPresent()){
            throw new NotFoundException("查询不到该博客");
        }
        return byId.get();
    }

    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Optional<Blog> blog = blogRepository.findById(id);
        if(!blog.isPresent()){
            throw new NotFoundException("该博客不存在");
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog.get(),b);
        String content = b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        blogRepository.updateViews(id);
        return b;
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {

        return blogRepository.findAll(new Specification<Blog>() {
            /**
             *root 查询的对象  criteriaQuery 条件容器，把条件放到cq里 criteriaBuilder 条件表达式
             */
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(StringUtils.isNotBlank(blog.getTitle())){
                    predicates.add(cb.like(root.<String>get("title"),"%" + blog.getTitle() + "%"));
                }
                if(blog.getTypeId() != null){
                    predicates.add(cb.equal(root.<Type>get("type").get("id"),blog.getTypeId()));
                }
                if(blog.isRecommend()){
                    predicates.add(cb.equal(root.<Boolean>get("recommend"),blog.isRecommend()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query,pageable);
    }

    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Join join = root.join("tags");
                return cb.equal(join.get("id"),tagId);
            }
        },pageable);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(0,size,sort);
        return blogRepository.findTop(pageable);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYear();
        Map<String,List<Blog>> map = new HashMap<>();
        for(String year : years){
            map.put(year,blogRepository.findByYear(year));
        }
        return map;
    }

    @Override
    public Long countBlog() {
        return blogRepository.count();
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if(blog.getId() == null){
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        }else{
            blog.setUpdateTime(new Date());
        }
        return blogRepository.save(blog);
    }

    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Optional<Blog> byId = blogRepository.findById(id);
        if(!byId.isPresent()){
            throw new NotFoundException("找不到该条博客");
        }
        BeanUtils.copyProperties(blog,byId.get(), MyBeanUtils.getNullPropertyNames(blog));
        byId.get().setUpdateTime(new Date());
        return blogRepository.save(byId.get());
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
}
