<?php 
	class ModelStatisticPersonnel extends BaseStatistic{
		
		function setTypes(){
			$this->types = array(
				"Bán hàng" 
				,"Hàng bán theo nhân viên" 
				,"Lợi nhuận"
				);
		}
		function setDescriptors(){
			$this->descriptors = array( 
				"Báo cáo thông tin doanh số bán hàng của mỗi nhân viên"
				,"Báo cáo số hàng bán của mỗi nhân viên"
				,"Báo cáo lợi nhuận từ mỗi nhân viên"
				);
		}
		function getTopicName(){
			return "Người làm";
		}		
		function getEnglishName(){
			return "personnel";
		}
		function showType0($from,$to){

			$table_array = array(
					array(
					//this is first table 
						"tablename" => "hang_hoa_va_hoa_don"
					)
					,
					array(
						"tablename" => "hoa_don"
						,"jointype" => "LEFT"
						,"on" => "hang_hoa_va_hoa_don.gab_bill_id = hoa_don.bill_id"
					),
					array(
						"tablename" => "nguoi_lam"
						,"jointype" => "LEFT"
						,"on" => "hoa_don.personnel_id = nguoi_lam.personnel_id"
					)
				);

			$columns = "nguoi_lam.personnel_ten ,sum(hang_hoa_va_hoa_don.gab_tong_tien_ban) tong_tien_ban , sum(hang_hoa_va_hoa_don.gab_current_so_luong) tong_so_luong ,  sum(hang_hoa_va_hoa_don.gab_tong_tien_von) tong_tien_von , sum(hang_hoa_va_hoa_don.gab_tong_tien_loi) tong_tien_loi";
			$where = array(
				"gab_thoi_gian"
				,array($from,$to)
				,"between"
				);
			$groupBy = "nguoi_lam.personnel_id";
			$limit = null;
			$orderBy = "sum(hang_hoa_va_hoa_don.gab_tong_tien_ban)";


			$result = $this->getWithJoin($table_array, $columns, $where, $groupBy, $limit, $orderBy);
			return $this->handleResponse($result);
		}
		function showType1($from,$to){
			$table_array = array(
					array(
					//this is first table 
						"tablename" => "hang_hoa_va_hoa_don"
					)
					,
					array(
						"tablename" => "hoa_don"
						,"jointype" => "LEFT"
						,"on" => "hang_hoa_va_hoa_don.gab_bill_id = hoa_don.bill_id"
					),
					array(
						"tablename" => "nguoi_lam"
						,"jointype" => "LEFT"
						,"on" => "hoa_don.personnel_id = nguoi_lam.personnel_id"
					)
				);

			$columns = "nguoi_lam.personnel_ten ,sum(hang_hoa_va_hoa_don.gab_current_so_luong) tong_so_luong , sum(hang_hoa_va_hoa_don.gab_tong_tien_ban) tong_tien_ban , sum(hang_hoa_va_hoa_don.gab_tong_tien_von) tong_tien_von , sum(hang_hoa_va_hoa_don.gab_tong_tien_loi) tong_tien_loi";
			$where = array(
				"gab_thoi_gian"
				,array($from,$to)
				,"between"
				);
			$groupBy = "nguoi_lam.personnel_id";
			$limit = null;
			$orderBy = "sum(hang_hoa_va_hoa_don.gab_current_so_luong) ";


			$result = $this->getWithJoin($table_array, $columns, $where, $groupBy, $limit, $orderBy);
			return $this->handleResponse($result);
		}
		function showType2($from,$to){		
			$table_array = array(
					array(
					//this is first table 
						"tablename" => "hang_hoa_va_hoa_don"
					)
					,
					array(
						"tablename" => "hoa_don"
						,"jointype" => "LEFT"
						,"on" => "hang_hoa_va_hoa_don.gab_bill_id = hoa_don.bill_id"
					),
					array(
						"tablename" => "nguoi_lam"
						,"jointype" => "LEFT"
						,"on" => "hoa_don.personnel_id = nguoi_lam.personnel_id"
					)
				);

			$columns = "nguoi_lam.personnel_ten, sum(hang_hoa_va_hoa_don.gab_tong_tien_loi) tong_tien_loi ,sum(hang_hoa_va_hoa_don.gab_current_so_luong) tong_so_luong , sum(hang_hoa_va_hoa_don.gab_tong_tien_ban) tong_tien_ban , sum(hang_hoa_va_hoa_don.gab_tong_tien_von) tong_tien_von ";
			$where = array(
				"gab_thoi_gian"
				,array($from,$to)
				,"between"
				);
			$groupBy = "nguoi_lam.personnel_id";
			$limit = null;
			$orderBy = "sum(hang_hoa_va_hoa_don.gab_tong_tien_loi) ";


			$result = $this->getWithJoin($table_array, $columns, $where, $groupBy, $limit, $orderBy);
			return $this->handleResponse($result);
		}
	}

?>