<?php 
	class ModelTradeBill extends BaseModel{
		function __construct($input){
			
			parent::__construct($input);

		}
		function setDataIndexes(){
			$this->data_indexes = array(
			"bill_id"
			,"bill_thoi_gian"
			,"customer_id"
			,"agency_id"
			,"personnel_id"
			,"bill_ghi_chu"
			,"bill_tong_tien_hang"
			,"bill_giam_gia"
			,"bill_tien_can_tra"
			,"bill_tien_da_tra"
			,"bill_status"
			);
		}
		function getEnglishName(){
			return "bill";
		}
		function getTableName(){
			return "hoa_don";
		}
		function decreaseGoodsIds($goods_ids){
			$goodses = $this->explodeGoodsIds($goods_ids);
			$old_goodses = $this->explodeGoodsIds($this->getPropertyValueFromDb("current_goods_ids"));
			$result = "";
			for($i = 0; $i < count($goodses) ; $i++){
				$result.= $goodses[$i]["goods_id"]."=";
				$new_amount = $old_goodses[$i]["goods_so_luong"] - $goodses[$i]["goods_so_luong"];
				
				if(!$new_amount)
					throw new Exception("Decrease Goods Ids Failed : new amount < 0", 1);
					
				$result.= $new_amount;
				$result.= ",";
			}
			$result = substr($result,0,strlen($result) - 2);
			
			$this->update(array("current_goods_ids" => $result));
		}
		function payment(){
			$goodses = $this->explodeGoodsIds($this->getPropertyValue("current_goods_ids"));
			///begin transaction 
			try{
				$this->sql_model->startTransaction();
				
				//update nguoi ban 
				$personnel = $this->model("ModelPersonnelPersonnel",array("personnel_id" => $this->getPropertyValue("personnel_id")),"personnel");
				$personnel->increaseTotalSell($this->getPropertyValue("bill_tien_da_tra"));
				//update khach hang
				if($this->getPropertyValue("customer_id")){
					$customer = $this->model("ModelPartnerCustomer",array("customer_id" => $this->getPropertyValue("customer_id")),"partner");
					$owe = $this->getPropertyValue("bill_tien_can_tra") - $this->getPropertyValue("bill_tien_da_tra");
					$customer->increaseOwe($owe);
					$customer->increaseTotalBuy($this->getPropertyValue("bill_tien_da_tra"));
				}
				//them hoa don
				$this->insert();
				////update hang hoa
				$id = $this->getLastInsertId();
				$goods = $this->model("ModelRepositoryGoods",null,"repository");
				$goods_and_bill = $this->model("ModelTradeGoodsAndBill",null,"trade")
				for($i = 0;$i < count($goodses);$i++){
					///update so luong hang hoa
					$goods_data = $goodses[$i];
					$goods->setData($goods_data);
					$goods->decreaseAmount(null);
					/////them goods _and _ bill
					$goods_and_bill = array();
					
					$goods_and_bill_data["gab_bill_id"] = $id;
					$goods_and_bill_data["gab_goods_id"] = $goods_data["goods_id"];
					$goods_and_bill_data["gab_origin_so_luong"] = $goods_data["goods_so_luong"];
					$goods_and_bill_data["gab_current_so_luong"] = $goods_data["goods_so_luong"];
					$goods_and_bill_data["gab_goods_gia_ban"] = $goods->getPropertyValueFromDb("goods_gia_ban");
					$goods_and_bill_data["gab_goods_gia_von"] = $goods->getPropertyValueFromDb("goods_gia_von");

					$goods_and_bill->setData($goods_and_bill_data);
					$goods_and_bill->add();
				}

				$this->sql_model->commit();

				$response["status"] = true;
				return $response;
			}
			catch(Exception $e){
				$this->sql_model->rollback();
				
				$response["status"] = false;
				$response["message"] = $e->getMessage();
				return $response;
			}
		}
	
	}
	
?>