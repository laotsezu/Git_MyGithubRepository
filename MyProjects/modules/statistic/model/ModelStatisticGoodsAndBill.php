<?php 
/*	class ModelStatisticGoodsAndBill extends BaseStatistic{
		var $table_array;
		var $columns;
		var $where;
		var $groupBy;
		var $orderBy;
		function __construct(){
			$this->table_array = array(
					array(
					//this is first table 
						"tablename" => "hang_hoa_va_hoa_don gab"
					)
					,
					array(
						"tablename" => "hoa_don"
						,"jointype" => "LEFT"
						,"on" => "hang_hoa_va_hoa_don.gab_bill_id = hoa_don.bill_id"
					),
					array(
						"tablename" => "hang_hoa"
						,"jointype" => "LEFT"
						,"on" => "hang_hoa_va_hoa_don.gab_goods_id = hang_hoa.goods_id"
					),
					array(
						"tablename" => "nguoi_lam"
						,"jointype" => "LEFT"
						,"on" => "nguoi_lam.personnel_id = hoa_don.personnel_id"
					)
					array(
						"tablename" => "khach_hang"
						,"jointype" => "LEFT"
						,"on" => "khach_hang.customer_id = hoa_don.customer_id"
					)
				);

			$this->where = array(
				"gab_thoi_gian"
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

		function getBill(){
			$this->columns = "*";
			$this->groupBy = "";
			$this->orderBy = "";
		}
		function getPersonnel(){
			$this->columns = "*";
			$this->groupBy = "";
			$this->orderBy = "";
		}
		function getCustomer(){
			$this->columns = "";
			$this->groupBy = "";
			$this->orderBy = "";
		}
		function getGoods(){
			$this->columns = "";
			$this->groupBy = "";
			$this->orderBy = "";
		}
		function getMoneyFollowGoods(){
			return $this->getMoney("hang_hoa.goods_id");
		}
		function getMoneyFollowPersonnel(){
			return $this->getMoney("nguoi_lam.personnel_id");
		}
		function getMoneyFollowCustomer(){
			return $this->getMoney("khach_hang.customer_id")
		}
		function getTotalMoney(){
			return $this->
		}
		
		private function getMoney($groupBy,$orderBy=null,$limit=null){
			
			if($groupBy){
				$this->columns = "hang_hoa.goods_ten,"
						."nguoi_lam.personnel_ten,"
						."khach_hang.customer_ten,"
						."sum(gab.gab_current_so_luong) tong_so_luong ,"
						."sum(gab.gab_tong_tien_ban) tong_tien_ban,"
						."sum(gab.gab_tong_tien_von) tong_tien_von,"
						."sum(gab.gab_tong_tien_loi) tong_tien_loi";
			}
			else{
				$this->columns = ."sum(gab.gab_current_so_luong) tong_so_luong ,"
						."sum(gab.gab_tong_tien_ban) tong_tien_ban,"
						."sum(gab.gab_tong_tien_von) tong_tien_von,"
						."sum(gab.gab_tong_tien_loi) tong_tien_loi";
			}

			$this->groupBy = $groupBy;
			$this->orderBy = $orderBy;
			$this->limit = $limit;
			
			return $this->exe_sql_command();
		
		}
		function exe_sql_command(){
			return $this->getWithJoin($this->table_array,$columns,$this->where,$this->groupBy,$this->limit,$this->orderBy);
		}
	}
*/
?>