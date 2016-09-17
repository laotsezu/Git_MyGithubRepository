<?php 
	class ModelRepositoryGoods extends BaseModel{
		static $TAG = "ModelRepositoryGoods: ";
		function __construct($input){
			parent::__construct($input);
		}
		function setDataIndexes(){
			$this->data_indexes = array(
			"goods_id"
			,"goods_ten"
			,"goods_nhom"
			,"goods_loai"
			,"goods_gia_ban"
			,"goods_gia_von"
			,"goods_so_luong"
			,"goods_don_vi"
			,"goods_status"
			);
		}
		function getEnglishName(){
			return "goods";
		}
		function getTableName(){
			return "hang_hoa";
		}
		function insertNewGoods(){
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
		function setAmount($amount){
			$amount_index = $this->getEnglishName()."_so_luong";
			if(!$amount){
				$amount = $this->getPropertyValue($amount_index);
			}
			if($amount){
				$result =  $this->setPropertyValueToDb($amount_index,$amount);
				if(!$result)
					throw new Exception(self::$TAG."Set Amount failed! ", 1);
			}
			else{
				throw new Exception(self::$TAG."Set Amount failed, Amount == 0 !", 1);
			}
		}
		function increaseAmount($amount){
			$amount_index = $this->getEnglishName()."_so_luong";
			if(!$amount){
				$amount = $this->getPropertyValue($amount_index);
			}
			if($amount){
				$result =  $this->increasePropertyValueToDb($amount_index,$amount);
				if(!$result)
					throw new Exception(self::$TAG."Increase Amount failed! ", 1);
			}
			else{
				throw new Exception(self::$TAG."Increase Amount failed, Amount == 0 !", 1);
			}
		}
		function decreaseAmount($amount){ 
			$this->increaseAmount($amount * -1);
		}
	}
?>