<?php 
	class ModelRepositoryCoupon extends BaseModel{
		function __construct($input){
			parent::__construct($input);
		}
		function setDataIndexes(){
			$this->data_indexes = array(
			"coupon_id"
			,"coupon_thoi_gian"
			,"agency_id"
			,"personnel_id"
			,"current_goods_ids"
			,"origin_goods_ids"
			,"coupon_ghi_chu"
			,"coupon_status"
			);
		}
		function getEnglishName(){
			return "coupon";
		}
		function getTableName(){
			return "phieu_kiem_hang";
		}
		function accept(){
			try{
				$this->sql_model->startTransaction();
				//bat dau thiet lap lai so luong cua tung hang hoa
				$goodses = $this->explodeGoodsIds($this->getPropertyValue("current_goods_ids"));
				$goods = $this->model("ModelRepositoryGoods",null,"repository");
				$so_luong_id = $goods->getEnglishName()."_so_luong";

				for($i = 0; $i < count($goodses) ; $i++){
					$goods_data = $goodses[$i];
					$goods->setData($goods_data);
					$goods->setAmount($goods_data[$so_luong_id]);
				}
				//bat dau them phieu kiem hang
				$this->insert();

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