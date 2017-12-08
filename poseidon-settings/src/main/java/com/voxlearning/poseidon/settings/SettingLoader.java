package com.voxlearning.poseidon.settings;

import com.voxlearning.poseidon.core.exceptions.IORuntimeException;
import com.voxlearning.poseidon.core.io.FileUtil;
import com.voxlearning.poseidon.core.io.IOUtil;
import com.voxlearning.poseidon.core.io.resources.URLResource;
import com.voxlearning.poseidon.core.util.RegUtil;
import com.voxlearning.poseidon.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 装载setting配置文件
 *
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-12-08
 * @since 17-12-8
 */
public class SettingLoader {

    private Logger logger = LoggerFactory.getLogger(SettingLoader.class);
    /**
     * 注释标记
     */
    private static final String COMMENT_FLAG_PRE = "#";

    private static final String ASSIGN_FLAG = "=";

    private static final char[] GROUP_SURROUND = {'[', ']'};

    private String regVar = "\\$\\{(.*?)\\}";

    private Charset charset;

    private boolean isUseVariable;

    private BasicSetting setting;

    public SettingLoader(BasicSetting setting) {

    }

    public SettingLoader(BasicSetting setting, Charset charset, boolean isUseVariable) {
        this.setting = setting;
        this.charset = charset;
        this.isUseVariable = isUseVariable;
    }

    public boolean load(URLResource resource) {
        if (Objects.isNull(resource)) {
            throw new NullPointerException("resource can not ben null or empty");
        }
        logger.debug("load setting file [{}]", resource);
        InputStream inputStream;
        try {
            inputStream = resource.getInputStream();
            load(inputStream);
        } catch (IOException e) {
            logger.error("Load setting error.");
            return false;
        }
        return true;
    }


    /**
     * 从流中加载setting文件
     *
     * @param inputStream 输入流
     */
    public boolean load(InputStream inputStream) throws IOException {
        setting.clear();
        BufferedReader reader = null;
        try {
            reader = IOUtil.getReader(inputStream, charset);
            String group = null;
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                line = line.trim();
                if (StrUtil.isBlank(line) || line.startsWith(COMMENT_FLAG_PRE)) {
                    continue;
                }
                if (line.charAt(0) == GROUP_SURROUND[0] && line.charAt(line.length() - 1) == GROUP_SURROUND[1]) {
                    group = line.substring(1, line.length() - 1).trim();
                    this.setting.getGroup().add(group);
                    continue;
                }
                String[] keyValue = line.split(ASSIGN_FLAG);
                if (keyValue.length < 2) {
                    continue;
                }
                String key = keyValue[0].trim();
                if (StrUtil.isNotBlank(group)) {
                    key = group + StrUtil.DOT + key;
                }
                String value = keyValue[1].trim();
                if (isUseVariable) {
                    value = replaceVar(value);
                }
                this.setting.put(key, value);
            }
        } finally {
            IOUtil.close(reader);
        }
        return true;
    }


    /**
     * 持久化配置
     *
     * @param absolutePath 绝对路径
     */
    public void store(String absolutePath) {
        if (StrUtil.isBlank(absolutePath)) {
            throw new NullPointerException("absolutePath can not be null or empty");
        }
        Writer writer = null;
        try {
            writer = FileUtil.getWrite(absolutePath, charset, false);
            for (Map.Entry<Object, Object> entry : this.setting.entrySet()) {
                writer.write(StrUtil.format("%s %s %s", entry.getKey(), ASSIGN_FLAG, entry.getValue()));
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            IOUtil.close(writer);
        }
    }

    public String replaceVar(String value) {
        //0 表示获取整个正则表达式 比如${var}
        Set<String> vars = RegUtil.findAll(regVar, value, 0, new HashSet<>());
        for (String var : vars) {
            //提取出${var} 中var
            String s = RegUtil.get(regVar, var, 1);
            Object varValue = this.setting.get(s);
            if (Objects.nonNull(varValue) && varValue instanceof CharSequence) {
                value = value.replace(var, (CharSequence) varValue);
            }
        }
        return value;
    }
}
