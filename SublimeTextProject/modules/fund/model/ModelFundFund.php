<?php 
	class ModelFundFund extends BaseModel{
		static $TAG ="ModelFundFund: ";
		function __construct($input){
			parent::__construct($input);
		}
		function setDataIndexes(){
			$this->data_indexes = array(
			"fund_id"
			,"agency_id"
			,"personnel_id"
			,"fund_tong_quy"
			,"fund_tong_thu"
			,"fund_tong_chi"
			,"fund_last_mofified"
			,"fund_status"
			);
		}
		function getEnglishName(){
			return "fund";
		}
		function getTableName(){
			return "quy";
		}
		function setWhere(){
			$this->sql_model->where("agency_id",$this->getPropertyValue("agency_id"));
		}
		function checkChangeAvailable(){
			$result = $this->sql_model->getOne(null);
			if(!$result){
				throw new Exception(self::$TAG."Agency was not have Fund yet!", 1);
			}
			else{
				$personnel_id_avaiable = $result["personnel_id"];
				if($personnel_id_avaiable !== $this->getPropertyValue("personnel_id")){
					throw new Exception(self::$TAG."Personnel unable change Fund Amount !", 1);
				}
			}
		}
		function getOneFund(){
			$this->setWhere();
			$result = $this->sql_model->getOne(null);
			if($result){
				return $result;
			}
			else{
				throw new Exception(self::$TAG . "Fund of agency is not exist!", 1);			
			}
		}

		function getFundAmount(){
			$one_fund = $this->getOneFund();
			return $one_fund["fund_tong_quy"];
		}
		function getThuAmount(){
			$one_fund = $this->getOneFund();
			return $one_fund["fund_tong_thu"];
		}
		function getChiAmount(){
			$one_fund = $this->getOneFund();
			return $one_fund["fund_tong_chi"];
		}
		function setFundAmounts($tong,$thu,$chi){
			$this->setWhere();
			$this->checkChangeAvailable();

			$data = array();

			if($tong){
				$data["fund_tong_quy"] = $tong;
			}
			if($thu){
				$data["fund_tong_thu"] = $thu;
			}
			if($chi){
				$data["fund_tong_chi"] = $chi;
			}	

			if($data){
				$data["fund_last_modified"] = $this->getCurrentTime();

				$result = $this->sql_model->update($data);

				if(!$result){
					throw new Exception(self::$TAG."Set Fund Amounts Failed !", 1);					
				}
			}
			else{
				throw new Exception(self::$TAG."Set Fund Amounts Failed, Data update == null ", 1);				
			}
		}
		function decreaseFund($amount){
			if($amount > 0){
				$current_tong = $this->getFundAmount();
				$current_tong -= $amount;

				$current_chi = $this->getChiAmount();
				$current_chi += abs($amount);

				$this->setFundAmounts($current_tong,null,$current_chi);
			}
			else{
				throw new Exception(self::$TAG."Decrease Fund Failed , Amount <= 0 ", 1);
			}
		}
		function increaseFund($amount){
			if($amount > 0){
				$current_tong = $this->getFundAmount();
				$current_tong += $amount;
				
				$current_thu = $this->getThuAmount();
				$current_thu += $amount;

				$this->setFundAmounts($current_tong,$current_thu,null);
			}	
			else{
				throw new Exception(self::$TAG."Increase Fund Failed , Amount <= 0 ", 1);
			}
		}
	}
?>