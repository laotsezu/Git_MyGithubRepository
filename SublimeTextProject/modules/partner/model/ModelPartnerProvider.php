<?php 
	class ModelPartnerProvider extends BaseModel{
		static $TAG = "ModelPartnerProvider: ";
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
			,"provider_tong_tien_mua"
			,"provider_ma_so_thue"
			,"provider_status"
			);
		}
		function getEnglishName(){
			return "provider";
		}
		function getTableName(){
			return "nha_cung_cap";
		}
		function addProvider(){
			try{
				$this->insert();
				$response["status"] = true;
			}
			catch(Exception $e){
				$response["status"] = false;
				$response["message"] = $e->getMessage();
			}
			return $response;
		}
		function increaseTotalBuy($amount){
			if($amount){
				$total_buy_index = $this->getEnglishName()."_tong_tien_mua";
				$result = $this->increasePropertyValueToDb($total_buy_index,$amount);
				if(!$result){
					throw new Exception(self::$TAG."Increase total buy failed!", 1);				
				}
			}
			else{
				throw new Exception(self::$TAG."Increase total buy failed!,Amount == 0 !", 1);
				
			}
		}
		function increaseOwe($amount){
			if($amount){
				$owe_index = $this->getEnglishName()."_no_nan";
				$result =  $this->increasePropertyValueToDb($owe_index,$amount);
				if(!$result)
					throw new Exception(self::$TAG."Increase Owe Failed! ", 1);
			}
		}
		function decreaseOwe($amount){
			if($amount){
				$owe_index = $this->getEnglishName()."_no_nan";
				$result =  $this->decreasePropertyValueToDb($owe_index,$amount);
				if(!$result)
					throw new Exception(self::$TAG."Decrease Owe failed! ", 1);
			}
			else{
				throw new Exception(self::$TAG."Decrease Owe Failed, Amount == 0 !", 1);
				
			}
		}
	}
	



?>