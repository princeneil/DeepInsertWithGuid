package  my.company.handlers.relationservice; 

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sap.cloud.sdk.service.prov.api.DataSourceHandler;
import com.sap.cloud.sdk.service.prov.api.EntityData;
import com.sap.cloud.sdk.service.prov.api.EntityMetadata;
import com.sap.cloud.sdk.service.prov.api.ExtensionHelper;
import com.sap.cloud.sdk.service.prov.api.exception.DatasourceException;
import com.sap.cloud.sdk.service.prov.api.operations.Create;
import com.sap.cloud.sdk.service.prov.api.request.CreateRequest;
import com.sap.cloud.sdk.service.prov.api.response.CreateResponse;

/***
 * Handler class for entity "CompanyEntity" of service "RelationService".
 * This handler registers custom handlers for the entity OData operations.
 * For more information, see: https://help.sap.com/viewer/65de2977205c403bbc107264b8eccf4b/Cloud/en-US/6fe3070250ea45b88c35cda209e8324b.html
 */
public class CompanyEntityCustomHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Create(entity = "CompanyEntity", serviceName = "RelationService")
	public CreateResponse createCompany(CreateRequest createRequest, ExtensionHelper extensionHelper) throws DatasourceException{
		// 1) retrieve the request payload, the data to create in backend
	    Map<String, Object> mapForCreation = createRequest.getData().asMap();
	    
	    // special handling required in case of UUID: FWK cannot generate it for inline entity
        Map<String, Object> inlineContactMap = (Map<String, Object>)mapForCreation.get("linkToContact");
        // check if request is deep insert
        if(inlineContactMap != null) {
    		// manually generate Guid for inline-entity-key-field (Contacts) and foreign-key-field (Companies)
    		UUID contactGuid = UUID.randomUUID();  
        	inlineContactMap.put("contactId", contactGuid); // fill the key field of inline entity (Contacts)
        	mapForCreation.put("linkToContact_contactId", contactGuid);// fill the forein-key field of "Companies" entity                	
        }
        		
        // 2) our actual task is: specify key field for navigation entity
        //Compose the map of key list for all entities of the deep insert
        Map<String, List<String>> keyMap = new HashMap<String, List<String>>();
        // the key map for the parent entity: Companies. Here, the key field is "companyId"       
        keyMap.put("CompanyEntity", Collections.singletonList("companyId"));        
        // here we assign the key field (contactId) of navigation target entity (Contacts) to the navigationProperty name (contact) 
        keyMap.put("linkToContact", Collections.singletonList("contactId"));                
        
        // 3) send data to database, including the info about keys 
        EntityData entityDataToCreate = EntityData.createFromDeepMap(mapForCreation, keyMap, "RelationService.CompanyEntity");              
         // execute it in database
        EntityData result = extensionHelper.getHandler().executeInsertWithAssociations(entityDataToCreate, true);// true to return created entity
        return CreateResponse.setSuccess().setData(result).response();
	}	

//	@Query(entity = "CompanyEntity", serviceName = "RelationService")
//	public QueryResponse queryCompanyEntity(QueryRequest req) {
//      //TODO: add your custom logic...
//
//      //List<Object> resultItems = new ArrayList<Object>();
//      //return QueryResponse.setSuccess().setData(resultItems).response(); //use this API to return items.
//      ErrorResponse errorResponse = ErrorResponse.getBuilder()
//        					.setMessage("Unimplemented Query Operation")
//        					.setStatusCode(500)
//        					.response();
//      return QueryResponse.setError(errorResponse);
//	}

//	@Read(entity = "CompanyEntity", serviceName = "RelationService")
//	public ReadResponse readCompanyEntity(ReadRequest req) {
//      //TODO: add your custom logic...
//
//      //Object data = new Object();
//      //return ReadResponse.setSuccess().setData(data).response(); //use this API to return an item.
//      ErrorResponse errorResponse = ErrorResponse.getBuilder()
//        					.setMessage("Unimplemented Read Operation")
//        					.setStatusCode(500)
//        					.response();
//      return ReadResponse.setError(errorResponse);
//	}

//	@Update(entity = "CompanyEntity", serviceName = "RelationService")
//	public UpdateResponse updateCompanyEntity(UpdateRequest req) {
//      //TODO: add your custom logic...
//
//      //return UpdateResponse.setSuccess().response(); //use this API if the item is successfully modified.
//      ErrorResponse errorResponse = ErrorResponse.getBuilder()
//        					.setMessage("Unimplemented Update Operation")
//        					.setStatusCode(500)
//        					.response();
//      return UpdateResponse.setError(errorResponse);
//	}

//	@Create(entity = "CompanyEntity", serviceName = "RelationService")
//	public CreateResponse createCompanyEntity(CreateRequest req) {
//      //TODO: add your custom logic...
//
//      //return CreateResponse.setSuccess().response(); //use this API if the item is successfully created.
//      ErrorResponse errorResponse = ErrorResponse.getBuilder()
//        					.setMessage("Unimplemented Create Operation")
//        					.setStatusCode(500)
//        					.response();
//      return CreateResponse.setError(errorResponse);
//	}

//	@Delete(entity = "CompanyEntity", serviceName = "RelationService")
//	public DeleteResponse deleteCompanyEntity(DeleteRequest req) {
//      //TODO: add your custom logic...
//
//      //return DeleteResponse.setSuccess().response(); //use this API if the item is successfully deleted.
//      ErrorResponse errorResponse = ErrorResponse.getBuilder()
//        					.setMessage("Unimplemented Delete Operation")
//        					.setStatusCode(500)
//        					.response();
//      return DeleteResponse.setError(errorResponse);
//	}

}