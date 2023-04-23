package com.spring.cloud.base.monitor.failure;

import com.spring.cloud.base.monitor.exception.TelnetException;
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/21 15:50
 */
public class TelnetStopFailureAnalyzer extends AbstractFailureAnalyzer<TelnetException> {
    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, TelnetException cause) {
        return new FailureAnalysis("telnet check failed!!!", cause.getMessage(), cause);
    }
}
