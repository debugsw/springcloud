package com.spring.cloud.base.common.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

/**
 * @Author: ls
 * @Date: 2023/1/28
 * @Description:
 **/
public class OssTemplate {

    private OssProperties ossProperties;

    public OssTemplate() {

    }

    public OssTemplate(OssProperties ossProperties) {
        this.ossProperties = ossProperties;
    }

    /**
     * 获取签名
     *
     * @return 签名模型
     */
    public OssSignatureModel getSignature(String fileSuffix) {

        OSSClient client = new OSSClient(ossProperties.getEndpoint(),
                new DefaultCredentialProvider(ossProperties.getAccessId(), ossProperties.getAccessKey()), null);

        long expireEndTime = System.currentTimeMillis() + ossProperties.getExpireTime() * 1000;
        Date expiration = new Date(expireEndTime);

        PolicyConditions policyConditions = new PolicyConditions();
        policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
        String dir = this.getDateDir(ossProperties.getDir());
        policyConditions.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

        String postPolicy = client.generatePostPolicy(expiration, policyConditions);
        byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);

        String encodedPolicy = BinaryUtil.toBase64String(binaryData);
        String postSignature = client.calculatePostSignature(postPolicy);

        String filename = UUID.randomUUID().toString().replace("-", "");
        if (StringUtils.hasText(fileSuffix)) {
            filename = fileSuffix.startsWith(".") ? filename + fileSuffix : filename + "." + fileSuffix;
        }

        OssSignatureModel ossSignatureModel = new OssSignatureModel();
        ossSignatureModel.setAccessId(ossProperties.getAccessId());
        ossSignatureModel.setPolicy(encodedPolicy);
        ossSignatureModel.setSignature(postSignature);
        ossSignatureModel.setFilename(filename);
        ossSignatureModel.setExpire(String.valueOf(expireEndTime));
        ossSignatureModel.setCdnHost(ossProperties.getCdnHost());
        ossSignatureModel.setBucket(ossProperties.getBucket());
        ossSignatureModel.setEndpoint(ossProperties.getEndpoint());
        ossSignatureModel.setDir(dir);
        ossSignatureModel.setFileCdnPath(this.getPath(ossSignatureModel.getCdnHost(), ossSignatureModel.getDir(), ossSignatureModel.getFilename()));

        return ossSignatureModel;
    }

    /**
     * 获取安全路径，日期切分
     *
     * @param dir 原始路径
     * @return 安全路径
     */
    private String getDateDir(String dir) {

        LocalDateTime localDateTime = LocalDateTime.now();
        String afterAddr = "/" + localDateTime.getYear() + "/" + localDateTime.getMonth().getValue() + "/" + localDateTime.getDayOfMonth();
        String result = StringUtils.hasText(dir) ? dir + afterAddr : afterAddr;
        result = result.replace("\\", "/").replace("//", "/");
        result = result.startsWith("/") ? result.substring(1) : result;
        result = result.endsWith("/") ? result.substring(0, result.length() - 1) : result;

        return result;
    }

    private String getPath(String cdn, String dir, String filename) {
        return cdn + "/" + dir + "/" + filename;
    }
}
