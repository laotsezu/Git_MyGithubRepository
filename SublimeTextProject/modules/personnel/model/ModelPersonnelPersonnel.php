<?php 
	class ModelPersonnelPersonnel extends BaseModel{
		static $TAG = "ModelPersonnelPersonnel: ";
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
			,"personnel_tai_khoan"
			,"personnel_mat_khau"
			);
		}
		function getEnglishName(){
			return "personnel";
		}
		function getTableName(){
			return "nguoi_lam";
		}
		function increaseTotalSell($amount){
			$total_sell_index = $this->getEnglishName()."_tong_tien_ban";
			$result = $this->increasePropertyValueToDb($total_sell_index,$amount);
			
			if(!$result){
				throw new Exception(self::$TAG."Increase total sell failed! ", 1);			
			}
		}
		function register(){
			$tai_khoan_index = $this->getEnglishName()."_tai_khoan";
			$mat_khau_index = $this->getEnglishName()."_mat_khau";
			$this->sql_model->where($tai_khoan_index,$this->getPropertyValue($tai_khoan_index));
			$personnel = $this->sql_model->getOne(null);
			if(!$personnel){
				$password = $this->getPropertyValue($mat_khau_index);
				$this->setPropertyValue($mat_khau_index,SHA1($password));
				$result = $this->sql_model->insert($this->data);
				if(!$result)
					throw new Exception(self::$TAG."Register failed !", 1);
			}
			else{
				throw new Exception(self::$TAG."Account is existing !", 1);
			}
		}
		function login(){
			$tai_khoan_index = $this->getEnglishName()."_tai_khoan";
			$mat_khau_index = $this->getEnglishName()."_mat_khau";
			$this->sql_model->where($tai_khoan_index,$this->getPropertyValue($tai_khoan_index));

			$personnel = $this->sql_model->getOne(null);
			if($personnel){
				$mat_khau = SHA1($this->getPropertyValue($mat_khau_index));
				if($personnel[$mat_khau_index] !== $mat_khau){
					throw new Exception(self::$TAG."Failed, Password wrong!", 1);	
				}
			}
			else{
				throw new Exception(self::$TAG."Personnel is not existing!", 1);			
			}
		}
		function changePassword($new_password){
			$new_password = SHA1($new_password);

			$tai_khoan_index = $this->getEnglishName()."_tai_khoan";
			$mat_khau_index = $this->getEnglishName()."_mat_khau";

			$this->sql_model->where($tai_khoan_index,$this->getPropertyValue($tai_khoan_index));
			$personnel = $this->sql_model->getOne(null);
			if($personnel){
				if($personnel[$mat_khau_index] == $new_password){
					throw new Exception(self::$TAG."Failed, New password == Old Password", 1);
				}
				else{
					$this->sql_model->where($tai_khoan_index,$this->getPropertyValue($tai_khoan_index));
					$this->sql_model->update(array($mat_khau_index => $new_password));
				}
			}
			else{
				throw new Exception(self::$TAG."Personnel is not existing!", 1);
				
			}
		}
	}



?>