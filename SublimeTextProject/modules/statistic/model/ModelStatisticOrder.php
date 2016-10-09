<?php 
	class ModelStatisticOrder extends BaseStatistic{
		
		function setTypes(){
			$this->types = array(
				"Hàng hóa"
				,"Giao dịch"
				);
		}
		function setDescriptors(){
			$this->descriptors = array( 
				
				,
				,
				,
				,
				,
				,
				,
				);
		}
		function getTopicName(){
			return "Đặt hàng";
		}	
		function getEnglishName(){
			return "order";
		}
	}

?>