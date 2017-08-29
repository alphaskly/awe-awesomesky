/**
 * XSSFilter.java
 * com.ipharmacare.platform.filter
 * Function： TODO 
 *
 *   version    date      author
 * ──────────────────────────────────
 *   	1.0	 2015年12月9日    songjy
 *
 * Copyright (c) 2015, TNT All Rights Reserved.
 */

package org.awesky.webapp.interceptors;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONArray;

/**
 * ClassName:XSSFilter
 * 
 * @author songjy
 * @version 1.0
 * @since v1.0
 * @Date 2015年12月9日 下午5:55:47
 */

public class XSSFilter implements Filter {
	private Log log = LogFactory.getLog(this.getClass());
	private String[] DANGEROUS_CHARACTERS;
	private String[] CHARACTERS_DANGEROUS;
	private boolean xssOpen = false;// 是否开启XSS攻击防御,true-开启 false-不开启


	public void init(FilterConfig filterConfig) throws ServletException {

		log.warn("XSSFilter init...");
		String json = filterConfig.getInitParameter("DANGEROUS_CHARACTERS");
		if (StringUtils.isNotBlank(json)) {
			List<String> list = JSONArray.parseArray(json, String.class);
			if (null != list && list.size() > 0) {
				DANGEROUS_CHARACTERS = list.toArray(new String[list.size()]);
			}
		}

		json = filterConfig.getInitParameter("CHARACTERS_DANGEROUS");

		if (StringUtils.isNotBlank(json)) {
			List<String> list = JSONArray.parseArray(json, String.class);
			if (null != list && list.size() > 0) {
				CHARACTERS_DANGEROUS = list.toArray(new String[list.size()]);
			}
		}

		xssOpen = Boolean.parseBoolean(filterConfig.getInitParameter("xssOpen"));

		if ((null == DANGEROUS_CHARACTERS) && (null == CHARACTERS_DANGEROUS)) {
			xssOpen = false;
		}

		log.warn("是否开启XSS攻击防御:" + (xssOpen ? "是" : "否"));
	}


	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (true == xssOpen) {
			chain.doFilter(new XSSServletRequestWrapper((HttpServletRequest) request), response);
		} else {
			chain.doFilter(request, response);
		}

	}


	public void destroy() {
		log.warn("XSSFilter destroy...");
	}

	public class XSSServletRequestWrapper extends HttpServletRequestWrapper {

		public XSSServletRequestWrapper(HttpServletRequest request) {
			super(request);
		}

		@Override
		public Object getAttribute(String name) {
			Object param = super.getAttribute(name);

			if ((null != param) && (param instanceof String)) {
				return rmXSS(param.toString());
			}

			return param;
		}

		@Override
		public String[] getParameterValues(String name) {
			String[] values = super.getParameterValues(name);

			if (null != values && (values.length > 0)) {

				int len = values.length;

				for (int i = 0; i < len; i++) {
					values[i] = rmXSS(values[i]);
				}

			}

			return values;
		}

		@Override
		public String getQueryString() {
			return rmXSS(super.getQueryString());

		}

		@Override
		public String getParameter(String name) {
			return rmXSS(super.getParameter(name));
		}

		@Override
		public Map<String, String[]> getParameterMap() {
			Map<String, String[]> map = super.getParameterMap();

			if (null != map && map.size() > 0) {

				for (Map.Entry<String, String[]> entry : map.entrySet()) {

					String[] values = entry.getValue();

					if (null != values && values.length > 0) {

						int len = values.length;

						for (int i = 0; i < len; i++) {

							if (StringUtils.isBlank(values[i])) {
								continue;
							}

							values[i] = rmXSS(values[i]);
						}

					}

				}

			}

			return map;
		}

		private String rmXSS(String script) {// 移除存在XSS攻击威胁的字符串

			if (StringUtils.isBlank(script)) {
				return script;
			}

			Pattern p = Pattern.compile("<root>.+</root>");// 例外处理
			Matcher m = p.matcher(script);

			if (m.find()) {
				return script;
			}

			if (StringUtils.endsWithIgnoreCase(script, "\56jsp")) {// 例外处理
				return script;
			}

			if (StringUtils.isNotEmpty(StringUtils.substringBetween(script, "【", "】"))) {// 例外处理
				return script;
			}

			script = rmXSSBetween(script, "<", ">");
			script = rmXSSBetween(script, "\"", "\"");
			script = rmXSSBetween(script, "'", "'");
			script = rmXSSBetween(script, "&lt;", ">");
			script = rmXSSBetween(script, "<", "&gt;");
			script = rmXSSBetween(script, "&lt;", "&gt;");
			script = rmXSSBetween(script, "(", ")");
			script = rmXSSDangerousCharacters(script, DANGEROUS_CHARACTERS);
			script = rmXSSDangerousCharacters(CHARACTERS_DANGEROUS, script);

			String[] temp = StringUtils.split(script);
			if (null != temp && temp.length > 0) {
				return temp[0];
			}

			return script;
		}

		private String rmXSSDangerousCharacters(String script, String[] xss) {// 移除存在XSS攻击威胁的字符串

			if (null == xss || xss.length == 0) {
				return script;
			}

			for (String remove : xss) {

				if (StringUtils.isBlank(remove)) {
					continue;
				}

				if (StringUtils.indexOf(script, remove) < 0) {
					continue;
				}

				if (remove.length() == 1) {
					script = StringUtils.remove(script, remove.charAt(0));
					log.warn('[' + remove + "]已移除");
					continue;
				}

				Pattern p = Pattern.compile(remove, Pattern.CASE_INSENSITIVE);
				Matcher m = p.matcher(script);

				while (m.find()) {
					String st = m.group();
					script = StringUtils.remove(script, remove);
					log.warn('[' + st + "]已移除");
				}

			}

			return script;
		}

		private String rmXSSDangerousCharacters(String[] xss, String script) {// 移除存在XSS攻击威胁的字符串

			if (null == xss || xss.length == 0) {
				return script;
			}

			for (String remove : xss) {

				if (StringUtils.isBlank(remove)) {
					continue;
				}

				String regex = remove + ".*" + remove;

				Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
				Matcher m = p.matcher(script);

				while (m.find()) {
					String st = m.group();
					script = StringUtils.remove(script, st);
					log.warn(st + "已移除");
				}
			}

			return script;
		}

		private String rmXSSBetween(String script, final String open, final String close) {// 移除存在XSS攻击威胁的字符串

			String[] xss = StringUtils.substringsBetween(script, open, close);

			if (null != xss && xss.length > 0) {

				for (String xs : xss) {
					script = StringUtils.remove(script, open + xs + close);
					log.warn(open + xs + close + "已移除");
				}
			}

			return script;
		}
	}
}
