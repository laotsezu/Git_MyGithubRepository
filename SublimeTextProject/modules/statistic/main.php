<?php 
class Statistic extends iBNC{
		static $TAG = "Statistic: ";
		public function __construct(){ 
			global $_B; 
			$this->r = $_B['r'];   
			$this->cache = $_B['cache'];   
			$controller = $this->r->get_string('controller','GET');
			$this->action = $this->r->get_string('action','GET');
			
			$this->d = array(); 

			if(method_exists($this, $controller)){
				$this->$controller();
			}
			else{
				$this->index();
			}
		} 
		function index(){
				
		}  
		function get(){
			$type = $this->r->get_string("type","GET",null);
			$from = $this->r->get_int("time_from","GET",0);
			$to = $this->r->get_int("time_to","GET",999999999999999);
			$position = $this->r->get_int("position","GET",-1);
			//$agency_id = $this->r->get_int("agency_id","GET",0);

			if(!$type){
				$this->mDie("Type missing!");
			}
	
			$get_model_method = "get_model_" . $type;
			$model = $this->$get_model_method();
			/*if(!$agency_id){
				$model->setAgencyId($agency_id);
			}*/
			if($position < 0){
				$result["items"] = $model->getTypes();
				$result["status"] = true;
			}
			else{
				$result = $model->getStatisticResult($position,$from,$to);
				$result["type"] = $model->getType($position);
				$result["descriptor"] = $model->getDescriptor($position);
			}
			echo json_encode($result);
		}
		private function get_model_cuoingay(){
			$endday = $this->model("ModelStatisticEndday",null,"statistic");
			return $endday;
		}	
		private function get_model_banhang(){
			$banhang = $this->model("ModelStatisticSell",null,"statistic");					
			return $banhang;
		}
		private function get_model_hanghoa(){
			$hanghoa = $this->model("ModelStatisticGoods",null,"statistic");
			return $hanghoa;
		}
		private function get_model_khachhang(){
			$customer = $this->model("ModelStatisticCustomer",null,"statistic");
			return $customer;
		}
		private function get_model_nhacungcap(){
			$provider = $this->model("ModelStatisticProvider",null,"statistic");
			return $provider;
		}
		private function get_model_nguoilam(){
			$personnel = $this->model("ModelStatisticPersonnel",null,"statistic");
			return $personnel;
		}
		private function mDie($message){
			$response["status"]= false;
			$response["message"] = self::$TAG.$message;
			die(json_encode($response));
		}
	}
?>