package org.iproduct.spring.beanconfig;

import java.time.LocalDateTime;
import java.util.Objects;

public class Article {
    private String title;
    private String content;
    private LocalDateTime created;

    public Article() {
    }

    public Article(String title, String content) {
        this.title = title;
        this.content = content;
        created = LocalDateTime.now();
    }

    public Article(String title, String content, LocalDateTime created) {
        this.title = title;
        this.content = content;
        this.created = created;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article)) return false;
        Article article = (Article) o;
        return Objects.equals(title, article.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Article{");
        sb.append("title='").append(title).append('\'');
        sb.append(", content='").append(content).append('\'');
        sb.append(", created=").append(created);
        sb.append('}');
        return sb.toString();
    }
}
