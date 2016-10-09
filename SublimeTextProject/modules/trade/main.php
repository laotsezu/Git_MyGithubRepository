<?php 
	class Trade extends iBNC{
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
			echo "123";
		}  
		function test(){
			$bill = $this->model("ModelTradeBill",null,"trade");
			$result = $bill->get(0,40);
			echo json_encode($result);
		}
		function banhang(){	
	
			$input["current_goods_ids"] = $this->r->get_string('current_goods_ids','POST'); 
			$input["customer_id"] = $this->r->get_int('customer_id','POST',0); 
			$input["agency_id"] = $this->r->get_int('agency_id','POST',0); 
			$input["personnel_id"] = $this->r->get_int('personnel_id','POST',0); 
			$input["bill_ghi_chu"] = $this->r->get_string('bill_ghi_chu','POST'); 
			$input["bill_tong_tien_hang"] = $this->r->get_int('bill_tong_tien_hang','POST'); 
			$input["bill_giam_gia"] = $this->r->get_int('bill_giam_gia','POST'); 
			$input["bill_tien_can_tra"] = $this->r->get_int('bill_tien_can_tra','POST'); 
			$input["bill_tien_da_tra"] = $this->r->get_int('bill_tien_da_tra','POST'); 
			$input["bill_phuong_thuc"] = $this->r->get_int('bill_phuong_thuc','POST'); 

			$this->super_check_empty("current_goods_ids",$input["current_goods_ids"] );

			/*$this->super_check_empty("agency_id",$input["agency_id"]);
			$this->super_check_empty("personnel_id",$input["personnel_id"]);
			*/
		
			$this->check_money($input["bill_tien_da_tra"] );
			$this->check_money($input["bill_tong_tien_hang"]);

			$bill = $this->model("ModelTradeBill",$input,"trade");

			$response = array();	
			$response = $bill->payment();

			echo json_encode($response);
		}
		function trahang(){
			$input_tra_hang["customer_id"] = $this->r->get_int("customer_id","POST");
			$input_tra_hang["agency_id"] = $this->r->get_int("agency_id","POST");
			$input_tra_hang["personnel_id"] = $this->r->get_int("personnel_id","POST");
			$input_tra_hang["rbill_ghi_chu"] = $this->r->get_string("rbill_ghi_chu","POST");
			$input_tra_hang["rbill_tong_tien_tra"] = $this->r->get_int("rbill_tong_tien_tra","POST");
			$input_tra_hang["rbill_phi_tra_hang"] = $this->r->get_int("rbill_phi_tra_hang","POST");
			$input_tra_hang["rbill_giam_gia"] = $this->r->get_int("rbill_giam_gia","POST");
			$input_tra_hang["rbill_tien_tra_khach"] = $this->r->get_int("rbill_tien_tra_khach","POST");
			$input_tra_hang["bill_id"] = $this->r->get_int("bill_id","POST");
			$input_tra_hang["rbill_goods_ids"] = $this->r->get_string("rbill_goods_ids","POST");

			$this->check_empty($input_tra_hang["agency_id"]);
			$this->check_empty($input_tra_hang["personnel_id"]);
			$this->check_empty($input_tra_hang["bill_id"]);
			$this->check_empty($input_tra_hang["rbill_goods_ids"]);

			$this->check_money($input_tra_hang["rbill_tong_tien_tra"]);
			$this->check_money($input_tra_hang["rbill_tien_tra_khach"]);
			
			$input_mua_hang_moi["current_goods_ids"] = $this->r->get_string('current_goods_ids','POST'); 
			$input_mua_hang_moi["customer_id"] = $this->r->get_int('customer_id','POST'); 
			$input_mua_hang_moi["agency_id"] = $this->r->get_int('agency_id','POST'); 
			$input_mua_hang_moi["personnel_id"] = $this->r->get_int('personnel_id','POST'); 
			$input_mua_hang_moi["bill_ghi_chu"] = $this->r->get_string('bill_ghi_chu','POST'); 
			$input_mua_hang_moi["bill_tong_tien_hang"] = $this->r->get_int('bill_tong_tien_hang','POST'); 
			$input_mua_hang_moi["bill_giam_gia"] = $this->r->get_int('bill_giam_gia','POST'); 
			$input_mua_hang_moi["bill_tien_can_tra"] = $this->r->get_int('bill_tien_can_tra','POST'); 
			$input_mua_hang_moi["bill_tien_da_tra"] = $this->r->get_int('bill_tien_da_tra','POST'); 
		
			$this->check_empty($input_mua_hang_moi["agency_id"] );
			$this->check_empty($input_mua_hang_moi["personnel_id"]);
			$this->check_empty($input_mua_hang_moi["bill_id"]);
			$this->check_empty($input_mua_hang_moi["rbill_goods_ids"]);

			$this->check_money($input_mua_hang_moi["rbill_tong_tien_tra"]);
			$this->check_money($input_mua_hang_moi["rbill_tien_tra_khach"] );


			$rbill = $this->model("ModelTradeRBill",$input_tra_hang,"trade");
			$newbill = $this->model("ModelTradeBill",$input_mua_hang_moi,"trade");

			$response = array();

			$response = $rbill->accept() && $newbill->payment() ;
			

			echo json_encode($response);
		}
		
	}
?>