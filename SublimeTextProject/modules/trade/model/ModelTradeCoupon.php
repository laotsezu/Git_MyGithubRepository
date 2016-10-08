<?php 
	class ModelTradeCounpon extends BaseModel{
		function __construct($input){
			parent::__construct($input);
		}
		function setDataIndexes(){
			$this->data_indexes = array(
			"coupon_id"
			,"coupon_thoi_gian"
			,"agency_id"
			,"personnel_id"
			,"goods_old_ids"
			,"goods_ids"
			,"counpon_ghi_chu"
			,"coupon_status"
			);
		}
		function getEnglishWord(){
			return "coupon";
		}
		function getTableName(){
			return "phieu_kiem_hang";
		}
		
	}

?>