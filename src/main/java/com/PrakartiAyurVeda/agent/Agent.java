package com.PrakartiAyurVeda.agent;

import com.PrakartiAyurVeda.agent.context.AgentContext;

public interface Agent {

    /**
     * Execute agent logic and update the shared context.
     */
    void execute(AgentContext context);

    /**
     * Agent name for logging & debugging.
     */
    default String name() {
        return this.getClass().getSimpleName();
    }
}
