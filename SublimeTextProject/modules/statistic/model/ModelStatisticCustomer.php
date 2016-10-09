<?php 
	class ModelStatisticCustomer extends BaseStatistic{
		
		function setTypes(){
			$this->types = array(
				"Bán hàng"
				,"Hàng bán theo khách"
				,"Công nợ"
				,"Lợi nhuận"
				);
		}
		function setDescriptors(){
			$this->descriptors = array( 
				"Top 10 khách hàng mua nhiều nhất"
				,"Báo cáo thông tin hàng bán cho mỗi khách"
				,"Top 10 khách hàng nợ nhiều nhất"
				,"Báo cáo lợi nhuận từ mỗi khách hàng"
				);
		}
		function getTopicName(){
			return "Khách hàng";
		}			
		function getEnglishName(){
			return "customer";
		}
		function showType0($from,$to){
			$table_array = array(
				array(
				//this is first table 
					"tablename" => "hoa_don"
				)
				,
				array(
					"tablename" => "hang_hoa_va_hoa_don"
					,"jointype" => "RIGHT"
					,"on" => "hoa_don.bill_id = hang_hoa_va_hoa_don.gab_bill_id"
				),
				array(
					"tablename" => "khach_hang"
					,"jointype" => "LEFT"
					,"on" => "hoa_don.customer_id = khach_hang.customer_id"
				)
			);

			$column = "SUM(hang_hoa_va_hoa_don.gab_tong_tien_ban) tong_tien_ban ,khach_hang.*";

			$where = array(
				"gab_thoi_gian"
				,array($from,$to)
				,"between"
			);
			
			$groupBy = "khach_hang.customer_id";
			$limit = 10;
			$orderBy = "SUM(hang_hoa_va_hoa_don.gab_tong_tien_ban)";

			$result = $this->getWithJoin($table_array, $column, $where, $groupBy, $limit, $orderBy);
			return $this->handleResponse($result);
		}
		function showType1($from,$to){
			$table_array = array(
				array(
				//this is first table 
					"tablename" => "hoa_don"
				)
				,
				array(
					"tablename" => "hang_hoa_va_hoa_don"
					,"jointype" => "RIGHT"
					,"on" => "hoa_don.bill_id = hang_hoa_va_hoa_don.gab_bill_id"
				),
				array(
					"tablename" => "khach_hang"
					,"jointype" => "LEFT"
					,"on" => "hoa_don.customer_id = khach_hang.customer_id"
				)

			);

			$column = "SUM(hang_hoa_va_hoa_don.gab_current_so_luong) tong_so_luong ,SUM(hang_hoa_va_hoa_don.gab_tong_tien_ban) tong_tien_ban ,khach_hang.*";
			$where = array(
				"gab_thoi_gian"
				,array($from,$to)
				,"between"
			);
			$groupBy = "khach_hang.customer_id";
			$limit = null;
			$orderBy = "SUM(hang_hoa_va_hoa_don.gab_current_so_luong)";

			$result = $this->getWithJoin($table_array, $column, $where, $groupBy, $limit, $orderBy);
			
			return $this->handleResponse($result);
		}
		function showType2($from,$to){
			$result = $this->get("khach_hang","*", null, null, 10, "customer_no_nan");
			return $this->handleResponse($result);
		}
		function showType3($from,$to){
			$table_array = array(
				array(
				//this is first table 
					"tablename" => "hoa_don"
				)
				,
				array(
					"tablename" => "hang_hoa_va_hoa_don"
					,"jointype" => "RIGHT"
					,"on" => "hoa_don.bill_id = hang_hoa_va_hoa_don.gab_bill_id"
				),
				array(
					"tablename" => "khach_hang"
					,"jointype" => "LEFT"
					,"on" => "hoa_don.customer_id = khach_hang.customer_id"
				)

			);

			$column = "SUM(hang_hoa_va_hoa_don.gab_tong_tien_loi) tong_tien_loi ,SUM(hang_hoa_va_hoa_don.gab_tong_tien_von) tong_tien_von ,SUM(hang_hoa_va_hoa_don.gab_tong_tien_ban) tong_tien_ban ,khach_hang.*";
			$where = array(
				"gab_thoi_gian"
				,array($from,$to)
				,"between"
			);
			$groupBy = "khach_hang.customer_id";
			$limit = 10;
			$orderBy = "SUM(hang_hoa_va_hoa_don.gab_tong_tien_loi)";

			$result = $this->getWithJoin($table_array, $column, $where, $groupBy, $limit, $orderBy);

			return $this->handleResponse($result);
		}
	}
?>