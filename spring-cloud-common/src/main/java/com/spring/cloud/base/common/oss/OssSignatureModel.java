package com.spring.cloud.base.common.oss;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: ls
 * @Date: 2023/1/28
 * @Description:
 **/
@Getter
@Setter
public class OssSignatureModel {

    private String accessId;

    private String policy;

    private String signature;

    private String filename;

    private String expire;

    private String cdnHost;

    private String dir;

    private String bucket;

    private String endpoint;

    private String fileCdnPath;
}
