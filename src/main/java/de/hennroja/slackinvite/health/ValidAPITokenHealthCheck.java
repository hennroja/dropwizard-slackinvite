package de.hennroja.slackinvite.health;

import com.codahale.metrics.health.HealthCheck;
import de.hennroja.slackinvite.SlackAPI;

/**
 * Created by hennroja on 20/03/16.
 */
public class ValidAPITokenHealthCheck extends HealthCheck {

    private final SlackAPI slackAPI;

    public ValidAPITokenHealthCheck(SlackAPI slackAPI) {
        this.slackAPI = slackAPI;
    }

    @Override
    protected Result check() throws Exception {
        if (!slackAPI.testAuth()) {
            return Result.unhealthy("slack api credentials not working");
        }
        return Result.healthy();
    }
}