package com.protsdev.citizens.dto;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMethod;

public class FormTemplate {
    private String error;
    private String title;
    private String contentType;
    private RequestMethod method;
    private List<Map<String, String>> properties;

    private FormTemplate(Builder builder) {
        this.error = builder.error;
        this.title = builder.title;
        this.contentType = builder.contentType;
        this.method = builder.method;
        this.properties = builder.properties;
    }

    public static class Builder {
        private String error;
        private String title;
        private String contentType;
        private RequestMethod method;
        private List<Map<String, String>> properties;

        public Builder(String title) {
            this.title = title;

            properties = new LinkedList<>();
        }

        public Builder setError(String error) {
            this.error = error;
            return this;
        }

        public Builder setContentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder setMethod(RequestMethod method) {
            this.method = method;
            return this;
        }

        public Builder addProperty(Map<String, String> property) {
            properties.add(property);
            return this;
        }

        public FormTemplate build() {
            return new FormTemplate(this);
        }
    }

    public String getError() {
        return error;
    }

    public String getTitle() {
        return title;
    }

    public String getContentType() {
        return contentType;
    }

    public RequestMethod getMethod() {
        return method;
    }

    public List<Map<String, String>> getProperties() {
        return properties;
    }

}
