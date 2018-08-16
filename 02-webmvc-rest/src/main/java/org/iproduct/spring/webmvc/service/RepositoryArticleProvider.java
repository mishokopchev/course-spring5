package org.iproduct.spring.webmvc.service;

import org.iproduct.spring.webmvc.dao.ArticleRepository;
import org.iproduct.spring.webmvc.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Primary
public class RepositoryArticleProvider implements ArticleProvider {
    @Autowired
    ArticleRepository repository;

    @Override
    public List<Article> getArticles() {
        return repository.findAll();
    }
}