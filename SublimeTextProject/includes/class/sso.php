<?php
/**
 * @Project ID BNC
 * @File /includes/class/sso.php
 * @Author Quang Chau Tran (quangchauvn@gmail.com)
 * @Createdate 09/09/2014, 02:27 PM
 */
if(!defined('BNC_CODE')) {
    exit('Access Denied');
}
class Sso{  
	private $pubkey; 
	
	public function __construct($service){
		global $_B; 
        $this->pubkey = $_B['service'][$service]['pubkey']; 
	} 
    public function encrypt($data)
    {
        if (openssl_public_encrypt($data, $encrypted, $this->pubkey)){
            $data = base64_encode($encrypted);
            $data = base64_encode($data);
        }
        else
            throw new Exception('Unable to encrypt data. Perhaps it is bigger than the key size?');

        return $data;
    } 
}