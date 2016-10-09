<?php 
	class ModelRepositoryGoodsAndICoupon extends BaseModel{
		static $TAG = "ModelRepositoryGoodsAndICoupon: ";
		function __construct($input){
			if((isset($input["gaic_current_so_luong"])) && !(isset($input["gaic_origin_so_luong"]))){
				$input["gaic_origin_so_luong"] = $input["gaic_current_so_luong"];
			}
			parent::__construct($input);
		}
		function setDataIndexes(){
			$this->data_indexes = array(
			"gaic_icoupon_id"
			,"gaic_goods_id"
			,"gaic_origin_so_luong"
			,"gaic_current_so_luong"
			,"gaic_goods_gia_ban"
			,"gaic_tong_tien_ban"
			,"gaic_thoi_gian"
			,"gaic_status"
			);
		}
		function getEnglishName(){
			return "gaic";
		}
		function getTableName(){
			return "hang_hoa_va_hoa_don";
		}
		private function setWhere(){
			$this->sql_model->where("gaic_bill_id",$this->getPropertyValue("gaic_bill_id"));
			$this->sql_model->where("gaic_goods_id",$this->getPropertyValue("gaic_goods_id"));
		}
		private function update($data){
			$this->setWhere();
			return $this->sql_model->update($data);
		}
		function getAmountFromDb(){
			$this->setWhere();

			$result = $this->getOne("*");

			if($result){
				return $result["gaic_current_so_luong"];
			}
			else{
				throw new Exception(self::$TAG."Get Amount Failed, No record!", 1);
			}
		}
		function setAmountToDb($amount){
			if($amount){
				$this->setWhere();


				$result = $this->update(array("gaic_current_so_luong" => $amount));

				if(!$result){
					throw new Exception(self::$TAG."Set Amount Failed !", 1);		
				}
			}
			else{
				throw new Exception(self::$TAG."Set Amount Failed, Amount == 0", 1);		
			}
		}
		function increaseAmount($amount){
			if($amount){
				$current_amount = $this->getAmountFromDb();
				$new_amount = $current_amount + $amount;
				if($new_amount < 0){
					throw new Exception(self::$TAG."Increase Amount Failed, Current amount - amount < 0!", 1);
				}
				else{
					$result = $this->setAmountToDb($new_amount);
					if(!$result){
						throw new Exception(self::$TAG."Increase Amount Failed!", 1);
					}
				}
			}
			else{
				throw new Exception(self::$TAG."Increase Amount failed, amount == 0!", 1);
			}
		}
		function decreaseAmount($amount){
			$this->increaseAmount($amount * -1);
		}
		function add(){
			$gia_ban = $this->getPropertyValue($this->getEnglishName()."_gia_ban"));
			$so_luong = $this->getPropertyValue($this->getEnglishName()."_current_so_luong");

			$this->setPropertyValue($this->getEnglishName()."_tong_tien_ban") = $gia_ban * $so_luong;
			
			$this->insert();
		}
	}
?>