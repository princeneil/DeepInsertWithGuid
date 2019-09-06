namespace com.relation;

entity CompanyEntity {
   key companyId : UUID;
   companyName  : String;
   linkToContact  : Association to ContactEntity;
}

entity ContactEntity {
   key contactId : UUID;
   contactName : String;
   contactPhone : Integer;	
}