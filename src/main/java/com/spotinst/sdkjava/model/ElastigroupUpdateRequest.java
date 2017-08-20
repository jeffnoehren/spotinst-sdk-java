package com.spotinst.sdkjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spotinst.sdkjava.client.rest.JsonMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aharontwizer on 8/24/15.
 */

public class ElastigroupUpdateRequest {
    //region Members
    @JsonProperty("group")
    private Elastigroup elastigroup;
    //endregion

    //region Private Constructor

    private ElastigroupUpdateRequest() {
    }

    //endregion

    //region Getters & Setters

    public Elastigroup getElastigroup() {
        return elastigroup;
    }

    public void setElastigroup(Elastigroup elastigroup) {
        this.elastigroup = elastigroup;
    }

    //endregion
    public static class Builder {
        private ElastigroupUpdateRequest elastigroupCreationRequest;

        private Builder() {
            this.elastigroupCreationRequest = new ElastigroupUpdateRequest();
        }

        public static Builder get() {
            Builder builder = new Builder();
            return builder;
        }

        public Builder setElastigroup(final Elastigroup elastigroup) {
            elastigroupCreationRequest.setElastigroup(elastigroup);
            return this;
        }

        public ElastigroupUpdateRequest build() {
            // TODO : Validations
            return elastigroupCreationRequest;
        }

    }
    //region Json methods
    public String toJson() {
        ApiElastigroup apiElastigroupToCreate = ElastigroupConverter.toDal(elastigroup);

        Map<String, ApiElastigroup> groupRequest = new HashMap<>();
        groupRequest.put("group", apiElastigroupToCreate);
        String elastigroupJson = JsonMapper.toJson(groupRequest);

        return elastigroupJson;
    }
    //endregion

}