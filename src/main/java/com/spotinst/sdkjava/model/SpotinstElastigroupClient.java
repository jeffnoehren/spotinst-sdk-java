package com.spotinst.sdkjava.model;

import com.spotinst.sdkjava.exception.HttpError;
import com.spotinst.sdkjava.exception.SpotinstHttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by talzur on 11/01/2017.
 */
public class SpotinstElastigroupClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpotinstElastigroupClient.class);
    //region Members
    private String authToken;
    private String account;
    private ISpotinstElastigroupRepo spotinstElastigroupRepo;
    private ISpotinstElastigroupActiveInstanceRepo spotinstElastigroupActiveInstanceRepo;
    //endregion

    public ISpotinstElastigroupRepo getSpotinstElastigroupRepo(){
        return this.spotinstElastigroupRepo;
    }

    public void setSpotinstElastigroupRepo(){
        this.spotinstElastigroupRepo = SpotinstRepoManager.getInstance().getSpotinstElastigroupRepo();
    }


    public ISpotinstElastigroupActiveInstanceRepo getSpotinstElastigroupActiveInstanceRepo(){
        return this.spotinstElastigroupActiveInstanceRepo;
    }

    public void setSpotinstElastigroupActiveInstanceRepo(){
        this.spotinstElastigroupActiveInstanceRepo = SpotinstRepoManager.getInstance().getSpotinstElastigroupActiveInstanceRepo();
    }

    //region Constructor

    public SpotinstElastigroupClient(String authToken, String account) {
        this.authToken = authToken;
        this.account = account;

        setSpotinstElastigroupRepo();
        setSpotinstElastigroupActiveInstanceRepo();
    }

    //endregion

    //region Methods
    public Elastigroup createElastigroup(ElastigroupCreationRequest elastigroupCreationRequest) {
        
        Elastigroup retVal = null;

        Elastigroup elastigroupToCreate = elastigroupCreationRequest.getElastigroup();

        RepoGenericResponse<Elastigroup> creationResponse = getSpotinstElastigroupRepo().create(elastigroupToCreate, authToken, account);
        if (creationResponse.isRequestSucceed()) {
            retVal = creationResponse.getValue();
        } else {
            List<HttpError> httpExceptions = creationResponse.getHttpExceptions();
            HttpError httpException = httpExceptions.get(0);
            LOGGER.error(String.format("Error encountered while attempting to create elastigroup. Code: %s. Message: %s.", httpException.getCode(), httpException.getMessage()));
            throw new SpotinstHttpException(httpException.getMessage());
        }
        return retVal;
    }

    public Boolean updateElastigroup(ElastigroupUpdateRequest elastigroupUpdateRequest, String elastigroupId) {

        Boolean retVal = null;

        Elastigroup elastigroupToUpdate = elastigroupUpdateRequest.getElastigroup();
        RepoGenericResponse<Boolean> updateResponse = getSpotinstElastigroupRepo().update(elastigroupId, elastigroupToUpdate, authToken,account);
        if (updateResponse.isRequestSucceed()) {
            retVal = updateResponse.getValue();
        } else {
            List<HttpError> httpExceptions = updateResponse.getHttpExceptions();
            HttpError httpException = httpExceptions.get(0);
            LOGGER.error(String.format("Error encountered while attempting to update elastigroup. Code: %s. Message: %s.", httpException.getCode(), httpException.getMessage()));
            throw new SpotinstHttpException(httpException.getMessage());
        }
        return retVal;
    }

    public Boolean deleteElastigroup(ElastigroupDeletionRequest elastigroupDeletionRequest) {

        Boolean retVal = null;
        String elastigroupToDeleteId = elastigroupDeletionRequest.getElastigroupId();
        RepoGenericResponse<Boolean> elastigroupDeletionResponse = getSpotinstElastigroupRepo().delete(elastigroupToDeleteId, authToken, account);
        if (elastigroupDeletionResponse.isRequestSucceed()) {
            retVal = elastigroupDeletionResponse.getValue();
        } else {
            List<HttpError> httpExceptions = elastigroupDeletionResponse.getHttpExceptions();
            HttpError httpException = httpExceptions.get(0);
            LOGGER.error(String.format("Error encountered while attempting to delete elastigroup. Code: %s. Message: %s.", httpException.getCode(), httpException.getMessage()));
            throw new SpotinstHttpException(httpException.getMessage());
        }

        return retVal;
    }

    public Boolean detachInstances(ElastigroupDetachInstancesRequest detachRequest, String elastigroupId) {

        Boolean retVal = null;

        if (detachRequest.getInstancesToDetach().size() > 0) {
            RepoGenericResponse<Boolean> detachResponse = getSpotinstElastigroupRepo().detachInstances(elastigroupId, detachRequest, authToken, account);
            if (detachResponse.isRequestSucceed()) {
                retVal = detachResponse.getValue();
            } else {
                List<HttpError> httpExceptions = detachResponse.getHttpExceptions();
                HttpError httpException = httpExceptions.get(0);
                LOGGER.error(String.format("Error encountered while attempting to detach instances. Code: %s. Message: %s.", httpException.getCode(), httpException.getMessage()));
                throw new SpotinstHttpException(httpException.getMessage());
            }
        } else {
            LOGGER.error("No instances to detach.");
        }


        return retVal;
    }

    public List<ElastigroupActiveInstance> getActiveInstances(ElastigroupGetActiveInstancesRequest elastigroupGetActiveInstancesRequest) {

        List<ElastigroupActiveInstance> retVal = null;

        String elastigroupId = elastigroupGetActiveInstancesRequest.getElastigroupId();

        ActiveInstanceFilter filter = new ActiveInstanceFilter();
        filter.setElastigroupId(elastigroupId);
        RepoGenericResponse<List<ElastigroupActiveInstance>> instancesResponse = getSpotinstElastigroupActiveInstanceRepo().getAll(filter, authToken,account);
        if (instancesResponse.isRequestSucceed()) {
            retVal = instancesResponse.getValue();
        } else {
            List<HttpError> httpExceptions = instancesResponse.getHttpExceptions();
            HttpError httpException = httpExceptions.get(0);
            LOGGER.error(String.format("Error encountered while attempting to get active instances. Code: %s. Message: %s.", httpException.getCode(), httpException.getMessage()));
            throw new SpotinstHttpException(httpException.getMessage());
        }

        return retVal;
    }

    public Elastigroup getElastigroup(ElastigroupGetRequest elastigroupGetRequest) {

        Elastigroup retVal = null;

        String elastigroupId = elastigroupGetRequest.getElastigroupId();

        RepoGenericResponse<Elastigroup> elastigroupRepoGenericResponse = getSpotinstElastigroupRepo().get(elastigroupId, authToken,account);
        if (elastigroupRepoGenericResponse.isRequestSucceed()) {
            retVal = elastigroupRepoGenericResponse.getValue();
        } else {
            List<HttpError> httpExceptions = elastigroupRepoGenericResponse.getHttpExceptions();
            HttpError httpException = httpExceptions.get(0);
            LOGGER.error(String.format("Error encountered while attempting to get elastigroup. Code: %s. Message: %s.", httpException.getCode(), httpException.getMessage()));
            throw new SpotinstHttpException(httpException.getMessage());
        }

        return retVal;
    }

    /**
     * This function is used to scale up an Elastigroup by takingin an ElastigroupScalingRequest that is sent by the user and sending it to
     * ISpotinstElastigroupRepo.scaleUp().
     *
     * @param elastigroupScalingRequest ElastigroupScalingRequest object that can be converted to a JSON to send as a request
     * @return ElastigroupScalingResponse Object that is returned from the scale down request from ISpotinstElastigroupRepo.scaleUp()
     */
    public ElastigroupScalingResponse scaleGroupUp(ElastigroupScalingRequest elastigroupScalingRequest) {

        ElastigroupScalingResponse retVal = null;

        String elastigroupId = elastigroupScalingRequest.getElastigroupId();

        ActiveInstanceFilter filter = new ActiveInstanceFilter();
        filter.setElastigroupId(elastigroupId);
        RepoGenericResponse<ElastigroupScalingResponse> elastigroupScalingResponse = getSpotinstElastigroupRepo().scaleUp(elastigroupScalingRequest, authToken, account);

        if (elastigroupScalingResponse.isRequestSucceed()) {
            retVal = elastigroupScalingResponse.getValue();
        } else {
            List<HttpError> httpExceptions = elastigroupScalingResponse.getHttpExceptions();
            HttpError httpException = httpExceptions.get(0);
            LOGGER.error(String.format("Error encountered while attempting to scale group up. Code: %s. Message: %s.", httpException.getCode(), httpException.getMessage()));
            throw new SpotinstHttpException(httpException.getMessage());
        }

        return retVal;
    }


    /**
     * This function is used to scale down an Elastigroup by takingin an ElastigroupScalingRequest that is sent by the user and sending it to
     * ISpotinstElastigroupRepo.scaleDown(). 
     *
     * @param elastigroupScalingRequest ElastigroupScalingRequest object that can be converted to a JSON to send as a request
     * @return ElastigroupScalingResponse Object that is returned from the scale down request from ISpotinstElastigroupRepo.scaleDown()
     */
    public ElastigroupScalingResponse scaleGroupDown(ElastigroupScalingRequest elastigroupScalingRequest) {
        ElastigroupScalingResponse retVal = null;

        String elastigroupId = elastigroupScalingRequest.getElastigroupId();

        ActiveInstanceFilter filter = new ActiveInstanceFilter();
        filter.setElastigroupId(elastigroupId);
        RepoGenericResponse<ElastigroupScalingResponse> elastigroupScalingResponse = getSpotinstElastigroupRepo().scaleDown(elastigroupScalingRequest, authToken, account);

        if (elastigroupScalingResponse.isRequestSucceed()) {
            retVal = elastigroupScalingResponse.getValue();
        } else {
            List<HttpError> httpExceptions = elastigroupScalingResponse.getHttpExceptions();
            HttpError httpException = httpExceptions.get(0);
            LOGGER.error(String.format("Error encountered while attempting to scale group down. Code: %s. Message: %s.", httpException.getCode(), httpException.getMessage()));
            throw new SpotinstHttpException(httpException.getMessage());
        }

        return retVal;
    }


    //endregion
}
