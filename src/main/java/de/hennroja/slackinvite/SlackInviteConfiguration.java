package de.hennroja.slackinvite;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by hennroja on 20/03/16.
 */
public class SlackInviteConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty
    private DataSourceFactory database = new DataSourceFactory();

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.database = dataSourceFactory;
    }

    @Valid
    @NotNull
    @JsonProperty
    private String slackapikey;

    @JsonProperty("slackapikey")
    public String getSlackapikey() { return slackapikey; };

    @JsonProperty("slackapikey")
    public void setSlackapikey(String slackapikey) {
        this.slackapikey = slackapikey;
    }
}