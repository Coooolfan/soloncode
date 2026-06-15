package org.noear.solon.codecli.config;

import lombok.Getter;

import org.noear.solon.ai.harness.HarnessExtension;
import org.noear.solon.codecli.config.entity.ApiSourceDo;
import org.noear.solon.codecli.config.entity.LspServerDo;
import org.noear.solon.codecli.config.entity.McpServerDo;
import org.noear.solon.codecli.config.entity.ModelDo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 代理属性（相关配置从 config.yml, AgentProperties - 慢慢过度到 settings.json, AgentSettings）
 *
 * @author noear
 * @since 3.9.1
 */
@Getter
public class AgentProperties implements Serializable {
    /**
     * @deprecated 2026.4.10 {@link #getModels()}
     */
    @Deprecated
    private ModelDo chatModel;

    //主代理工具权限
    private List<String> tools = new ArrayList<>();

    // 禁用工具（全局）
    private List<String> disallowedTools = new ArrayList<>();

    //最大步数
    @Deprecated
    private Integer maxSteps;
    private Integer maxTurns;

    //自我反思
    private boolean autoRethink = true;

    private int sessionWindowSize = 8;

    private int summaryWindowSize = 40;
    private int summaryWindowToken = 60_000;
    private String summaryModel;

    private boolean memoryIsolation = true;
    private boolean memoryEnabled = true;

    private boolean sandboxMode = true;
    private boolean sandboxAllowUserHome = true;
    private boolean sandboxSystemRestrict = true;

    private boolean checkUpdate = true;
    private String webEndpoint = "/cli";

    private boolean hitlEnabled = false;
    private boolean subagentEnabled = true;
    private boolean bashAsyncEnabled = false;

    private boolean mcpEnabled = true;
    private boolean openApiEnabled = true;
    private boolean lspEnabled = true;

    private String userAgent = "Mozilla/5.0 AppleWebKit/537.36 (KHTML, like Gecko; compatible; SolonCode/1.0 like claude-code; +https://solon.noear.org/)";
    private String defaultModel;

    private int apiRetries = 3;
    private int mcpRetries = 3;
    private int modelRetries = 3;

    private List<HarnessExtension> extensions = new ArrayList<>();

    private List<ModelDo> models = new ArrayList<>();

    /**
     * @deprecated 4.0.0
     */
    @Deprecated
    private Map<String, String> skillPools = new LinkedHashMap<>();

    /**
     * @deprecated 4.0.0
     */
    @Deprecated
    private List<String> agentPools = new ArrayList<>();

    private Map<String, McpServerDo> mcpServers = new LinkedHashMap<>();
    private Map<String, ApiSourceDo> apiServers = new LinkedHashMap<>();
    private Map<String, LspServerDo> lspServers = new LinkedHashMap<>();

    private boolean thinkPrinted = false;
    private boolean cliPrintSimplified = true;

    public boolean isAutoRethink() {
        return autoRethink;
    }

    public Integer getMaxTurns() {
        if (maxTurns == null) {
            return maxSteps;
        } else {
            return maxTurns;
        }
    }
}
