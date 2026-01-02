package com.PrakartiAyurVeda.agent.orchestrator;

import java.util.List;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import com.PrakartiAyurVeda.agent.Agent;
import com.PrakartiAyurVeda.agent.context.AgentContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AgentOrchestrator {

    private final List<Agent> agents;

    public AgentContext run(AgentContext context) {

        // Add user info to logs (if available)
        if (context.getUser() != null) {
            MDC.put("user", context.getUser().getEmail());
        }

        try {
            for (Agent agent : agents) {

                long start = System.currentTimeMillis();
                log.info("[AGENT_START] {}", agent.name());

                agent.execute(context);

                long duration = System.currentTimeMillis() - start;
                log.info("[AGENT_END] {} took {} ms", agent.name(), duration);

                if (!context.isSafe()) {
                    log.warn("[AGENT_ABORTED] {} marked context unsafe", agent.name());
                    break;
                }
            }

            context.setCompleted(true);
            return context;

        } finally {
            MDC.remove("user");
        }
    }
}
