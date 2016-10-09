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

			$Model = $this->model('home');

			$this->d['mes'] = $Model->showHello();

			$this->view($this->d,'html','index');
		}  
		function ban_hang(){	
	
			$input["goods_ids"] = $this->r->get_int('goods_ids','POST'); 
			$input["customer_id"] = $this->r->get_int('customer_id','POST'); 
			$input["agency_id"] = $this->r->get_int('agency_id','POST'); 
			$input["personnel_id"] = $this->r->get_int('personnel_id','POST'); 
			$input["bill_ghi_chu"] = $this->r->get_string('bill_ghi_chu','POST'); 
			$input["bill_tong_tien_hang"] = $this->r->get_int('bill_tong_tien_hang','POST'); 
			$input["bill_giam_gia"] = $this->r->get_int('bill_giam_gia','POST'); 
			$input["bill_tien_can_tra"] = $this->r->get_int('bill_tien_can_tra','POST'); 
			$input["bill_tien_da_tra"] = $this->r->get_int('bill_tien_da_tra','POST'); 

			$bill = new Bill($input);

			$response = array();

			try{
				$response["status"] = $bill->payment();
			}
			catch(Excetion $e){
				$response["status"] = false;
			}
		}
		function tra_hang(){
			$input_tra_hang["customer_id"] = $this->r->get_int("customer_id","POST");
			$input_tra_hang["agency_id"] = $this->r->get_int("agency_id","POST");
			$input_tra_hang["personnel_id"] = $this->r->get_int("personnel_id","POST");
			$input_tra_hang["rbill_ghi_chu"] = $this->r->get_int("rbill_ghi_chu","POST");
			$input_tra_hang["rbill_tong_tien_tra"] = $this->r->get_int("rbill_tong_tien_tra","POST");
			$input_tra_hang["rbill_phi_tra_hang"] = $this->r->get_int("rbill_phi_tra_hang","POST");
			$input_tra_hang["rbill_giam_gia"] = $this->r->get_int("rbill_giam_gia","POST");
			$input_tra_hang["rbill_tien_tra_khach"] = $this->r->get_string("rbill_tien_tra_khach","POST");
			$input_tra_hang["bill_id"] = $this->r->get_string("bill_id","POST");
			$input_tra_hang["goods_ids"] = $this->r->get_string("goods_ids","POST");
			
			$input_mua_hang_moi["goods_ids"] = $this->r->get_int('goods_ids','POST'); 
			$input_mua_hang_moi["customer_id"] = $this->r->get_int('customer_id','POST'); 
			$input_mua_hang_moi["agency_id"] = $this->r->get_int('agency_id','POST'); 
			$input_mua_hang_moi["personnel_id"] = $this->r->get_int('personnel_id','POST'); 
			$input_mua_hang_moi["bill_ghi_chu"] = $this->r->get_string('bill_ghi_chu','POST'); 
			$input_mua_hang_moi["bill_tong_tien_hang"] = $this->r->get_int('bill_tong_tien_hang','POST'); 
			$input_mua_hang_moi["bill_giam_gia"] = $this->r->get_int('bill_giam_gia','POST'); 
			$input_mua_hang_moi["bill_tien_can_tra"] = $this->r->get_int('bill_tien_can_tra','POST'); 
			$input_mua_hang_moi["bill_tien_da_tra"] = $this->r->get_int('bill_tien_da_tra','POST'); 
		
			$rbill = new RBill($input_tra_hang);
			$newbill = new Bill($input_mua_hang_moi);

			$response = array();

			try{
				$response["status"] = $rbill->return() && $newbill->payment();
			}
			catch(Excetion $e){
				$response["status"] = false;
			}
		}
	}
?>