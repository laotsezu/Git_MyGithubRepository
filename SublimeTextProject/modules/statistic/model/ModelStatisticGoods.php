<?php 
	class ModelStatisticGoods extends BaseStatistic{
		
		function setTypes(){
			$this->types = array(
				"Bán hàng"
				,"Lợi nhuận"
				,"Xuất hủy"
				,"Xuất nhập tồn"
				,"Xuất nhập tồn chi tiết"
				,"Nhân viên theo hàng bán"
				,"Khách theo hàng bán"
				,"NCC Theo hàng nhập"
				);
		}
		function setDescriptors(){
			$this->descriptors = array( 
				"Top 10 sản phẩm doanh số cao nhất (đã trừ trả hàng)"
				,"Top 10 sản phẩm có lợi nhuận cao nhất"
				,""
				,""
				,""
				,"Báo cáo hàng bán theo mỗi nhân viên"
				,"Báo cáo hàng bán theo mỗi khách hàng"
				,"Báo cáo hàng bán theo mỗi nhà cung cấp"
				);
		}
		function getTopicName(){
			return "Hàng hóa";
		}	
		function getEnglishName(){
			return "goods";
		}
		function showType0($from,$to){
			
			$table_array = array(
					array(
					//this is first table 
						"tablename" => "hang_hoa_va_hoa_don"
					)
					,
					array(
						"tablename" => "hang_hoa"
						,"jointype" => "LEFT"
						,"on" => "hang_hoa_va_hoa_don.gab_goods_id = hang_hoa.goods_id"
					)
				);

			$where = array(
				"gab_thoi_gian"
				,array($from,$to)
				,"between"
			);

			$columns = "hang_hoa.goods_ten,SUM(gab_current_so_luong) tong_so_luong, SUM(hang_hoa_va_hoa_don.gab_tong_tien_loi) tong_tien_loi ,SUM(hang_hoa_va_hoa_don.gab_tong_tien_von) tong_tien_von , SUM(hang_hoa_va_hoa_don.gab_tong_tien_ban) tong_tien_ban ";
			$groupBy = "gab_goods_id";
			$limit = 10;
			$orderBy = "SUM(hang_hoa_va_hoa_don.gab_tong_tien_ban)";
			$result = $this->getWithJoin($table_array,$columns, $where, $groupBy, $limit, $orderBy);
			
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
						"tablename" => "hang_hoa"
						,"jointype" => "LEFT"
						,"on" => "hang_hoa_va_hoa_don.gab_goods_id = hang_hoa.goods_id"
					)
				);

			$where = array(
				"gab_thoi_gian"
				,array($from,$to)
				,"between"
			);
			$columns = "hang_hoa.goods_ten,SUM(gab_current_so_luong) tong_so_luong, SUM(hang_hoa_va_hoa_don.gab_tong_tien_loi) tong_tien_loi ,SUM(hang_hoa_va_hoa_don.gab_tong_tien_von) tong_tien_von , SUM(hang_hoa_va_hoa_don.gab_tong_tien_ban) tong_tien_ban ";
			$groupBy = "hang_hoa_va_hoa_don.gab_goods_id";
			$limit = 10;
			$orderBy = "SUM(hang_hoa_va_hoa_don.gab_tong_tien_loi)";

			$result = $this->getWithJoin($table_array,$columns, $where, $groupBy, $limit, $orderBy);
			
			return $this->handleResponse($result);
		}
		function show_maintaining_err(){
			$response["status"] = false;
			$response["message"] = "Function is maintaining!";

			die(json_encode($response));
		}
		function showType2($from,$to){
			$this->show_maintaining_err();
		}
		function showType3($from,$to){
			$this->show_maintaining_err();
		}
		function showType4($from,$to){
			$this->show_maintaining_err();
		}
		function showType5($from,$to){

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
					)
					,
					array(
						"tablename" => "nguoi_lam"
						,"jointype" => "LEFT"
						,"on" => "hoa_don.personnel_id = nguoi_lam.personnel_id"
					),
					array(
						"tablename" => "hang_hoa"
						,"jointype" => "LEFT"
						,"on" => "hang_hoa_va_hoa_don.gab_goods_id = hang_hoa.goods_id"
					)
				);

			$where = array(
				"gab_thoi_gian"
				,array($from,$to)
				,"between"
			);
			$columns = "hang_hoa.goods_ten , sum(nguoi_lam.personnel_id) tong_nguoi_lam , sum(hang_hoa_va_hoa_don.gab_current_so_luong) tong_so_luong,sum(hang_hoa_va_hoa_don.gab_tong_tien_ban) tong_tien_ban ";
			$groupBy = "gab_goods_id";

			$limit = null;
			$orderBy = "SUM(nguoi_lam.personnel_id)";
			$result = $this->getWithJoin($table_array,$columns, $where, $groupBy, $limit, $orderBy);
			
			return $this->handleResponse($result);
		}
		function showType6($from,$to){

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
					)
					,
					array(
						"tablename" => "khach_hang"
						,"jointype" => "LEFT"
						,"on" => "hoa_don.customer_id = khach_hang.customer_id"
					),
					array(
						"tablename" => "hang_hoa"
						,"jointype" => "LEFT"
						,"on" => "hang_hoa_va_hoa_don.gab_goods_id = hang_hoa.goods_id"
					)
				);

			$where = array(
				"gab_thoi_gian"
				,array($from,$to)
				,"between"
			);
			$columns = "hang_hoa.goods_ten, sum(khach_hang.customer_id) tong_khach_hang,sum(hang_hoa_va_hoa_don.gab_current_so_luong) tong_so_luong ,sum(hang_hoa_va_hoa_don.gab_tong_tien_ban) tong_tien_ban ";
			$groupBy = "gab_goods_id";

			$limit = null;
			$orderBy = "SUM(khach_hang.customer_id)";
			$result = $this->getWithJoin($table_array,$columns, $where, $groupBy, $limit, $orderBy);
			
			return $this->handleResponse($result);
		}
		function showType7($from,$to){
			$table_array = array(
					array(
					//this is first table 
						"tablename" => "phieu_nhap_hang"
					)
					,
					array(
						"tablename" => "hang_hoa_va_nhap_hang"
						,"jointype" => "RIGHT"
						,"on" => "hang_hoa_va_nhap_hang.gaic_icoupon_id = phieu_nhap_hang.icoupon_id"
					)
					,
					array(
						"tablename" => "nha_cung_cap"
						,"jointype" => "LEFT"
						,"on" => "phieu_nhap_hang.provider_id = nha_cung_cap.provider_id"
					),
					array(
						"tablename" => "hang_hoa"
						,"jointype" => "LEFT"
						,"on" => "hang_hoa_va_nhap_hang.gaic_goods_id = hang_hoa.goods_id"
					)
				);

			$where = array(
				"gaic_thoi_gian"
				,array($from,$to)
				,"between"
			);
			$columns = "hang_hoa.goods_ten, sum(nha_cung_cap.provider_id) tong_nha_cung_cap , sum(hang_hoa_va_nhap_hang.gaic_current_so_luong) tong_so_luong , sum(hang_hoa_va_nhap_hang.gaic_tong_tien_ban) tong_tien_ban ";
			$groupBy = "gaic_goods_id";

			$limit = null;
			$orderBy = "SUM(nha_cung_cap.provider_id)";
			$result = $this->getWithJoin($table_array,$columns, $where, $groupBy, $limit, $orderBy);
			
			return $this->handleResponse($result);
		}
	}

?>