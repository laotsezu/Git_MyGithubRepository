<?php 
	class ModelPersonnelPersonnel extends BaseModel{
		function __construct($input){
			parent::__construct($input);
		}
		function setDataIndexes(){
			$this->data_indexes = array(
			"personnel_id"
			,"personnel_ten"
			,"personnel_loai"
			,"personnel_sdt"
			,"personnel_dia_chi"
			,"personnel_status"
			,"personnel_tong_tien_ban"
			);
		}
		function getEnglishWord(){
			return "personnel";
		}
		function getTableName(){
			return "nguoi_lam";
		}
		function increaseTotalSell($amount){
			$total_sell_index = $this->getEnglishWord()."_tong_tien_ban";
			
			$result = $this->increasePropertyValueToDb($total_sell_index,$amount);
			
			if(!$result){
				throw new Exception("Increase Total Sell : Increase Property Value To Db failed! ", 1);			
			}
		}
	}



?>