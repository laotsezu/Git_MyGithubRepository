<?php 
	class ModelTradeGoodsAndBill extends BaseModel{
		static $TAG = "ModelTradeGoodsAndBill: ";
		function __construct($input){
			if((isset($input["gab_current_so_luong"])) && !(isset($input["gab_origin_so_luong"]))){
				$input["gab_origin_so_luong"] = $input["gab_current_so_luong"];
			}
			parent::__construct($input);
		}
		function setDataIndexes(){
			$this->data_indexes = array(
			"gab_bill_id"
			,"gab_goods_id"
			,"gab_origin_so_luong"
			,"gab_current_so_luong"
			,"gab_goods_gia_ban"
			,"gab_goods_gia_von"
			,"gab_tong_tien_von"
			,"gab_tong_tien_ban"
			,"gab_tong_tien_loi"
			,"gab_thoi_gian"
			,"gab_status"
			);
		}
		function getEnglishName(){
			return "gab";
		}
		function getTableName(){
			return "hang_hoa_va_hoa_don";
		}
		private function setWhere(){
			$this->sql_model->where("gab_bill_id",$this->getPropertyValue("gab_bill_id"));
			$this->sql_model->where("gab_goods_id",$this->getPropertyValue("gab_goods_id"));
		}
		private function update($data){
			$this->setWhere();
			return $this->sql_model->update($data);
		}
		function getAmountFromDb(){
			$this->setWhere();

			$result = $this->getOne("*");

			if($result){
				return $result["gab_current_so_luong"];
			}
			else{
				throw new Exception(self::$TAG."Get Amount Failed, No record!", 1);
			}
		}
		function setAmountToDb($amount){
			if($amount){
				$this->setWhere();

				$gia_von_index = $this->getEnglishName()."_gia_von";
				$gia_ban_index = $this->getEnglishName()."_gia_ban";

				$new_tong_gia_von = $this->getPropertyValueFromDb($gia_von_index) * $amount;
				$new_tong_gia_ban = $this->getPropertyValueFromDb($gia_ban_index) * $amount;

				$result = $this->update(array("gab_current_so_luong" => $amount,"gab_tong_tien_ban" => $new_tong_gia_ban,"gab_tong_tien_von" => $new_tong_gia_von,"gab_tong_tien_loi" => ($new_tong_gia_ban - $new_tong_gia_von)));

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
			$gia_von = $this->getPropertyValue($this->getEnglishName()."_gia_von"));
			$so_luong = $this->getPropertyValue($this->getEnglishName()."_current_so_luong");

			$this->setPropertyValue($this->getEnglishName()."_tong_tien_ban") = $gia_ban * $so_luong;
			$this->setPropertyValue($this->getEnglishName()."_tong_tien_von") = $gia_von * $so_luong;
			$this->setPropertyValue($this->getEnglishName()."_tong_tien_loi") = ($gia_ban - $gia_von) * $so_luong;
			$this->insert();
		}
	}
	
?>