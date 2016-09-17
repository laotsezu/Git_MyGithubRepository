<?php 

	class ModelPartnerCustomer extends BaseModel{	
		static $TAG = "ModelPartnerCustomer: ";
		function __construct($input){
			parent::__construct($input);
		}
		function setDataIndexes(){
			$this->data_indexes = array(
			"customer_id"
			,"customer_ten"
			,"customer_sdt"
			,"customer_nhom"
			,"customer_loai"
			,"customer_gioi_tinh"
			,"customer_ngay_sinh"
			,"customer_email"
			,"customer_dia_chi"
			,"customer_no_nan"
			,"customer_tong_tien_mua"
			,"customer_ma_so_thue"
			,"customer_status"
			);
		}
		function getEnglishName(){
			return "customer";
		}
		function getTableName(){
			return "khach_hang";
		}
		function addCustomer(){
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