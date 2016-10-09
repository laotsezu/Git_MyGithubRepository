<?php 
	class ModelStatisticSell extends BaseStatistic{
		
		function setTypes(){
			$this->types = array(
				"Thời gian" 
				,"Lợi nhuận" 
				,"Giảm giá hóa đơn" 
				,"Trả hàng" 
				,"Nhân viên" 
				,"Chi nhánh" 
				);
		}
		function setDescriptors(){
			$this->descriptors = array( 
				"Báo cáo doanh thu bán hàng"
				,"Báo cáo lợi nhuận bán hàng"
				,"Báo cáo hóa đơn giảm giá"
				,"Báo cáo đơn hàng trả"
				,"Báo cáo thông tin bán hàng của nhân viên"
				,"Báo cáo thông tin bán hàng của chi nhánh"
				);
		}
		function getTopicName(){
			return "Bán hàng";
		}	
		function getEnglishName(){
			return "sell";
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
			$result = $this->getWithJoin($table_array,"hang_hoa_va_hoa_don.*,hang_hoa.goods_ten",array("gab_thoi_gian",array($from,$to),"between"),null);

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
			$result = $this->getWithJoin($table_array,"hang_hoa_va_hoa_don.*,hang_hoa.goods_ten",array("gab_thoi_gian",array($from,$to),"between"),null);

			return $this->handleResponse($result);	
		}
		function showType2($from,$to){
			$result = null; 
			return $this->handleResponse($result);	
		}
		function showType3($from,$to){	
			$result = $this->get("hoa_don_tra_hang","*",array("rbill_thoi_gian",array($from,$to),"between"),null);
			return $this->handleResponse($result);	
		}
		function showType4($from,$to){
			$table_array = array(
					array(
					//this is first table 
						"tablename" => "hoa_don"
					)
					,
					array(
						"tablename" => "hang_hoa_va_hoa_don"
						,"jointype" => "RIGHT"
						,"on" => "hang_hoa_va_hoa_don.gab_bill_id = hoa_don.bill_id"
					)
					,array(
						"tablename" => "nguoi_lam"
						,"jointype" => "LEFT"
						,"on" => "hoa_don.personnel_id = nguoi_lam.personnel_id"
					)
				);
			$result = $this->getWithJoin($table_array,"nguoi_lam.*,sum(hang_hoa_va_hoa_don.gab_tong_tien_ban) tong_tien_ban ",array("gab_thoi_gian",array($from,$to),"between"),"personnel_id");
			return $this->handleResponse($result);	
		}
		function showType5($from,$to){
			$result = $this->get("hoa_don","*",array("bill_thoi_gian",array($from,$to),"between"),"agency_id",null,"bill_tong_tien_hang");
			
			return $this->handleResponse($result);	
		}
	}

?>