package com.risen.phoenix.jdbc.core.pnd;

import com.risen.phoenix.jdbc.util.CaseUtils;
import org.springframework.util.StringUtils;

public class SimpleSqlExp implements ISqlExp {

    private String snippet;

    public SimpleSqlExp(String snippet) {
        this.snippet = snippet;
    }

    public SimpleSqlExp(String... snippets) {
        if (snippets.length > 2){
            snippets[0] = CaseUtils.lowerCamel(snippets[0]);
        }
        this.snippet = StringUtils.arrayToDelimitedString(snippets, " ");
    }

    public SimpleSqlExp append(String... snippets) {
        this.snippet = StringUtils.arrayToDelimitedString(snippets, " ");
        return this;
    }

    @Override
    public boolean isEmpty() {
        return !StringUtils.hasText(this.snippet);
    }

    @Override
    public String getSql() {
        return this.snippet;
    }

    @Override
    public String toString() {
        return this.getSql();
    }
}
