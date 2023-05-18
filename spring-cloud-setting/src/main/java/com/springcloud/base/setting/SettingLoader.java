package com.springcloud.base.setting;

import com.spring.cloud.base.utils.*;
import com.spring.cloud.base.utils.base.SystemPropsUtil;
import com.spring.cloud.base.utils.map.Resource;
import com.spring.cloud.base.utils.str.StrUtil;
import com.spring.cloud.base.utils.utils.CharUtil;
import com.spring.cloud.base.utils.utils.CharsetUtil;
import com.springcloud.base.log.Log;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @Author: ls
 * @Description: Setting文件加载器
 * @Date: 2023/5/6 10:54
 */
public class SettingLoader {
	private static final Log log = Log.get();

	/**
	 * 注释符号（当有此符号在行首，表示此行为注释）
	 */
	private final static char COMMENT_FLAG_PRE = '#';
	/**
	 * 赋值分隔符（用于分隔键值对）
	 */
	private char assignFlag = '=';
	/**
	 * 变量名称的正则
	 */
	private String varRegex = "\\$\\{(.*?)\\}";

	/**
	 * 本设置对象的字符集
	 */
	private final Charset charset;
	/**
	 * 是否使用变量
	 */
	private final boolean isUseVariable;
	/**
	 * GroupedMap
	 */
	private final GroupedMap groupedMap;

	/**
	 * 构造
	 *
	 * @param groupedMap GroupedMap
	 */
	public SettingLoader(GroupedMap groupedMap) {
		this(groupedMap, CharsetUtil.CHARSET_UTF_8, false);
	}

	/**
	 * 构造
	 *
	 * @param groupedMap    GroupedMap
	 * @param charset       编码
	 * @param isUseVariable 是否使用变量
	 */
	public SettingLoader(GroupedMap groupedMap, Charset charset, boolean isUseVariable) {
		this.groupedMap = groupedMap;
		this.charset = charset;
		this.isUseVariable = isUseVariable;
	}

	/**
	 * 加载设置文件
	 *
	 * @param resource 配置文件URL
	 * @return 加载是否成功
	 */
	public boolean load(Resource resource) {
		if (resource == null) {
			throw new NullPointerException("Null setting url define!");
		}
		log.debug("Load setting file [{}]", resource);
		InputStream settingStream = null;
		try {
			settingStream = resource.getStream();
			load(settingStream);
		} catch (Exception e) {
			log.error(e, "Load setting error!");
			return false;
		} finally {
			IoUtil.close(settingStream);
		}
		return true;
	}

	/**
	 * 加载设置文件。 此方法不会关闭流对象
	 *
	 * @param settingStream 文件流
	 * @return 加载成功与否
	 * @throws IOException IO异常
	 */
	synchronized public boolean load(InputStream settingStream) throws IOException {
		this.groupedMap.clear();
		BufferedReader reader = null;
		try {
			reader = IoUtil.getReader(settingStream, this.charset);
			String group = null;
			String line;
			while (true) {
				line = reader.readLine();
				if (line == null) {
					break;
				}
				line = line.trim();
				if (StrUtil.isBlank(line) || StrUtil.startWith(line, COMMENT_FLAG_PRE)) {
					continue;
				}
				if (StrUtil.isSurround(line, CharUtil.BRACKET_START, CharUtil.BRACKET_END)) {
					group = line.substring(1, line.length() - 1).trim();
					continue;
				}
				final String[] keyValue = StrUtil.splitToArray(line, this.assignFlag, 2);
				if (keyValue.length < 2) {
					continue;
				}
				String value = keyValue[1].trim();
				if (this.isUseVariable) {
					value = replaceVar(group, value);
				}
				this.groupedMap.put(group, keyValue[0].trim(), value);
			}
		} finally {
			IoUtil.close(reader);
		}
		return true;
	}

	/**
	 * 设置变量的正则
	 *
	 * @param regex 正则
	 */
	public void setVarRegex(String regex) {
		this.varRegex = regex;
	}

	/**
	 * 赋值分隔符（用于分隔键值对）
	 *
	 * @param assignFlag 正则
	 * @since 4.6.5
	 */
	public void setAssignFlag(char assignFlag) {
		this.assignFlag = assignFlag;
	}

	/**
	 * 持久化当前设置，会覆盖掉之前的设置
	 *
	 * @param absolutePath 设置文件的绝对路径
	 */
	public void store(String absolutePath) {
		store(FileUtil.touch(absolutePath));
	}

	/**
	 * 持久化当前设置，会覆盖掉之前的设置
	 *
	 * @param file 设置文件
	 * @since 5.4.3
	 */
	public void store(File file) {
		Assert.notNull(file, "File to store must be not null !");
		log.debug("Store Setting to [{}]...", file.getAbsolutePath());
		PrintWriter writer = null;
		try {
			writer = FileUtil.getPrintWriter(file, charset, false);
			store(writer);
		} finally {
			IoUtil.close(writer);
		}
	}

	/**
	 * 存储到Writer
	 *
	 * @param writer Writer
	 */
	synchronized private void store(PrintWriter writer) {
		for (Entry<String, LinkedHashMap<String, String>> groupEntry : this.groupedMap.entrySet()) {
			writer.println(StrUtil.format("{}{}{}", CharUtil.BRACKET_START, groupEntry.getKey(), CharUtil.BRACKET_END));
			for (Entry<String, String> entry : groupEntry.getValue().entrySet()) {
				writer.println(StrUtil.format("{} {} {}", entry.getKey(), this.assignFlag, entry.getValue()));
			}
		}
	}

	/**
	 * 替换给定值中的变量标识
	 *
	 * @param group 所在分组
	 * @param value 值
	 * @return 替换后的字符串
	 */
	private String replaceVar(String group, String value) {
		final Set<String> vars = ReUtil.findAll(varRegex, value, 0, new HashSet<>());
		String key;
		for (String var : vars) {
			key = ReUtil.get(varRegex, var, 1);
			if (StrUtil.isNotBlank(key)) {
				String varValue = this.groupedMap.get(group, key);
				if (null == varValue) {
					final List<String> groupAndKey = StrUtil.split(key, CharUtil.DOT, 2);
					if (groupAndKey.size() > 1) {
						varValue = this.groupedMap.get(groupAndKey.get(0), groupAndKey.get(1));
					}
				}
				if (null == varValue) {
					varValue = SystemPropsUtil.get(key);
				}
				if (null != varValue) {
					value = value.replace(var, varValue);
				}
			}
		}
		return value;
	}
}
