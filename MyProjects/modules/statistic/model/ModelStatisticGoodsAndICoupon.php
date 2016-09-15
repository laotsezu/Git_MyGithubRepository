<?php 
	/*class ModelStatisticGoodsAndICoupon extends BaseStatistic{
		var $table_array;
		var $columns;
		var $where;
		var $groupBy;
		var $orderBy;
		function __construct(){
			$this->table_array = array(
					array(
					//this is first table 
						"tablename" => "hang_hoa_va_nhap_hang gaic"
					)
					,
					array(
						"tablename" => "phieu_nhap_hang"
						,"jointype" => "LEFT"
						,"on" => "hang_hoa_va_nhap_hang.gaic_icoupon_id = phieu_nhap_hang.icoupon_id"
					),
					array(
						"tablename" => "hang_hoa"
						,"jointype" => "LEFT"
						,"on" => "hang_hoa_va_nhap_hang.gaic_goods_id = hang_hoa.goods_id"
					),
					array(
						"tablename" => "nguoi_lam"
						,"jointype" => "LEFT"
						,"on" => "nguoi_lam.personnel_id = phieu_nhap_hang.personnel_id"
					)
					array(
						"tablename" => "nha_cung_cap"
						,"jointype" => "LEFT"
						,"on" => "nha_cung_cap.provider_id = phieu_nhap_hang.provider_id"
					)
				);

			$this->where = array(
				"gaic_thoi_gian"
				,array()
				,"between"
				);

		}
		function setDescriptors(){
			$this->descriptors = array("Unsupported");
		}
		function setTypes(){
			$this->descriptors = array("Unsupported");
		}
		function getTopicName(){
			return "Unsupported";
		}
		function getEnglishName(){
			return "Unsupported";
		}
		function getICoupon(){
			$columns = ;
			$groupBy = ;
			$orderBy = ;
		}
		function getPersonnel(){
			$columns = ;
			$groupBy = ;
			$orderBy = ;
		}
		function getProvider(){
			$columns = ;
			$groupBy = ;
			$orderBy = ;
		}	
		function getGoods(){
			$columns = ;
			$groupBy = ;
			$orderBy = ;
		}
		function getMoneyFollowGoods(){
			return $this->getMoney("hang_hoa.goods_id","",null);
		}
		function getMoneyFollowPersonnel(){
			return $this->getMoney("nguoi_lam.personnel_id","",null);
		}
		function getMoneyFollowCustomer(){
			return $this->getMoney("nha_cung_cap.provider_id","")
		}
		function getMoney($groupBy,$orderBy,$limit){

			$columns = "hang_hoa.goods_ten,"
					."nguoi_lam.personnel_ten,"
					."nha_cung_cap.provider_ten,"
					."sum(gaic.gab_tong_tien_ban) tong_tien_ban,"
			
			return $this->getWithJoin($this->table_array, $columns, $this->where, $groupBy, $limit, $orderBy);
		}

	}*/


?>