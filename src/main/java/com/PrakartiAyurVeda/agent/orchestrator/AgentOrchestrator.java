package com.PrakartiAyurVeda.agent.orchestrator;

import java.util.List;

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

    /**
     * Executes agents sequentially using shared AgentContext.
     */
    public AgentContext run(AgentContext context) {

        for (Agent agent : agents) {

            log.info("Executing agent: {}", agent.name());

            agent.execute(context);

            if (!context.isSafe()) {
                log.warn("Execution stopped. System marked unsafe by agent: {}", agent.name());
                break;
            }
        }

        context.setCompleted(true);
        return context;
    }
}
