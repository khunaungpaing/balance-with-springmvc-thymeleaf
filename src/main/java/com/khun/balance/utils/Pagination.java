package com.khun.balance.utils;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Pagination {
    private int current;
    private int total;
    private boolean first;
    private boolean last;
    private String url;
    private List<Integer> pages;
    private Map<String, String> param;
    private List<Integer> size;
    private String sizeChangeFormId;

    public boolean isShow(){
        return pages.size()>1;
    }

    public static Builder builder(String url) {
        return new Builder(url);
    }

    public static class Builder {
        private int current;
        private int total;
        private boolean first;
        private boolean last;
        private String url;
        private Map<String, String> param;
        private List<Integer> size;
        private String sizeChangeFormId;


        public Builder(String url) {
            this.url = url;
        }

        public <T> Builder page(Page<T> page) {
            this.current = page.getNumber();
            this.total = page.getTotalPages();
            this.first = page.isFirst();
            this.last = page.isLast();
            return this;
        }

        public Builder current(int current) {
            this.current = current;
            return this;
        }

        public Builder total(int total) {
            this.total = total;
            return this;
        }

        public Builder first(boolean first) {
            this.first = first;
            return this;
        }

        public Builder last(boolean last) {
            this.last = last;
            return this;
        }

        public Builder params(Map<String, String> param) {
            this.param = param;
            return this;
        }

        public Builder size(List<Integer> size) {
            this.size = size;
            return this;
        }

        public Builder sizeChangeFormId(String sizeChangeFormId) {
            this.sizeChangeFormId = sizeChangeFormId;
            return this;
        }


        public Pagination build() {
            return new Pagination(current, total, first, last, url, param, size,sizeChangeFormId);
        }
    }

    private Pagination(int current, int total, boolean first, boolean last, String url, Map<String, String> param, List<Integer> size,String sizeChangeFormId) {
        this.current = current;
        this.total = total;
        this.first = first;
        this.last = last;
        this.url = url;
        this.param = param == null ? new HashMap<>() : param;
        this.size = size == null ? new ArrayList<>() : size;
        this.sizeChangeFormId = sizeChangeFormId;

        this.pages = new ArrayList<>();
        this.pages.add(current);

        while (pages.size() < 3 && pages.get(0) > 0) {
            pages.add(0, pages.get(0) - 1);
        }
        while (pages.size() < 5 && pages.get(pages.size() - 1) < total - 1) {
            pages.add(pages.get(pages.size() - 1) + 1);
        }
    }

    public String getParam() {
        return param.entrySet().stream().map(e -> "%s=%s".formatted(e.getKey(), e.getValue()))
                .reduce("", "%s&%s"::formatted);
    }
}
