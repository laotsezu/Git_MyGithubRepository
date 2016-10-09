<?php 
	class ModelStatisticProvider extends BaseStatistic{
		
		function setTypes(){
			$this->types = array(
				"Nhập hàng"
				,"Hàng nhập theo NCC"
				,"Công nợ"
				);
		}
		function setDescriptors(){
			$this->descriptors = array( 
				"Top 10 nhà cung cấp nhập hàng nhiều nhất(trừ trả hàng)"	
				,"Báo cáo danh sách hàng nhập của mỗi nhà cung cấp"
				,"Báo cáo số nợ đối với mỗi nhà cung cấp"
				);
		}
		function getTopicName(){
			return "Nhà cung cấp";
		}		
		function getEnglishName(){
			return "provider";
		}
		function showType0($from,$to){
			$table_array = array(
					array(
					//this is first table 
						"tablename" => "hang_hoa_va_nhap_hang"
					)
					,
					array(
						"tablename" => "phieu_nhap_hang"
						,"jointype" => "LEFT"
						,"on" => "hang_hoa_va_nhap_hang.gaic_icoupon_id = phieu_nhap_hang.icoupon_id"
					),
					array(
						"tablename" => "nha_cung_cap"
						,"jointype" => "LEFT"
						,"on" => "phieu_nhap_hang.provider_id = nha_cung_cap.provider_id"
					)
				);

			$columns = "nha_cung_cap.provider_id,nha_cung_cap.provider_ten,sum(hang_hoa_va_nhap_hang.gaic_tong_tien_ban) tong_tien_ban";
			$where = array(
				"gaic_thoi_gian"
				,array($from,$to)
				,"between"
				);
			$groupBy = "nha_cung_cap.provider_id";
			$limit = 10;
			$orderBy = "sum(hang_hoa_va_nhap_hang.gaic_tong_tien_ban)";

			$result = $this->getWithJoin($table_array, $columns, $where, $groupBy, $limit, $orderBy);
			return $this->handleResponse($result);
		}
		function showType1($from,$to){
			$table_array = array(
					array(
					//this is first table 
						"tablename" => "hang_hoa_va_nhap_hang"
					)
					,
					array(
						"tablename" => "phieu_nhap_hang"
						,"jointype" => "LEFT"
						,"on" => "hang_hoa_va_nhap_hang.gaic_icoupon_id = phieu_nhap_hang.icoupon_id"
					),
					array(
						"tablename" => "nha_cung_cap"
						,"jointype" => "LEFT"
						,"on" => "phieu_nhap_hang.provider_id = nha_cung_cap.provider_id"
					)
				);

			$columns = "nha_cung_cap.provider_id,nha_cung_cap.provider_ten,sum(hang_hoa_va_nhap_hang.gaic_current_so_luong) tong_so_luong,sum(hang_hoa_va_nhap_hang.gaic_tong_tien_ban) tong_tien_ban";
			$where = array(
				"gaic_thoi_gian"
				,array($from,$to)
				,"between"
				);
			$groupBy = "nha_cung_cap.provider_id";
			$limit = null;
			$orderBy = "sum(hang_hoa_va_nhap_hang.gaic_current_so_luong)";

			$result = $this->getWithJoin($table_array, $columns, $where, $groupBy, $limit, $orderBy);
			return $this->handleResponse($result);
		}
		function showType2($from,$to){
			$result = $this->get("nha_cung_cap","*",null,null,10,"provider_no_nan");
			return $this->handleResponse($result);
		}
	}

?>