<?php
/**
 * @Project ID BNC
 * @File /modules/home/main.php
 * @Author Quang Chau Tran (quangchauvn@gmail.com)
 * @Createdate 09/04/2014, 02:19 PM
 */
if(!defined('BNC_CODE')) {
    exit('Access Denied');
}
class Home extends iBNC{ 
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
		$this->formtaophieuchi();
	}  
	function form(){
		?> 
			<h3></h3>
			<form method="POST" action="">
				<label></label><input type="text" name=""/></br>
				<label></label><input type="text" name=""/></br>
				<label></label><input type="text" name=""/></br>
				<label></label><input type="text" name=""/></br>
				<label></label><input type="text" name=""/></br>
				<label></label><input type="text" name=""/></br>
				<label></label><input type="text" name=""/></br>
				<input type="submit" value="submit"/>
			</form>
		<?php
	}
	function formtaophieuthu(){
		?> 
			<h3>Form Lập phiếu Thu</h3>
			<form method="POST" action="http://kiot.igarden.vn/fund/lapphieuthu">
				<label>Mã nhân viên*: </label><input type="text" name="personnel_id" /></br>
				<label>Mã chi nhánh*: </label><input type="text" name="agency_id" /></br>
				<label>Người nộp/nhận: </label><input type="text" name="receipt_nguoi_nop_nhan" /></br>
				<label>Kiểu thanh toán: </label><input type="text" name="receipt_kieu_thanh_toan" /></br>
				<label>Số tài khoản: </label><input type="text" name="receipt_so_tai_khoan" /></br>
				<label>Giá trị*: </label><input type="text" name="receipt_gia_tri" /></br>
				<label>Lý do: </label><input type="text" name="receipt_ly_do" /></br>
				<input type="submit" value="Tạo Phiếu"/>		
			</form>
		<?php 
	}
	function formtaophieuchi(){
		?> 
			<h3>Form Lập phiếu Chi</h3>
			<form method="POST" action="http://kiot.igarden.vn/fund/lapphieuchi">
				<label>Mã nhân viên*: </label><input type="text" name="personnel_id" /></br>
				<label>Mã chi nhánh*: </label><input type="text" name="agency_id" /></br>
				<label>Người nộp/nhận: </label><input type="text" name="receipt_nguoi_nop_nhan" /></br>
				<label>Kiểu thanh toán: </label><input type="text" name="receipt_kieu_thanh_toan" /></br>
				<label>Số tài khoản: </label><input type="text" name="receipt_so_tai_khoan" /></br>
				<label>Giá trị*: </label><input type="text" name="receipt_gia_tri" /></br>
				<label>Lý do: </label><input type="text" name="receipt_ly_do" /></br>
				<input type="submit" value="Tạo Phiếu"/>		
			</form>

		<?php 
	}
	function formdangky(){
		?>	
			<h3>Form Dang Ky</h3>
			<form method="POST" action="http://kiot.igarden.vn/personnel/dangky">
				<label>Tai Khoan: </label><input type="text" name="personnel_tai_khoan" /></br>
				<label>Mat Khau: </label><input type="password" name="personnel_mat_khau" /></br>
				<label>Ten Nguoi Dung: </label><input type="text" name="personnel_ten" /></br>
				<label>Chuc Vu: </label><input type="text" name="personnel_loai" /></br>
				<label>So Dien Thoai: </label><input type="text" name="personnel_sdt" /></br>
				<label>Dia Chi: </label><input type="text" name="personnel_dia_chi" /></br>
				<input type="submit" value="Dang Ky"/>		
			</form>
		<?php
	}
	function formdangnhap(){
		?>
			<h3>Form Dang Nhap</h3>
			<form method="POST" action="http://kiot.igarden.vn/personnel/dangnhap">
				<label>Tai Khoan: </label><input type="text" name="personnel_tai_khoan" /></br>
				<label>Mat Khau: </label><input type="password" name="personnel_mat_khau" /></br>
				<input type="submit" value="Dang Nhap"/>	
			</form>
		<?php
	}
	function formdangxuat(){
		?>
			<h3>Form Dang Xuat</h3>
			<form method="POST" action="http://kiot.igarden.vn/personnel/dangxuat">
				<label>Tai Khoan: </label><input type="text" name="personnel_tai_khoan" /></br>
				<input type="submit" value="Dang Xuat"/>	
			</form>
		<?php
	}
	function formdoimatkhau(){
		?>
			<h3>Form Doi Mat Khau</h3>
			<form method="POST" action="http://kiot.igarden.vn/personnel/doimatkhau">
				<label>Tai Khoan: </label><input type="text" name="personnel_tai_khoan" /></br>
				<label>Mat Khau: </label><input type="password" name="personnel_mat_khau" /></br>
				<label>Mat Khau Moi: </label><input type="password" name="personnel_mat_khau_moi" /></br>
				<input type="submit" value="Doi Mat Khau"/>	
			</form>
		<?php
	}
	function formdoithongtin(){
		/*?>	
			<h3>Form Cap Nhat Thong Tin</h3>
			<form method="POST" action="http://kiot.igarden.vn/personnel/capnhatthongtin">
				<label>Tai Khoan: </label><input type="text" name="personnel_tai_khoan" /></br>
				<label>Mat Khau: </label><input type="password" name="personnel_mat_khau" /></br>
				<label>Ten Nguoi Dung: </label><input type="text" name="personnel_ten" /></br>
				<label>Chuc Vu: </label><input type="text" name="personnel_loai" /></br>
				<label>So Dien Thoai: </label><input type="text" name="personnel_sdt" /></br>
				<label>Dia Chi: </label><input type="text" name="personnel_dia_chi" /></br>
				<input type="submit" value="Cap Nhat Thong Tin"/>		
			</form>
		<?php*/
	}
}
?>