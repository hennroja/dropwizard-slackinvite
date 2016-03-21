package de.hennroja.slackinvite;

import de.hennroja.slackinvite.core.JoinRequest;
import de.hennroja.slackinvite.core.JoinRequestDOA;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import de.hennroja.slackinvite.health.ValidAPITokenHealthCheck;
import de.hennroja.slackinvite.resources.SlackInviteMainResource;

import java.security.SecureRandom;

/**
 * Created by hennroja on 20/03/16.
 */
public class SlackInviteApplication extends Application<SlackInviteConfiguration>
{
	public static void main(String[] args) throws Exception
	{
		new SlackInviteApplication().run(args);
	}

    private final HibernateBundle<SlackInviteConfiguration> hibernateBundle =
            new HibernateBundle<SlackInviteConfiguration>(JoinRequest.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(SlackInviteConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

	@Override
	public void initialize(Bootstrap<SlackInviteConfiguration> bootstrap)
	{
        bootstrap.addBundle(new MigrationsBundle<SlackInviteConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(SlackInviteConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
		bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html"));
        bootstrap.addBundle(hibernateBundle);

	}

	@Override
	public void run(SlackInviteConfiguration configuration, Environment environment)
	{
		environment.jersey().setUrlPattern("/api/*");
        JoinRequestDOA joinRequest = new JoinRequestDOA(hibernateBundle.getSessionFactory());
        environment.jersey().register(joinRequest);

        SlackAPI slackAPI = new SlackAPI(configuration.getSlackapikey());

		final SlackInviteMainResource resource = new SlackInviteMainResource(joinRequest, slackAPI, new SecureRandom());
		final ValidAPITokenHealthCheck healthCheck = new ValidAPITokenHealthCheck(slackAPI);
		environment.healthChecks().register("slacktoken", healthCheck);
		environment.jersey().register(resource);


	}

}