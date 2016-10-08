<?php 
	class ModelRepositoryICoupon extends BaseModel{ 
		function __construct($input){
			parent::__construct($input);
		}
		function setDataIndexes(){
			$this->data_indexes = array(
			"icoupon_id"
			,"icoupon_thoi_gian"
			,"icoupon_goods_ids"
			,"provider_id"
			,"agency_id"
			,"personnel_id"
			,"icoupon_tong_tien_hang"
			,"icoupon_giam_gia"
			,"icoupon_tien_phai_tra"
			,"icoupon_tien_da_tra"
			,"icoupon_ghi_chu"
			,"icoupon_status"
			);
		}
		function getEnglishName(){
			return "icoupon";
		}
		function getTableName(){
			return "phieu_nhap_hang";
		}
		function accept(){
			try{
				$this->sql_model->startTransaction();
				
				////De nghi tang tong tien mua + tinh toan no nan cho Provider
				$provider_id = $this->getPropertyValue("provider_id");
				if($provider_id){
					$provider = $this->model("ModelPartnerProvider",array("provider_id" => $provider_id),"partner")
					$provider->increaseTotalBuy($this->getPropertyValue("icoupon_tien_da_tra"));
					$owe = $this->getPropertyValue("icoupon_tien_phai_tra") - $this->getPropertyValue("icoupon_tien_da_tra");
					if($owe > 0){
						$provider->increaseOwe($owe);
					}
					else if($owe < 0){
						$provider->decreaseOwe($owe);
					}
				}

				//bat dau them phieu kiem hang
				$this->insert();
				$icoupon_id = $this->getLastInsertId();
				//bat dau tang so luong cua tung hang hoa
				$goods = $this->model("ModelRepositoryGoods",null,"repository");
				$goods_and_icoupon = $this->model("ModelRepositoryGoodsAndICoupon",null,"repository");
				$so_luong_index = $goods->getEnglishName()."_so_luong";
				$goodses = $this->explodeGoodsIds($this->getPropertyValue("icoupon_goods_ids"));

				for($i = 0; $i < count($goodses) ; $i++){
					$goods_data = $goodses[$i];
					$goods->setData($goods_data);
					$goods->increaseAmount($goods_data[$so_luong_index]);

					$goods_and_icoupon_data = array();

					$goods_and_icoupon_data["gaic_icoupon_id"] = $icoupon_id;
					$goods_and_icoupon_data["gaic_goods_id"] = $goods["goods_id"];
					$goods_and_icoupon_data["gaic_origin_so_luong"] = $goods["goods_so_luong"];
					$goods_and_icoupon_data["gaic_current_so_luong"] = $goods["goods_so_luong"];
					$goods_and_icoupon_data["gaic_goods_gia_ban"] = $goods["goods_gia_ban"];

					$goods_and_icoupon->setData($goods_and_icoupon_data);
					$goods_and_icoupon->add();
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