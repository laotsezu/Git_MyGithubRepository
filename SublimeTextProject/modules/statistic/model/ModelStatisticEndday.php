<?php 
	class ModelStatisticEndday extends BaseStatistic{
		function setTypes(){
			$this->types = array( 
				"Bán hàng" 
				,"Thu chi"
				,"Hàng hóa"
				);
		}
		function setDescriptors(){
			$this->descriptors = array( 
				"Báo cáo cuối ngày về bán hàng"
				,"Báo cáo cuối ngày về thu chi"
				,"Báo cáo cuối ngày về hàng hóa"
				);
		}
		function getTopicName(){
			return "Cuối ngày";
		}	
		function getEnglishName(){
			return "endday";
		}
		function showType0($from,$to){
			$result = $this->get("hoa_don","*",array("bill_thoi_gian",array($from,$to),"between"),null,null,null);
			return $this->handleResponse($result);
		}
		function showType1($from,$to){
			$result = $this->get("phieu_thu_chi","*",array("receipt_thoi_gian",array($from,$to),"between"),null,null,null);
			return $this->handleResponse($result);
		}	
		function showType2($from,$to){
			$result = $this->get("hang_hoa_va_hoa_don","*",array("gab_thoi_gian",array($from,$to),"between"),"gab_goods_id",null,null);
			return $this->handleResponse($result);
		}		
	}

?>