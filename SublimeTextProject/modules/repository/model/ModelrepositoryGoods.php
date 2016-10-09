<?php 
	class ModelRepositoryGoods extends BaseModel{
		function __construct($input){
			parent::__construct($input);
		}
		function setDataIndexes(){
			$this->data_indexes = array(
			"goods_id"
			,"goods_ten"
			,"goods_nhom"
			,"goods_loai"
			,"goods_gia_ban"
			,"goods_gia_von"
			,"goods_so_luong"
			,"goods_status"
			);
		}
		function getEnglishWord(){
			return "goods";
		}
		function getTableName(){
			return "hang_hoa";
		}
		function increaseAmount($amount){
			$amount_index = $this->getEnglishWord()."_so_luong";
			if(!$amount){
				$amount = $this->getPropertyValue($amount_index);
			}
			if($amount){
				$result =  $this->increasePropertyValueToDb($amount_index,$amount);
				if(!$result)
					throw new Exception("Increase Amount : Increase Property value to db failed! ", 1);
			}
			else{
				throw new Exception("Increase Amount : Amount == false !", 1);
			}
		}
		function decreaseAmount($amount){ 
			$this->increaseAmount($amount * -1);
		}
		public static function explodeGoodsIds($goods_ids){
			$items = array();
			$goodses = explode(",",$goods_id);
			for($i = 0; $i < count($goodses); $i++){
				$goods = $goodses[$i];
				$goods = explode("=",$goods);
				$goods_id = $goods[0];
				$goods_count = $goods[1];
				$item = array("goods_id" => $goods_ids,"goods_so_luong" => $goods_count);
				$items[] = $item;
			}
			return $items;
		}

	}
	
?>