<?php 
	class ModelRepositoryIcoupon extends BaseModel{ 
		function __construct($input){
			parent::__construct($input);
		}
		function setDataIndexes(){
			$this->data_indexes = array(
			"icoupon_id"
			,"icoupon_thoi_gian"
			,"provider_id"
			,"agency_id"
			,"personnel_id"
			,"icoupon_tong_tien_hang"
			,"icoupon_giam_gia"
			,"icoupon_tien_phai_tra"
			,"icoupon_ghi_chu"
			,"icoupon_status"
			);
		}
		function getEnglishWord(){
			return "icoupon";
		}
		function getTableName(){
			return "phieu_nhap_hang";
		}
	}
	
?>