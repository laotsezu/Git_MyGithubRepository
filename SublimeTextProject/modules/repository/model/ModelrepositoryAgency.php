<?php 
	class ModelRepositoryAgency extends BaseModel{		
		function __construct($input){
			parent::__construct($input);
		}
		function setDataIndexes(){
			$this->data_indexes = array(
			"agency_id"
			,"agency_ten"
			,"agency_dia_chi"
			,"agency_sdt"
			,"agency_status"
			);
		}
		function getEnglishWord(){
			return "agency";
		}
		function getTableName(){
			return "chi_nhanh";
		}
	}


?>