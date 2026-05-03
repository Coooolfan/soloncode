package org.noear.solon.codecli.config;

/**
 *
 * @author noear 2026/4/4 created
 *
 */
public class AgentFlags {
    public final static String FLAG_RUN = "run";
    public final static String FLAG_SERVE = "serve";
    public final static String FLAG_ACP = "acp";
    public final static String FLAG_WEB = "web";

    public static String getVersion() {
        return "0.1.3";
    }

    public static String getLastVersion() {
        // This fork disables upstream update checks.
        return null;
    }


    public static boolean checkUpdate() {
        // This fork does not report available updates.
        return false;
    }
}
