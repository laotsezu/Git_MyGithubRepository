<?php 

	class ModelPartnerCustomer extends BaseModel{	
		function __construct($input){
			parent::__construct($input);
		}
		function setDataIndexes(){
			$this->data_indexes = array(
			"customer_id"
			,"customer_ten"
			,"customer_sdt"
			,"customer_nhom"
			,"customer_gioi_tinh"
			,"customer_ngay_sinh"
			,"customer_email"
			,"customer_dia_chi"
			,"customer_no_nan"
			,"customer_tong_tien_mua"
			,"customer_status"
			);
		}
		function getEnglishWord(){
			return "customer";
		}
		function getTableName(){
			return "khach_hang";
		}
		function increaseTotalBuy($amount){
			if($amount){
				$total_buy_index = $this->getEnglishWord()."_tong_tien_mua";
				$result = $this->increasePropertyValueToDb($total_buy_index,$amount);
				if(!$result){
					throw new Exception("Increase Total Buy : Increase Property Value To Db Failed!", 1);				
				}
			}
			else{
				throw new Exception("Increase Total Buy : Amount == false !", 1);
				
			}
		}
		function increaseOwe($amount){
			if($amount){
				$owe_index = $this->getEnglishWord()."_no_nan";
				$result =  $this->increasePropertyValueToDb($owe_index,$amount);
				if(!$result)
					throw new Exception("Increase Owe : Increase Property value to db failed! ", 1);
			}
			else{
				throw new Exception("Increase Owe : Amount == false !", 1);				
			}
		}
		function decreaseOwe($amount){
			if($amount){
				$owe_index = $this->getEnglishWord()."_no_nan";
				$result =  $this->decreasePropertyValueToDb($owe_index,$amount);
				if(!$result)
					throw new Exception("Decrease Owe : Increase Property value to db failed! ", 1);
			}
			else{
				throw new Exception("Decrease Owe : Amount == false !", 1);
				
			}
		}
	}

?>