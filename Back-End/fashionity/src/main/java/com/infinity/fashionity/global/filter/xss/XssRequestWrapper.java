package com.infinity.fashionity.global.filter.xss;

import com.infinity.fashionity.global.utils.StringUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.net.URLDecoder;
import java.util.Map;

public class XssRequestWrapper extends HttpServletRequestWrapper {
    private byte[] raw;//inputstream의 데이터를 필터링하기 위한 맴버변수(wrapper는 request별로 각자 new되기 때문에 동시성문제 고려 x)

    public XssRequestWrapper(HttpServletRequest request) {
        super(request);

        try {
            if ("post".equalsIgnoreCase(request.getMethod())
                    && "application/json".equalsIgnoreCase(request.getContentType())
                    || "multipart/form-data".equalsIgnoreCase(request.getContentType())) {

                InputStream is = request.getInputStream();

                this.raw = StringUtils.xssFilter(new String(is.readAllBytes())).getBytes();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (this.raw == null) {
            return super.getInputStream();
        }
        final ByteArrayInputStream bais = new ByteArrayInputStream(this.raw);

        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }

            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
    }

    @Override
    public String getQueryString() {
        return StringUtils.xssFilter(super.getQueryString());
    }

    @Override
    public String getParameter(String name) {
        return StringUtils.xssFilter(super.getParameter(name));
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> params = super.getParameterMap();
        if (params != null) {
            params.forEach((key, value) -> {
                for (int i = 0; i < value.length; i++) {
                    value[i] = StringUtils.xssFilter(value[i]);
                }
            });
        }
        return params;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] params = super.getParameterValues(name);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                params[i] = StringUtils.xssFilter(params[i]);
            }
        }
        return params;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream(), "UTF-8"));
    }
}
