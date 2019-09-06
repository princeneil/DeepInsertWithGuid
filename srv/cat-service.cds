using com.relation from '../db/data-model';

service RelationService {
   entity CompanyEntity as projection on relation.CompanyEntity;
   entity ContactEntity as projection on relation.ContactEntity;    
}