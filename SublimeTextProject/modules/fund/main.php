<?php 
	class Fund extends iBNC{
		function __construct(){
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
		function lapphieuthu(){

			$phieu_input = $this->get_input();
			
			$receipt = $this->model("ModelFundReceipt",$phieu_input,"fund");
			$response = $receipt->createReceipt("chi");

			echo json_encode($response);
		}
		
		function lapphieuchi(){
			$phieu_input = $this->get_input();
			
			$receipt = $this->model("ModelFundReceipt",$phieu_input,"fund");
			$response = $receipt->createReceipt("thu");

			echo json_encode($response);
		}
		private function get_input(){

			$phieu_input["personnel_id"] = $this->r->get_int("personnel_id","POST");
			$phieu_input["agency_id"] = $this->r->get_int("agency_id","POST");
			$phieu_input["receipt_nguoi_nop_nhan"] = $this->r->get_string("receipt_nguoi_nop_nhan","POST");
			$phieu_input["receipt_kieu_thanh_toan"] = $this->r->get_string("receipt_kieu_thanh_toan","POST");
			$phieu_input["receipt_so_tai_khoan"] = $this->r->get_string("receipt_so_tai_khoan","POST");
			$phieu_input["receipt_gia_tri"] = $this->r->get_int("receipt_gia_tri","POST");
			$phieu_input["receipt_ly_do"] = $this->r->get_string("receipt_ly_do","POST");

			$this->check_empty($phieu_input["agency_id"]);
			$this->check_empty($phieu_input["personnel_id"]);
			
			$this->check_money($phieu_input["receipt_gia_tri"]);

			return $phieu_input;
		}
		
	}

?>