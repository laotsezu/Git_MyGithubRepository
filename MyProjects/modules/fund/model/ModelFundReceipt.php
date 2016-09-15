<?php 
	class ModelFundReceipt extends BaseModel{
		function __construct($input){
			parent::__construct($input);
		}

		function setDataIndexes(){
			$this->data_indexes = array(
			"receipt_id"
			,"receipt_thoi_gian"
			,"receipt_loai_phieu"
			,"receipt_kieu_thanh_toan"
			,"personnel_id"
			,"agency_id"
			,"receipt_so_tai_khoan"
			,"receipt_nguoi_nop_nhan"
			,"receipt_gia_tri"
			,"receipt_ly_do"
			,"receipt_status"
			);
		}
		function getEnglishName(){
			return "receipt";
		}
		function getTableName(){
			return "phieu_thu_chi";
		}
		function createReceipt($type){
			$this->check_type($type);
			try{
				$loai_phieu_index = $this->getEnglishName()."_loai_phieu";
				$amount = $this->getPropertyValue($this->getEnglishName()."_gia_tri");

				$fund = $this->model("ModelFundFund",null, "fund");
				$fund_data["agency_id"] = $this->getPropertyValue("agency_id");
				$fund_data["personnel_id"] = $this->getPropertyValue("personnel_id");
				$fund->setData($fund_data);

				$amount = abs($amount);

				if($type === "thu"){					
					$fund->increaseFund($amount);
				}
				else if($type === "chi"){
					$fund->decreaseFund($amount);
				}

				///update lai fund		
				
				$this->setPropertyValue($loai_phieu_index,$type);
				$this->insert();
				$response["status"] = true;
			}
			catch(Exception $e){
					$response["status"] = false;
					$response["message"] = $e->getMessage();
			}
			return $response;
		}
		function check_type($type){
			if(!$type || ($type !== "thu" && $type !== "chi")){
				$response["status"] = false;
				$response["message"] = "Type unavaiable, Type must be thu/chi !";

				die(json_encode($response));
			}
		}
	}
?>