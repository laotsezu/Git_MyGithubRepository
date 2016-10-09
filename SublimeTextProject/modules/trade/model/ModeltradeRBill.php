<?php 
	class ModelTradeRBill extends BaseModel{

		function __construct($input){
			parent::__construct($input);
		}
		function setDataIndexes(){
			$this->data_indexes = array(
			"rbill_id"
			,"rbill_thoi_gian"
			,"bill_id"
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
			return "rbill";
		}
		function getTableName(){
			return "hoa_don_tra_hang";
		}
		function return(){
			$goodses = Goods::explodeGoodsIds($this->getPropertyValue("goods_ids"));
			try{
				$this->sql_model->startTransaction();
				////update hang hoa
				for($i = 0;$i < count($goodses);$i++){
					$goods_data = $goodses[$i];
					$goods = new Goods($goods_data);
					$goods->increaseAmount(null);
				}
				//update nguoi ban 
				$personnel = new Personnel(array("personnel_id" => $this->getPropertyValue("personnel_id")));
				$personnel->increaseTotalSell($this->getPropertyValue("bill_tien_da_tra") * -1);

				//update khach hang
				if($this->getPropertyValue("customer_id")){
					$customer = new Customer(array("customer_id" => $this->getPropertyValue("customer_id")));
					$customer->increaseTotalBuy($this->getPropertyValue("bill_tien_da_tra") * -1);
				//update bill
				$bill= new Bill(array("bill_id" => $this->getPropertyValue("bill_id")));
				
				$this->sql_model->commit();
			}
			catch(Exception $e){
				$this->sql_model->rollback();
			}
		}	
	}
?>