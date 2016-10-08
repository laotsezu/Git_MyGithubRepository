<?php 
	class ModelPartnerProvider extends BaseModel{
		function __construct($input){
			parent::__construct($input);
		}
		function setDataIndexes(){
			$this->data_indexes = array(
			"provider_id"
			,"provider_ten"
			,"provider_sdt"
			,"provider_email"
			,"provider_dia_chi"
			,"provider_cong_ty"
			,"provider_no_nan"
			,"provider_status"
			);
		}
		function getEnglishWord(){
			return "provider";
		}
		function getTableName(){
			return "nha_cung_cap";
		}
	}
	



?>