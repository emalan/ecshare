package com.madalla.service.blog;

import java.util.List;

public interface IBlogService {

    public abstract List<BlogEntry> getBlogEntries(String category);
    public abstract List<BlogEntry> getBlogEntries();
}
