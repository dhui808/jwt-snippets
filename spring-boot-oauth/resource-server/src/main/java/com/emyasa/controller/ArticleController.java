package com.emyasa.controller;

import java.util.Arrays;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleController {

    @GetMapping("/articles")
    @PreAuthorize("hasAuthority('articles.read')")
    public List<String> getArticles() {
        return Arrays.asList("Olympics 2024", "World Cup", "Stanley Cup");
    }

}
