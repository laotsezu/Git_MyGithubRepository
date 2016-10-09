<?php 
	class ModelTradeBill extends BaseModel{
		function __construct($input){
			parent::__construct($input);
		}
		function setDataIndexes(){
			$this->data_indexes = array(
			"bill_id"
			,"bill_thoi_gian"
			,"goods_ids"
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
		function getEnglishWord(){
			return "bill";
		}
		function getTableName(){
			return "hoa_don";
		}
		function payment(){
			$goodses = Goods::explodeGoodsIds($this->getPropertyValue("goods_ids"));
			///begin transaction 
			try{
				$this->sql_model->startTransaction();
				////update hang hoa
				for($i = 0;$i < count($goodses);$i++){
					$goods_data = $goodses[$i];
					$goods = new Goods($goods_data);
					$goods->decreaseAmount(null);
				}
				//update nguoi ban 
				$personnel = new Personnel(array("personnel_id" => $this->getPropertyValue("personnel_id")));
				$personnel->increaseTotalSell($this->getPropertyValue("bill_tien_da_tra"));
				//update khach hang
				if($this->getPropertyValue("customer_id")){
					$customer = new Customer(array("customer_id" => $this->getPropertyValue("customer_id")));
					$owe = $this->getPropertyValue("bill_tien_can_tra") - $this->getPropertyValue("bill_tien_da_tra");
					$customer->increaseOwe($owe);
					$customer->increaseTotalBuy($this->getPropertyValue("bill_tien_da_tra"));
				}
				//update hoa don
				$this->insert($this->data);

				$this->sql_model->commit();
			}
			catch(Exception $e){
				$this->sql_model->rollback();
			}
		}
		function return(){
			
		}
	}
	
?>